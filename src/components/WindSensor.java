package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.WindSensorI;
import ports.WindSensorInboundPort;

@RequiredInterfaces(required = {WindSensorI.class})
@OfferedInterfaces(offered = {WindSensorI.class})
public class WindSensor extends AbstractComponent {

	protected final String				uri ;
	/** The inbound port URI for the eolienne.*/
	protected final String				windSensorInboundPortURI ;

	protected final WindSensorInboundPort windSensorInboundPort;

	protected double power = 0;



	protected WindSensor(String uri, String windSensorInboundPortURI) throws Exception{

		super(uri, 1, 1);
		this.uri = uri;
		this.windSensorInboundPortURI = windSensorInboundPortURI;

		windSensorInboundPort = new WindSensorInboundPort(windSensorInboundPortURI, this) ;
		windSensorInboundPort.publishPort() ;

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		this.tracer.setTitle("WindSensor") ;
		this.tracer.setRelativePosition(2, 0) ;
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
		this.logMessage("starting WindSensor component.") ;
		
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
									((WindSensor)this.getTaskOwner()).sendWind() ;

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
			windSensorInboundPort.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.shutdown();
	}
}
