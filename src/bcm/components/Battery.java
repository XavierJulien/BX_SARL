package bcm.components;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import bcm.interfaces.battery.BatteryChargerI;
import bcm.interfaces.battery.BatteryI;
import bcm.ports.battery.BatteryInboundPort;
import bcm.ports.battery.BatteryOutboundPort;
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
import simulation.models.battery.BatteryCoupledModel;
import simulation.simulatorplugins.BatterySimulatorPlugin;


/**
 * This class represents the battery of the house, the battery can be turned on and off by the controller, charged by the charger and provide energy to the house
 * @author Julien Xavier et Alexis Belanger
 *
 */
@RequiredInterfaces(required = {BatteryI.class, BatteryChargerI.class})
@OfferedInterfaces(offered = {BatteryI.class, BatteryChargerI.class})
public class Battery extends		AbstractCyPhyComponent implements	EmbeddingComponentStateAccessI{

	protected final String				uri ;
	protected final String				batteryInboundPortURI ;	
	protected final String				batteryOutboundPortURI ;
	protected final String				batteryChargerInboundPortURI ;
	protected BatteryOutboundPort		batteryOutboundPort ;
	protected BatteryInboundPort		batteryInboundPort ;
	protected BatteryInboundPort 		batteryChargerInboundPort;
	
	protected BatterySimulatorPlugin		asp ;
	
	protected double 					prod;
	protected double 					maxCharge;
	protected double					chargePercentage;
	protected boolean 					isOn;
	protected boolean 					isCharging;
	protected double 					currentCharge;
	protected final String				batteryRef;
	
//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	/**
	 * Constructor of the Battery Component 
	 * @param uri the battery URI
	 * @param batteryOutboundPortURI Port for the battery -&gt controller connection
	 * @param batteryInboundPortURI Port for the battery &lt- controller connection
	 * @param batteryChargerInboundPortURI Port for the battery -&gt charger connection
	 * @param maxCharge The highest quantity of energy the battery can contain
	 * @param production The battery energy production
	 * @param batteryRef The ref of the Battery Model
	 * @throws Exception
	 * <pre>
	 * pre uri != null
	 * pre batteryOutboundPortURI != null
	 * pre batteryInboundPortURI != null
	 * pre batteryChargerInboundPortURI != null
	 * pre maxCharger &gt 0
	 * pre production &gt 0
	 * pre  batteryRef != null
	 * </pre>
	 */
	protected Battery(String uri,
					   String batteryOutboundPortURI,
					   String batteryInboundPortURI,
					   String batteryChargerInboundPortURI,
					   double maxCharge,
					   double production,
					   String batteryRef) throws Exception{
		super(uri, 2, 2);

		assert uri != null;
		assert batteryOutboundPortURI != null;
		assert batteryInboundPortURI != null;
		assert batteryChargerInboundPortURI != null;
		assert maxCharge > 0;
		assert production > 0;
		assert batteryRef != null;

		this.uri = uri;
		this.batteryInboundPortURI = batteryInboundPortURI;
		this.batteryOutboundPortURI = batteryOutboundPortURI;
		this.batteryChargerInboundPortURI = batteryChargerInboundPortURI;

		//-------------------PUBLISH-------------------
		batteryInboundPort = new BatteryInboundPort(batteryInboundPortURI, this) ;
		batteryInboundPort.publishPort() ;
		batteryOutboundPort = new BatteryOutboundPort(batteryOutboundPortURI, this) ;
		batteryOutboundPort.localPublishPort() ;
		batteryChargerInboundPort = new BatteryInboundPort(batteryChargerInboundPortURI, this) ;
		batteryChargerInboundPort.publishPort() ;

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		//-------------------GUI-------------------
		this.tracer.setTitle(uri) ;
		this.tracer.setRelativePosition(0, 3) ;
		
		
		//----------------Variables----------------
		this.maxCharge = maxCharge;
		this.chargePercentage = 100;
		this.currentCharge = maxCharge;
		this.isOn = false;
		this.isCharging = false;
		this.prod = production;
		this.batteryRef = batteryRef;
		
		//----------------Modelisation-------------
		
		this.initialise();
		
	}
	
//------------------------------------------------------------------------
//----------------------------SERVICES------------------------------------
//------------------------------------------------------------------------
	
