package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cyphy.plugins.devs.SupervisorPlugin;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.sensors.TemperatureSensorHeatingI;
import interfaces.sensors.TemperatureSensorI;
import ports.sensors.TemperatureSensorHeatingOutboundPort;
import ports.sensors.TemperatureSensorInboundPort;
import ports.sensors.TemperatureSensorOutboundPort;

@RequiredInterfaces(required = {TemperatureSensorI.class, TemperatureSensorHeatingI.class})
@OfferedInterfaces(offered = {TemperatureSensorI.class, TemperatureSensorHeatingI.class})
public class TemperatureSensor extends AbstractComponent {

	protected SupervisorPlugin		sp ;
	
	protected final String				uri ;

	protected final String				temperatureSensorInboundPortURI ;
	
	protected final String				temperatureSensorOutboundPortURI ;
	
	protected final String				linkWithHeatingOutboundPortURI ;

	protected final TemperatureSensorInboundPort temperatureSensorInboundPort;
	
	protected final TemperatureSensorHeatingOutboundPort temperatureSensorHeatingOutboundPort;
	
	protected final TemperatureSensorOutboundPort temperatureSensorOutboundPort;

	protected double power = 15;



	protected TemperatureSensor(String uri, String temperatureSensorInboundPortURI, String linkWithHeatingOutboundPortURI, String temperatureSensorOutboundPortURI) throws Exception{

		super(uri, 1, 1);
		this.uri = uri;
		this.temperatureSensorInboundPortURI = temperatureSensorInboundPortURI;
		this.linkWithHeatingOutboundPortURI = linkWithHeatingOutboundPortURI;
		this.temperatureSensorOutboundPortURI = temperatureSensorOutboundPortURI;

		temperatureSensorInboundPort = new TemperatureSensorInboundPort(temperatureSensorInboundPortURI, this) ;
		temperatureSensorInboundPort.publishPort() ;
		temperatureSensorHeatingOutboundPort = new TemperatureSensorHeatingOutboundPort(linkWithHeatingOutboundPortURI, this);
		temperatureSensorHeatingOutboundPort.localPublishPort() ;
		temperatureSensorOutboundPort = new TemperatureSensorOutboundPort(temperatureSensorOutboundPortURI, this);
		temperatureSensorOutboundPort.localPublishPort() ;
		
		
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		this.tracer.setTitle("TemperatureSensor") ;
		this.tracer.setRelativePosition(3, 0) ;
	}

	public void sendTemperature() throws Exception {
		this.logMessage("The temperature is "+power+" degrees") ;
		//return Math.abs(Math.sin(power));
		this.temperatureSensorOutboundPort.sendTemperature(power+(int)(Math.random()*100)) ;

	}

	@Override
	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Heat Sensor component.") ;
	}
	
	@Override
	public void execute() throws Exception {
		super.execute();
		
		/*Map<String,AbstractAtomicModelDescriptor> atomicModelDescriptors =
				new HashMap<>() ;

		System.out.println("AVANT PUT");
		atomicModelDescriptors.put(
			WindSpeedModel.URI,
			AtomicModelDescriptor.create(
				WindSpeedModel.class,
				WindSpeedModel.URI,
				TimeUnit.SECONDS,
				null,
				SimulationEngineCreationMode.ATOMIC_ENGINE)
		) ;
		
		System.out.println("AVANT ARCHI");
		
		Map<String,CoupledModelDescriptor> coupledModelDescriptors =
				new HashMap<>() ;
		
		ArchitectureI architecture =
				new Architecture(
						WindSpeedModel.URI,
						atomicModelDescriptors,
						coupledModelDescriptors,
						TimeUnit.SECONDS) ;
		System.out.println("ICI");
		SimulationEngine se = architecture.constructSimulator() ;
		se.setDebugLevel(0);
		
		
		System.out.println("APRES CONSTRUCT");
		
		Map<String, Object> simParams = new HashMap<String, Object>() ;

		String modelURI = WindSpeedModel.URI;
		simParams.put(
				modelURI + ":" + PlotterDescription.PLOTTING_PARAM_NAME,
				new PlotterDescription(
						"Wind sensor Model",
						"Time (sec)",
						"Connected/Interrupted",
						SimulationMain.ORIGIN_X,
						SimulationMain.ORIGIN_Y,
						SimulationMain.getPlotterWidth(),
						SimulationMain.getPlotterHeight())) ;
		se.setSimulationRunParameters(simParams) ;
		this.logMessage("supervisor component begins simulation.") ;
		*/
		// Schedule the first service method invocation in one second.
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							while(true) {
								((TemperatureSensor)this.getTaskOwner()).sendTemperature() ;
								Thread.sleep(1000);
							}
							

						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				},
				1000, TimeUnit.MILLISECONDS);
		
		/*long start = System.currentTimeMillis() ;
		se.doStandAloneSimulation(0.0, 5000.0) ;
		long end = System.currentTimeMillis() ;
		System.out.println(se.getFinalReport()) ;
		System.out.println("Simulation ends. " + (end - start)) ;*/
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
		temperatureSensorOutboundPort.doDisconnection();
		super.finalise();
	}

	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			temperatureSensorInboundPort.unpublishPort();
			temperatureSensorHeatingOutboundPort.unpublishPort();
			temperatureSensorOutboundPort.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.shutdown();
	}
}
