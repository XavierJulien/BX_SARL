package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.CapteurChaleurI;
import interfaces.CapteurChaleurChauffageI;
import ports.CapteurChaleurInboundPort;
import ports.CapteurChauffageOutboundPort;

@RequiredInterfaces(required = {CapteurChaleurI.class, CapteurChaleurChauffageI.class})
@OfferedInterfaces(offered = {CapteurChaleurI.class, CapteurChaleurChauffageI.class})
public class CapteurChaleur extends AbstractComponent {

	protected final String				uri ;

	protected final String				capteurChaleurInboundPortURI ;
	
	protected final String				linkWithChauffageOutboundPortURI ;

	protected final CapteurChaleurInboundPort capteurChaleurInboundPort;
	
	protected final CapteurChauffageOutboundPort capteurChauffageOutboundPort;

	protected double power = 0;



	protected CapteurChaleur(String uri, String capteurChaleurInboundPortURI, String linkWithChauffageOutboundPortURI) throws Exception{

		super(uri, 1, 1);
		this.uri = uri;
		this.capteurChaleurInboundPortURI = capteurChaleurInboundPortURI;
		this.linkWithChauffageOutboundPortURI = linkWithChauffageOutboundPortURI;

		capteurChaleurInboundPort = new CapteurChaleurInboundPort(capteurChaleurInboundPortURI, this) ;
		capteurChaleurInboundPort.publishPort() ;
		capteurChauffageOutboundPort = new CapteurChauffageOutboundPort(linkWithChauffageOutboundPortURI, this);
		capteurChauffageOutboundPort.localPublishPort() ;

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		this.tracer.setTitle("CapteurChaleur") ;
		this.tracer.setRelativePosition(3, 0) ;
	}

	public double sendChaleur() throws Exception {
		this.logMessage("Sending chaleur power....") ;
		power+=0.2;
		return Math.abs(Math.sin(power));
		//		return 0.3;

	}

	@Override
	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting CapteurChaleur component.") ;
	}
	
	@Override
	public void execute() throws Exception {
		super.execute();
		// Schedule the first service method invocation in one second.
		
				this.scheduleTask(
						new AbstractComponent.AbstractTask() {
							@Override
							public void run() {
								try {
									((CapteurChaleur)this.getTaskOwner()).sendChaleur() ;

								} catch (Exception e) {
									throw new RuntimeException(e) ;
								}
							}
						},
						1000, TimeUnit.MILLISECONDS);
	}
	
	public void getHeating() throws Exception {
		double heat = this.capteurChauffageOutboundPort.getHeating();
		this.logMessage("The CapteurChaleur is getting "+heat+" degrees from the Chauffage") ;
	}

	// ------------------------------------------------------------------------
	// FINALISE / SHUTDOWN
	// ------------------------------------------------------------------------

	@Override
	public void finalise() throws Exception {
		capteurChauffageOutboundPort.doDisconnection();
		super.finalise();
	}

	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			capteurChaleurInboundPort.unpublishPort();
			capteurChauffageOutboundPort.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.shutdown();
	}
}
