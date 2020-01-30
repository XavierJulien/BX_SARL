package bcm.components;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import bcm.interfaces.sensors.TemperatureSensorHeatingI;
import bcm.interfaces.sensors.TemperatureSensorI;
import bcm.ports.sensors.TemperatureSensorHeatingOutboundPort;
import bcm.ports.sensors.TemperatureSensorInboundPort;
import bcm.ports.sensors.TemperatureSensorOutboundPort;
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
import simulation.models.heatSensor.HeatSensorCoupledModel;
import simulation.models.heatSensor.HeatSensorModel;
import simulation.simulatorplugins.HeatSensorSimulatorPlugin;

/**
 * this class represents the temperature sensore of the house, it can send the current temperature to the controller and receive temperature information from the heating
 * @author Julien Xavier et Alexis Belanger
 *
 */
@RequiredInterfaces(required = {TemperatureSensorI.class, TemperatureSensorHeatingI.class})
@OfferedInterfaces(offered = {TemperatureSensorI.class, TemperatureSensorHeatingI.class})
public class TemperatureSensor 
extends		AbstractCyPhyComponent
implements	EmbeddingComponentStateAccessI
{

	protected final String				uri ;
	protected final String				temperatureSensorInboundPortURI ;
	protected final String				temperatureSensorOutboundPortURI ;
	protected final String				linkWithHeatingOutboundPortURI ;
	protected final TemperatureSensorInboundPort temperatureSensorInboundPort;
	protected final TemperatureSensorHeatingOutboundPort temperatureSensorHeatingOutboundPort;
	protected final TemperatureSensorOutboundPort temperatureSensorOutboundPort;
	
	
	protected HeatSensorSimulatorPlugin		asp ;

	protected final String temperatureSensorRef;
	protected double temperature;


	/**
	 * the temperature sensor component constructor
	 * @param uri the temperature sensor URI
	 * @param temperatureSensorInboundPortURI Port for the temperature sensor &lt- controller connection
	 * @param linkWithHeatingOutboundPortURI  Port for the temperature sensor -&gt heating connection
	 * @param temperatureSensorOutboundPortURI Port for the temperature sensor -&gt controller connection
	 * @param tempertureSensorRef the ref of the temperature sensor model
	 * @throws Exception
	 * 
	 * <pre> 
     * pre uri != null
     * pre temperatureSensorInboundPortURI != null
     * pre linkWithHeatingOutboundPortURI != null
     * pre temperatureSensorOutboundPortURI != null
     * pre tempertureSensorRef != null
	 * </pre>
	 */
	protected TemperatureSensor(String uri,
								String temperatureSensorInboundPortURI,
								String linkWithHeatingOutboundPortURI,
								String temperatureSensorOutboundPortURI,
								String tempertureSensorRef) throws Exception{

		super(uri, 1, 1);
		
		assert uri != null;
		assert temperatureSensorInboundPortURI != null;
		assert linkWithHeatingOutboundPortURI != null;
		assert temperatureSensorOutboundPortURI != null;
		assert tempertureSensorRef != null;
		
		this.uri = uri;
		this.temperatureSensorInboundPortURI = temperatureSensorInboundPortURI;
		this.linkWithHeatingOutboundPortURI = linkWithHeatingOutboundPortURI;
		this.temperatureSensorOutboundPortURI = temperatureSensorOutboundPortURI;
		

		
		//-------------------PUBLISH-------------------
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

		//-------------------GUI-------------------
		this.tracer.setTitle("TemperatureSensor") ;
		this.tracer.setRelativePosition(3, 0) ;
		
		//----------------Variables----------------
		temperature = 12;
		this.temperatureSensorRef = tempertureSensorRef;
		
		//----------------Modelisation-------------

		this.initialise();
	}

	
//------------------------------------------------------------------------
//----------------------------SERVICES------------------------------------
//------------------------------------------------------------------------

	
	/**
	 * this method is used to send the current temperature to the controller
	 * @throws Exception
	 */
	public void sendTemperature() throws Exception {
		this.logMessage("The temperature is "+temperature+" degrees") ;
		this.temperatureSensorOutboundPort.sendTemperature(temperature) ;
	}
	
	/**
	 * this method is used to get the heat produced by the heating to update the current temperature
	 * @throws Exception
	 */
	public void getHeating() throws Exception {
		double heat = this.temperatureSensorHeatingOutboundPort.getHeating();
		temperature += heat;
	}

	/**
	 * start the component
	 */
	@Override
	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Heat Sensor component.") ;
	}
	
//------------------------------------------------------------------------
//----------------------INITIALISE & EXECUTE------------------------------
//------------------------------------------------------------------------
		
	protected void initialise() throws Exception {
		Architecture localArchitecture = this.createLocalArchitecture(null) ;
		this.asp = new HeatSensorSimulatorPlugin() ;
		this.asp.setPluginURI(localArchitecture.getRootModelURI()) ;
		this.asp.setSimulationArchitecture(localArchitecture) ;
		this.installPlugin(this.asp) ;
		this.toggleLogging() ;
	}
	
	/**
	 * execute the component, first the simulation starts, then the component behaviour
	 */
	@Override
	public void execute() throws Exception {
		super.execute();
		
		
		//---------------SIMULATION---------------
		SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 500L ;
		HashMap<String,Object> simParams = new HashMap<String,Object>() ;
		simParams.put(temperatureSensorRef, this) ;
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
		
		//---------------BCM---------------
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
	
//------------------------------------------------------------------------
//-------------------------SIMULATION METHODS-----------------------------
//------------------------------------------------------------------------

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
	
	

//------------------------------------------------------------------------
//----------------------------FINALISE------------------------------------
//------------------------------------------------------------------------
	@Override
	public void finalise() throws Exception {
		temperatureSensorHeatingOutboundPort.doDisconnection();
		temperatureSensorOutboundPort.doDisconnection();
		super.finalise();
	}

//------------------------------------------------------------------------
//----------------------------SHUTDOWN------------------------------------
//------------------------------------------------------------------------

	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			temperatureSensorInboundPort.unpublishPort();
			temperatureSensorHeatingOutboundPort.unpublishPort();
			temperatureSensorOutboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
