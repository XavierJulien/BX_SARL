package bcm.components;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import bcm.interfaces.heating.HeatingI;
import bcm.interfaces.heating.HeatingTemperatureSensorI;
import bcm.ports.heating.HeatingInboundPort;
import bcm.ports.heating.HeatingOutboundPort;
import bcm.ports.heating.HeatingTemperatureSensorInboundPort;
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
import simulation.models.heating.HeatingCoupledModel;
import simulation.simulatorplugins.HeatingSimulatorPlugin;


/**
 * This class represents the heating of the house, it can be turned on/off, it has a modular power (and so a modular consumption), and inform the temperature sensor of the produced heat
 * @author Julien Xavier et Alexis Belanger
 *
 */
@RequiredInterfaces(required = {HeatingI.class, HeatingTemperatureSensorI.class})
@OfferedInterfaces(offered = {HeatingI.class, HeatingTemperatureSensorI.class})
public class Heating 
extends		AbstractCyPhyComponent
implements	EmbeddingComponentStateAccessI{
	
	protected final String						uri ;
	protected final String						heatingInboundPortURI ;
	protected final String						heatingOutboundPortURI ;
	protected final String						heatingElectricMeterOutboundPortURI ;
	protected final String						heatingElectricMeterInboundPortURI ;
	protected final String						heatingToHeatSensorInboundPortURI ;
	
	
	protected HeatingOutboundPort				heatingOutboundPort ;
	protected HeatingInboundPort				heatingInboundPort ;
	protected HeatingOutboundPort				heatingElectricMeterOutboundPort ;
	protected HeatingInboundPort				heatingElectricMeterInboundPort ;
	protected HeatingTemperatureSensorInboundPort		heatingToHeatSensorInboundPort ;
	
	
	protected boolean 							isOn;
	protected int 								maxPower;
	protected int 								powerPercentage;
	protected int								maxConsumption;
	protected int 								consumption;
	protected final String						heatingRef;

	
//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	/**
	 * the heating component constructor
	 * @param uri the heating uri
	 * @param heatingOutboundPortURI Port for the heating -&gt controller connection
	 * @param heatingInboundPortURI Port for the heating &lt- controller connection
	 * @param heatingToHeatSensorInboundPortURI Port for the heating &lt- heat sensor connection
	 * @param heatingElectricMeterOutboundPortURI Port for the heating -&gt electricmeter connection
	 * @param heatingElectricMeterInboundPortURI Port for the heating &lt- electricMeter connection
	 * @param maxPower the maximum heat the heating can produce
	 * @param consumption the heating consumption
	 * @param heatingRef the ref of the heating model
	 * @throws Exception
	 * 
	 * <pre>
	 * pre uri != null
     * pre heatingOutboundPortURI != null
     * pre heatingInboundPortURI != null
     * pre heatingToHeatSensorInboundPortURI != null
     * pre heatingElectricMeterInboundPortURI != null
     * pre heatingElectricMeterOutboundPortURI != null
     * pre maxPower > 0;
     * pre consumption > 0;
     * pre heatingRef != null
	 * </pre>
	 */
	protected Heating(String uri,
						String heatingOutboundPortURI,
						String heatingInboundPortURI,
						String heatingToHeatSensorInboundPortURI,
						String heatingElectricMeterOutboundPortURI,
						String heatingElectricMeterInboundPortURI,
						int maxPower,
						int consumption,
						String heatingRef) throws Exception{
		super(uri, 2, 2);

		assert uri != null;
		assert heatingOutboundPortURI != null;
		assert heatingInboundPortURI != null;
		assert heatingToHeatSensorInboundPortURI != null;
		assert heatingElectricMeterInboundPortURI != null;
		assert heatingElectricMeterOutboundPortURI != null;
		assert maxPower > 0;
		assert consumption > 0;
		assert heatingRef != null;

		this.uri = uri;
		this.heatingInboundPortURI = heatingInboundPortURI;
		this.heatingOutboundPortURI = heatingOutboundPortURI;
		this.heatingElectricMeterOutboundPortURI = heatingElectricMeterOutboundPortURI;
		this.heatingElectricMeterInboundPortURI = heatingElectricMeterInboundPortURI;
		this.heatingToHeatSensorInboundPortURI = heatingToHeatSensorInboundPortURI;

		//-------------------PUBLISH-------------------
		heatingToHeatSensorInboundPort = new HeatingTemperatureSensorInboundPort(heatingToHeatSensorInboundPortURI, this) ;
		heatingToHeatSensorInboundPort.publishPort() ;
		heatingInboundPort = new HeatingInboundPort(heatingInboundPortURI, this) ;
		heatingInboundPort.publishPort() ;
		this.heatingOutboundPort = new HeatingOutboundPort(heatingOutboundPortURI, this) ;
		this.heatingOutboundPort.localPublishPort() ;
		
		heatingElectricMeterInboundPort = new HeatingInboundPort(heatingElectricMeterInboundPortURI, this) ;
		heatingElectricMeterInboundPort.publishPort() ;
		this.heatingElectricMeterOutboundPort = new HeatingOutboundPort(heatingElectricMeterOutboundPortURI, this) ;
		this.heatingElectricMeterOutboundPort.localPublishPort() ;

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		//-------------------GUI-------------------
		this.tracer.setTitle(uri) ;
		this.tracer.setRelativePosition(3, 2) ;
		
		
		//----------------Variables----------------
		isOn = false;
		this.maxPower = maxPower;
		powerPercentage = 0;
		maxConsumption = consumption;
		consumption = 0;
		this.heatingRef = heatingRef;
		
		//----------------Modelisation-------------
		
		this.initialise();
	}


//------------------------------------------------------------------------
//----------------------------SERVICES------------------------------------
//------------------------------------------------------------------------
	
	
	/**
	 * this method is called by an inbound port to start the heating
	 * @throws Exception
	 */
	public void startHeating() throws Exception{
		this.logMessage("The heating is starting his job....") ;
		powerPercentage = 10;
		consumption = (int) ((powerPercentage/100.0)*maxConsumption);
		isOn = true;
	}

	/**
	 * this method is called by an inbound port to stop the heating
	 * @throws Exception
	 */
	public void stopHeating() throws Exception{
		this.logMessage("The heating is stopping his job....") ;
		powerPercentage = 0;
		consumption = 0;
		isOn =false;
	}

	/**
	 * this method is used to send the heating to the electric meter
	 * @throws Exception
	 */
	public void sendConsumption() throws Exception {
		this.logMessage("Sending comsumption....") ;
		if(isOn) {
			this.heatingElectricMeterOutboundPort.sendConsumption((powerPercentage/100.0)*maxConsumption) ;
		}else {
			this.heatingElectricMeterOutboundPort.sendConsumption(0);
		}
		
	}
	
	/**
	 * this method is called by an inbound port to increase the power of the heating
	 * @param power the increase value
	 * @throws Exception
	 */
	public void putExtraPowerInHeating(double power) throws Exception {
		powerPercentage = (int) Math.min(100, powerPercentage+power);
		this.logMessage("The heating is now running at "+powerPercentage+"% of his maximum power") ;
	}
	
	/**
	 * this method is called by an inbound port to decrease the power of the heating
	 * @param power the decrease value
	 * @throws Exception
	 */
	public void slowHeating(double power) throws Exception {
		powerPercentage = (int) Math.max(0, powerPercentage-power);
		this.logMessage("The heating is now running at "+powerPercentage+"% of his maximum power") ;
	}
	
	
	/**
	 * this method is used to inform the temperature sensor of the heat produced by the heating
	 * @return
	 * @throws Exception
	 */
	public double sendHeating() throws Exception {
		if(isOn) {
			this.logMessage("Sending Heat....") ;
			return maxPower*(powerPercentage/100.0);
		}else {
			return 0;
		}
	}

	/**
	 * start the component
	 */
	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Heating component.") ;
	}
	

