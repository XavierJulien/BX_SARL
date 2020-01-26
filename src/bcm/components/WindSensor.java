package bcm.components;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import bcm.interfaces.sensors.WindSensorI;
import bcm.ports.sensors.WindSensorInboundPort;
import bcm.ports.sensors.WindSensorOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.cyphy.interfaces.EmbeddingComponentStateAccessI;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.simulators.SimulationEngine;
import simulation.models.windSensor.WindSensorCoupledModel;
import simulation.models.windSensor.WindSensorModel;
import simulation.simulatorplugins.WindSensorSimulatorPlugin;

@RequiredInterfaces(required = {WindSensorI.class})
@OfferedInterfaces(offered = {WindSensorI.class})
public class WindSensor extends AbstractCyPhyComponent implements EmbeddingComponentStateAccessI{

	
	
	protected final String				uri ;
	protected final String				windSensorInboundPortURI ;
	protected final String				windSensorOutboundPortURI ;
	protected final WindSensorInboundPort windSensorInboundPort;
	protected final WindSensorOutboundPort windSensorOutboundPort;

	protected WindSensorSimulatorPlugin		asp ;
	
	protected final String 				windSensorRef;
	
	protected double power;


//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected WindSensor(String uri, 
						 String windSensorInboundPortURI, 
						 String windSensorOutboundPortURI,
						 String windSensorRef) throws Exception{
		super(uri, 1, 1);
		
		assert uri != null;
		assert windSensorInboundPortURI != null;
		assert windSensorOutboundPortURI != null;
		assert windSensorRef != null;
		
		this.uri = uri;
		this.windSensorInboundPortURI = windSensorInboundPortURI;
		this.windSensorOutboundPortURI = windSensorOutboundPortURI;

		
		//-------------------PUBLISH-------------------
		windSensorInboundPort = new WindSensorInboundPort(windSensorInboundPortURI, this) ;
		windSensorInboundPort.publishPort() ;
		windSensorOutboundPort = new WindSensorOutboundPort(windSensorOutboundPortURI, this) ;
		windSensorOutboundPort.localPublishPort() ;

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}
		

		//-------------------GUI-------------------
		this.tracer.setTitle("WindSensor") ;
		this.tracer.setRelativePosition(2, 0) ;

		//----------------Variables----------------
		
		this.windSensorRef = windSensorRef;
		power = 0;
		
		//----------------Modelisation-------------
		
		this.initialise();
	}

//------------------------------------------------------------------------
//----------------------------SERVICES------------------------------------
//------------------------------------------------------------------------

	
	/**
	 * this method is used to send the wind speed to the controller
	 * @throws Exception
	 */
	public void sendWindSpeed() throws Exception {
		this.logMessage("Sending wind power....") ;
		power = (Double)this.asp.getModelStateValue(WindSensorModel.URI, "currentWind" );
		this.windSensorOutboundPort.sendWindSpeed(power) ;
	}

	
	/**
	 * Start the component
	 */
	@Override
	public void start() throws ComponentStartException {
		super.start() ;
		this.logMessage("starting WindSensor component.") ;
		
	}
	
	
//------------------------------------------------------------------------
//----------------------INITIALISE & EXECUTE------------------------------
//------------------------------------------------------------------------

	protected void initialise() throws Exception {
		Architecture localArchitecture = this.createLocalArchitecture(null) ;
		this.asp = new WindSensorSimulatorPlugin() ;
		this.asp.setPluginURI(localArchitecture.getRootModelURI()) ;
		this.asp.setSimulationArchitecture(localArchitecture) ;
		this.installPlugin(this.asp) ;
		this.toggleLogging() ;
	}
	
	
	/**
	 * Execute the component, first the simulation starts  then the component behaviour
	 */
	@Override
	public void execute() throws Exception {
		super.execute();

		//---------------SIMULATION---------------
		SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 1000L ;
		HashMap<String,Object> simParams = new HashMap<String,Object>() ;
		simParams.put(windSensorRef, this) ;
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
		
		
		//---------------BCM---------------
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
	
//------------------------------------------------------------------------
//-------------------------SIMULATION METHODS-----------------------------
//------------------------------------------------------------------------
	
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
	
//------------------------------------------------------------------------
//----------------------------FINALISE------------------------------------
//------------------------------------------------------------------------
	@Override
	public void finalise() throws Exception {
		super.finalise();
	}

//------------------------------------------------------------------------
//----------------------------SHUTDOWN------------------------------------
//------------------------------------------------------------------------
	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			windSensorInboundPort.unpublishPort();
			windSensorOutboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
