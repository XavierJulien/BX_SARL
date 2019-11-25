package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.HeatSensorI;
import interfaces.HeatSensorHeatingI;
import ports.HeatSensorInboundPort;
import ports.HeatSensorHeatingOutboundPort;

@RequiredInterfaces(required = {HeatSensorI.class, HeatSensorHeatingI.class})
@OfferedInterfaces(offered = {HeatSensorI.class, HeatSensorHeatingI.class})
public class HeatSensor extends AbstractComponent {

	protected final String				uri ;

	protected final String				heatSensorInboundPortURI ;
	
	protected final String				linkWithHeatingOutboundPortURI ;

	protected final HeatSensorInboundPort heatSensorInboundPort;
	
	protected final HeatSensorHeatingOutboundPort heatSensorHeatingOutboundPort;

	protected double power = 0;



	protected HeatSensor(String uri, String heatSensorInboundPortURI, String linkWithHeatingOutboundPortURI) throws Exception{

		super(uri, 1, 1);
		this.uri = uri;
		this.heatSensorInboundPortURI = heatSensorInboundPortURI;
		this.linkWithHeatingOutboundPortURI = linkWithHeatingOutboundPortURI;

		heatSensorInboundPort = new HeatSensorInboundPort(heatSensorInboundPortURI, this) ;
		heatSensorInboundPort.publishPort() ;
		heatSensorHeatingOutboundPort = new HeatSensorHeatingOutboundPort(linkWithHeatingOutboundPortURI, this);
		heatSensorHeatingOutboundPort.localPublishPort() ;

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		this.tracer.setTitle("HeatSensor") ;
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
									((HeatSensor)this.getTaskOwner()).sendTemperature() ;

								} catch (Exception e) {
									throw new RuntimeException(e) ;
								}
							}
						},
						1000, TimeUnit.MILLISECONDS);
	}
	
	public void getHeating() throws Exception {
		double heat = this.heatSensorHeatingOutboundPort.getHeating();
		this.logMessage("The HeatSensor sees that the heating increase the temperature of "+heat+" degrees") ;
	}

	// ------------------------------------------------------------------------
	// FINALISE / SHUTDOWN
	// ------------------------------------------------------------------------

	@Override
	public void finalise() throws Exception {
		heatSensorHeatingOutboundPort.doDisconnection();
		super.finalise();
	}

	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			heatSensorInboundPort.unpublishPort();
			heatSensorHeatingOutboundPort.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.shutdown();
	}
}