//------------------------------------------------------------------------
//----------------------INITIALISE & EXECUTE------------------------------
//------------------------------------------------------------------------
		
	
	protected void initialise() throws Exception {
		Architecture localArchitecture = this.createLocalArchitecture(null) ;
		this.asp = new HeatingSimulatorPlugin() ;
		this.asp.setPluginURI(localArchitecture.getRootModelURI()) ;
		this.asp.setSimulationArchitecture(localArchitecture) ;
		this.installPlugin(this.asp) ;
		this.toggleLogging() ;
	}
	
	/**
	 * execute the component, first the simulation starts then the component behaviour
	 */
	@Override
	public void execute() throws Exception{
		super.execute();
		
		
		//---------------SIMULATION---------------
		SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 500L ;
		HashMap<String,Object> simParams = new HashMap<String,Object>() ;
		simParams.put(heatingRef, this) ;
		this.asp.setSimulationRunParameters(simParams) ;
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
								((Heating)this.getTaskOwner()).sendConsumption() ;
								Thread.sleep(1000);
							}
						} catch (Exception e) {throw new RuntimeException(e) ;}
					}
				},
				1000, TimeUnit.MILLISECONDS);
	}
	
	
//------------------------------------------------------------------------
//-------------------------SIMULATION METHODS-----------------------------
//------------------------------------------------------------------------

	@Override
	protected Architecture createLocalArchitecture(String architectureURI) throws Exception{
		return HeatingCoupledModel.build() ;
	}

	@Override
	public Object getEmbeddingComponentStateValue(String name) throws Exception{
		if(name.equals("isOn")) {
			return (Boolean)isOn;
		}else if(name.equals("power")) {
			return (Integer)powerPercentage;
		}else {
			return null;
		}
	}


//------------------------------------------------------------------------
//----------------------------FINALISE------------------------------------
//------------------------------------------------------------------------
	@Override
	public void finalise() throws Exception {
		heatingOutboundPort.doDisconnection();
		heatingElectricMeterOutboundPort.doDisconnection();
		super.finalise();
	}

//------------------------------------------------------------------------
//----------------------------SHUTDOWN------------------------------------
//------------------------------------------------------------------------
	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			heatingInboundPort.unpublishPort();
			heatingOutboundPort.unpublishPort();
			heatingElectricMeterInboundPort.unpublishPort();
			heatingElectricMeterOutboundPort.unpublishPort();
			heatingToHeatSensorInboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