	/**
	 * Turn the battery on
	 * @throws Exception	<i>catch dans le bloc supérieur</i>
	 */
	public void startBattery() throws Exception{
		this.logMessage("The battery is starting his job....") ;
		isOn = true;
	}

	/**
	 *Turn the battery off 
	 * @throws Exception	<i>catch dans le bloc supérieur</i>
	 */
	public void stopBattery() throws Exception{
		this.logMessage("The battery is stopping his job....") ;
		isOn =false;
	}

	
	/**
	 * Used to inform the controller of the remaining battery charge
	 * @throws Exception	<i>catch dans le bloc supérieur</i>
	 */
	public void sendChargePercentage() throws Exception {
		this.logMessage("Sending charge percentage....") ;
		this.batteryOutboundPort.sendChargePercentage(chargePercentage);
	}
	
	/**
	 * Sends the amount of energy produced by the battery tp the controller
	 * @throws Exception	<i>catch dans le bloc supérieur</i>
	 */
	public void sendEnergy() throws Exception {
		isCharging = false;
		this.logMessage("Sending energy....") ;
		currentCharge -= prod*2;
		chargePercentage = Math.min(100,currentCharge*100.0/ maxCharge);
		this.batteryOutboundPort.sendEnergy(prod) ;
	}
	
	/**
	 * Get energy from the charger
	 * pre : power must be greater or equal to zero
	 * @param power the energy amount received
	 */
	public void receivePower(double power) {
		assert power >= 0;
		isCharging = true;
		this.currentCharge = currentCharge + power;
		this.chargePercentage = currentCharge*100.0/maxCharge;
		this.logMessage("receiving "+power+"kw from charger");
	}

	
	/**
	 * Starts the component
	 */
	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Battery component.") ;
	}
	
	
//------------------------------------------------------------------------
//----------------------INITIALISE & EXECUTE------------------------------
//------------------------------------------------------------------------
	
	/**
	 * Used for the simulation initialization
	 * @throws Exception	<i>catch dans le bloc supérieur</i>
	 */
	protected void		initialise() throws Exception {
		Architecture localArchitecture = this.createLocalArchitecture(null) ;
		this.asp = new BatterySimulatorPlugin() ;
		this.asp.setPluginURI(localArchitecture.getRootModelURI()) ;
		this.asp.setSimulationArchitecture(localArchitecture) ;
		this.installPlugin(this.asp) ;
		this.toggleLogging() ;
	}
	
	
	
	/**
	 * Execute the component : First the simulation is launched, then the component behaviour
	 * 	 * @throws Exception	<i>catch dans le bloc supérieur</i>

	 */
	@Override
	public void execute() throws Exception {
		super.execute();
		
		
		//---------------SIMULATION---------------
		SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 500L ;
		HashMap<String,Object> simParams = new HashMap<String,Object>() ;
		simParams.put(batteryRef, this) ;
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
							((Battery)this.getTaskOwner()).startBattery();
							while(true) {
								if(isOn) {
									((Battery)this.getTaskOwner()).sendEnergy();
									
								}
								((Battery)this.getTaskOwner()).sendChargePercentage();
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
		return BatteryCoupledModel.build() ;
	}

	@Override
	public Object getEmbeddingComponentStateValue(String name) throws Exception{
		if(name.equals("isOn")) {
			return (Boolean)isOn;
		}else if(name.equals("charge")) {
			return currentCharge;
		}else if(name.equals("charging")) {
			return isCharging;
		}else {
			return null;
			
		}
	}

//------------------------------------------------------------------------
//----------------------------FINALISE------------------------------------
//------------------------------------------------------------------------
	@Override
	public void finalise() throws Exception {
		batteryOutboundPort.doDisconnection();
		super.finalise();
	}

//------------------------------------------------------------------------
//----------------------------SHUTDOWN------------------------------------
//------------------------------------------------------------------------
	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			batteryInboundPort.unpublishPort();
			batteryOutboundPort.unpublishPort();
			batteryChargerInboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
