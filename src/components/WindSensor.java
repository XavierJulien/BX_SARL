package components;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.cyphy.interfaces.EmbeddingComponentStateAccessI;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.simulators.SimulationEngine;
import interfaces.sensors.WindSensorI;
import ports.sensors.WindSensorInboundPort;
import ports.sensors.WindSensorOutboundPort;
import simulation.components.windSensor.WindSensorSimulatorPlugin;
import simulation.events.kettle.FillKettle;
import simulation.events.kettle.KettleUpdater;
import simulation.models.windSensor.WindSensorCoupledModel;

@RequiredInterfaces(required = {WindSensorI.class})
@OfferedInterfaces(offered = {WindSensorI.class})
public class WindSensor extends AbstractCyPhyComponent implements EmbeddingComponentStateAccessI{

	
	protected WindSensorSimulatorPlugin		asp ;
	
	protected final String				uri ;
	/** The inbound port URI for the eolienne.*/
	protected final String				windSensorInboundPortURI ;
	protected final String				windSensorOutboundPortURI ;

	protected final WindSensorInboundPort windSensorInboundPort;
	protected final WindSensorOutboundPort windSensorOutboundPort;

	private static double x = 0;
	
	
	
	protected double power = 0;



	protected WindSensor(String uri, String windSensorInboundPortURI, String windSensorOutboundPortURI) throws Exception{

		super(uri, 1, 1);
		this.uri = uri;
		this.windSensorInboundPortURI = windSensorInboundPortURI;
		this.windSensorOutboundPortURI = windSensorOutboundPortURI;

		windSensorInboundPort = new WindSensorInboundPort(windSensorInboundPortURI, this) ;
		windSensorInboundPort.publishPort() ;
		
		windSensorOutboundPort = new WindSensorOutboundPort(windSensorOutboundPortURI, this) ;
		windSensorOutboundPort.localPublishPort() ;

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	
		
		this.initialise();

		this.tracer.setTitle("WindSensor") ;
		this.tracer.setRelativePosition(2, 0) ;
	}

	public void sendWindSpeed() throws Exception {
		this.logMessage("Sending wind power....") ;
		//Waiting for simulation, here's a little function to replace it for the moment
		x+=0.1;
		power =(((Math.sin(x+8)+1.0/10*Math.cos((x+2)*5)+ Math.cos((x*7)/2.0))*3)+6);
		this.windSensorOutboundPort.sendWindSpeed(power) ;
		//		return 0.3;

	}

	@Override
	public void			start() throws ComponentStartException
	{
		super.start() ;
		this.logMessage("starting WindSensor component.") ;
		
	}
	
	
	
	protected void initialise() throws Exception {
		// The coupled model has been made able to create the simulation
		// architecture description.
		Architecture localArchitecture = this.createLocalArchitecture(null) ;
		// Create the appropriate DEVS simulation plug-in.
		this.asp = new WindSensorSimulatorPlugin() ;
		// Set the URI of the plug-in, using the URI of its associated
		// simulation model.
		this.asp.setPluginURI(localArchitecture.getRootModelURI()) ;
		// Set the simulation architecture.
		this.asp.setSimulationArchitecture(localArchitecture) ;
		
		
		// Install the plug-in on the component, starting its own life-cycle.
		this.installPlugin(this.asp) ;
		// Toggle logging on to get a log on the screen.
		this.toggleLogging() ;
	}
	
	
	
	@Override
	public void execute() throws Exception {
		super.execute();

		SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 500L ;
		HashMap<String,Object> simParams = new HashMap<String,Object>() ;
		simParams.put("windSensorRef", this) ;
		this.asp.setSimulationRunParameters(simParams) ;
		// Start the simulation.
		this.runTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							Thread.sleep(1000);
							asp.doStandAloneSimulation(0.0, 5000.0) ;
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				}) ;
		Thread.sleep(10L) ;
		
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							while(true) {
								
									
									((WindSensor)this.getTaskOwner()).sendWindSpeed();
								
								
								Thread.sleep(1000);
							}
							

						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				},
				1000, TimeUnit.MILLISECONDS);
		
		
	}
	
	// ------------------------------------------------------------------------
	// MODELISATION
	// ------------------------------------------------------------------------

	
	
	@Override
	protected Architecture createLocalArchitecture(String architectureURI) throws Exception{
		return WindSensorCoupledModel.build() ;
	}

	
	
	@Override
	public Object getEmbeddingComponentStateValue(String name) throws Exception {
		if(name == "power") {
			return new Double(power);
		}else {
			return null;
		}
		
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
			windSensorOutboundPort.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.shutdown();
	}
}
