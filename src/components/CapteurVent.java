package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.CapteurVentI;
import ports.CapteurVentInboundPort;

@RequiredInterfaces(required = {CapteurVentI.class})
@OfferedInterfaces(offered = {CapteurVentI.class})
public class CapteurVent extends AbstractComponent {

	protected final String				uri ;
	/** The inbound port URI for the eolienne.*/
	protected final String				capteurVentInboundPortURI ;

	protected final CapteurVentInboundPort capteurVentInboundPort;

	protected double power = 0;



	protected CapteurVent(String uri, String capteurVentInboundPortURI) throws Exception{

		super(uri, 1, 1);
		this.uri = uri;
		this.capteurVentInboundPortURI = capteurVentInboundPortURI;

		capteurVentInboundPort = new CapteurVentInboundPort(capteurVentInboundPortURI, this) ;
		capteurVentInboundPort.publishPort() ;

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		this.tracer.setTitle("CapteurVent") ;
		this.tracer.setRelativePosition(2, 1) ;
	}

	public double sendWind() throws Exception {
		this.logMessage("Sending wind power....") ;
		power+=0.2;
		return Math.abs(Math.sin(power));
		//		return 0.3;

	}

	@Override
	public void			start() throws ComponentStartException
	{
		super.start() ;
		this.logMessage("starting CapteurVent component.") ;
		// Schedule the first service method invocation in one second.
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							((CapteurVent)this.getTaskOwner()).sendWind() ;

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
			capteurVentInboundPort.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.shutdown();
	}
}
