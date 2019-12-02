package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.TemperatureSensorI;
import interfaces.TemperatureSensorHeatingI;
import ports.TemperatureSensorInboundPort;
import ports.TemperatureSensorHeatingOutboundPort;

@RequiredInterfaces(required = {TemperatureSensorI.class, TemperatureSensorHeatingI.class})
@OfferedInterfaces(offered = {TemperatureSensorI.class, TemperatureSensorHeatingI.class})
public class TemperatureSensor extends AbstractComponent {

	protected final String				uri ;

	protected final String				temperatureSensorInboundPortURI ;
	
	protected final String				linkWithHeatingOutboundPortURI ;

	protected final TemperatureSensorInboundPort temperatureSensorInboundPort;
	
	protected final TemperatureSensorHeatingOutboundPort temperatureSensorHeatingOutboundPort;

	protected double power = 0;



	protected TemperatureSensor(String uri, String temperatureSensorInboundPortURI, String linkWithHeatingOutboundPortURI) throws Exception{

		super(uri, 1, 1);
		this.uri = uri;
		this.temperatureSensorInboundPortURI = temperatureSensorInboundPortURI;
		this.linkWithHeatingOutboundPortURI = linkWithHeatingOutboundPortURI;

		temperatureSensorInboundPort = new TemperatureSensorInboundPort(temperatureSensorInboundPortURI, this) ;
		temperatureSensorInboundPort.publishPort() ;
		temperatureSensorHeatingOutboundPort = new TemperatureSensorHeatingOutboundPort(linkWithHeatingOutboundPortURI, this);
		temperatureSensorHeatingOutboundPort.localPublishPort() ;

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		this.tracer.setTitle("TemperatureSensor") ;
		this.tracer.setRelativePosition(3, 0) ;
	}

	public double sendTemperature() throws Exception {
		this.logMessage("Sending heat....") ;
		power+=0.2;
		return Math.abs(Math.sin(power));
		//		return 0.3;

	}

	@Override
	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Heat Sensor component.") ;
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
									((TemperatureSensor)this.getTaskOwner()).sendTemperature() ;

								} catch (Exception e) {
									throw new RuntimeException(e) ;
								}
							}
						},
						1000, TimeUnit.MILLISECONDS);
	}
	
	public void getHeating() throws Exception {
		double heat = this.temperatureSensorHeatingOutboundPort.getHeating();
		this.logMessage("The TemperatureSensor sees that the heating increase the temperature of "+heat+" degrees") ;
	}

	// ------------------------------------------------------------------------
	// FINALISE / SHUTDOWN
	// ------------------------------------------------------------------------

	@Override
	public void finalise() throws Exception {
		temperatureSensorHeatingOutboundPort.doDisconnection();
		super.finalise();
	}

	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			temperatureSensorInboundPort.unpublishPort();
			temperatureSensorHeatingOutboundPort.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.shutdown();
	}
}
