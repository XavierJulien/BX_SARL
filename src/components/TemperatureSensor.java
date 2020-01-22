package components;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.cyphy.interfaces.EmbeddingComponentStateAccessI;
import fr.sorbonne_u.components.cyphy.plugins.devs.SupervisorPlugin;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.simulators.SimulationEngine;
import interfaces.sensors.TemperatureSensorHeatingI;
import interfaces.sensors.TemperatureSensorI;
import ports.sensors.TemperatureSensorHeatingOutboundPort;
import ports.sensors.TemperatureSensorInboundPort;
import ports.sensors.TemperatureSensorOutboundPort;
import simulation.components.heatSensor.HeatSensorSimulatorPlugin;
import simulation.models.heatSensor.HeatSensorCoupledModel;
import simulation.models.heatSensor.HeatSensorModel;

@RequiredInterfaces(required = {TemperatureSensorI.class, TemperatureSensorHeatingI.class})
@OfferedInterfaces(offered = {TemperatureSensorI.class, TemperatureSensorHeatingI.class})
public class TemperatureSensor 
extends		AbstractCyPhyComponent
implements	EmbeddingComponentStateAccessI
{

	protected SupervisorPlugin		sp ;
	protected final String				uri ;
	protected final String				temperatureSensorInboundPortURI ;
	protected final String				temperatureSensorOutboundPortURI ;
	protected final String				linkWithHeatingOutboundPortURI ;
	protected final TemperatureSensorInboundPort temperatureSensorInboundPort;
	protected final TemperatureSensorHeatingOutboundPort temperatureSensorHeatingOutboundPort;
	protected final TemperatureSensorOutboundPort temperatureSensorOutboundPort;
	protected double temperature = 5;



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
		
		this.initialise();
	}

	public void sendTemperature() throws Exception {
		this.logMessage("The temperature is "+temperature+" degrees") ;
		//return Math.abs(Math.sin(power));
		this.temperatureSensorOutboundPort.sendTemperature(temperature) ;
	}
	public void getHeating() throws Exception {
		double heat = this.temperatureSensorHeatingOutboundPort.getHeating();
		temperature += heat;
		//this.logMessage("The TemperatureSensor sees that the heating increase the temperature of "+heat+" degrees") ;
	}

	
	//------------------------------------------------------------------------
	//----------------------------MODEL METHODS-------------------------------
	//------------------------------------------------------------------------
		

	@Override
	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Heat Sensor component.") ;
	}
	
	protected void initialise() throws Exception {
		// The coupled model has been made able to create the simulation
		// architecture description.
		Architecture localArchitecture = this.createLocalArchitecture(null) ;
		// Create the appropriate DEVS simulation plug-in.
		this.asp = new HeatSensorSimulatorPlugin() ;
		
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
		simParams.put("heatSensorRef", this) ;
		this.asp.setSimulationRunParameters(simParams) ;
		Thread.sleep(1000L);
		this.runTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
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
								temperature = (Double)(((TemperatureSensor)this.getTaskOwner()).asp.getModelStateValue(HeatSensorModel.URI, "currentTemperature"));
								((TemperatureSensor)this.getTaskOwner()).sendTemperature() ;
								
								((TemperatureSensor)this.getTaskOwner()).getHeating() ;
								Thread.sleep(1000);
							}
							

						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				},
				1000, TimeUnit.MILLISECONDS);
	}
	
	@Override
	protected Architecture createLocalArchitecture(String architectureURI) throws Exception{
		return HeatSensorCoupledModel.build() ;
	}

	@Override
	public Object getEmbeddingComponentStateValue(String name) throws Exception{
		if(name.equals("temperature")) {
			return ((Double)temperature);
		}else {
			return null;
		}
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
