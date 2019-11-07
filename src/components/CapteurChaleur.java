package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.CapteurChaleurI;
import ports.CapteurChaleurInboundPort;

@RequiredInterfaces(required = {CapteurChaleurI.class})
@OfferedInterfaces(offered = {CapteurChaleurI.class})
public class CapteurChaleur extends AbstractComponent {

	protected final String				uri ;
	/** The inbound port URI for the eolienne.*/
	protected final String				capteurChaleurInboundPortURI ;

	protected final CapteurChaleurInboundPort capteurChaleurInboundPort;

	protected double power = 0;



	protected CapteurChaleur(String uri, String capteurChaleurInboundPortURI) throws Exception{

		super(uri, 1, 1);
		this.uri = uri;
		this.capteurChaleurInboundPortURI = capteurChaleurInboundPortURI;

		capteurChaleurInboundPort = new CapteurChaleurInboundPort(capteurChaleurInboundPortURI, this) ;
		capteurChaleurInboundPort.publishPort() ;

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

	// ------------------------------------------------------------------------
	// FINALISE / SHUTDOWN
	// ------------------------------------------------------------------------


	@Override
	public void finalise() throws Exception {
		super.finalise();
	}

	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			capteurChaleurInboundPort.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.shutdown();
	}
}
