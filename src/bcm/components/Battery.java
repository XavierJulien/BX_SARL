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
	
//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected Battery(String uri,
					   String batteryOutboundPortURI,
					   String batteryInboundPortURI,
					   String batteryChargerInboundPortURI,
					   double maxCharge,
					   double production ) throws Exception{
		super(uri, 2, 2);

		assert uri != null;
		assert batteryOutboundPortURI != null;
		assert batteryInboundPortURI != null;
		assert batteryChargerInboundPortURI != null;

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
		
		//----------------Modelisation-------------
		
		this.initialise();
		
		
		
	}
	
//------------------------------------------------------------------------
//----------------------------SERVICES------------------------------------
//------------------------------------------------------------------------

	public void startBattery() throws Exception{
		this.logMessage("The battery is starting his job....") ;
		isOn = true;
	}

	public void stopBattery() throws Exception{
		this.logMessage("The battery is stopping his job....") ;
		isOn =false;
	}

	public void sendChargePercentage() throws Exception {
		this.logMessage("Sending charge percentage....") ;
		this.batteryOutboundPort.sendChargePercentage(chargePercentage);
	}
	
	public void sendEnergy() throws Exception {
		isCharging = false;
		this.logMessage("Sending energy....") ;
		currentCharge -= prod*2;
		chargePercentage = Math.min(100,currentCharge*100.0/ maxCharge);
		this.batteryOutboundPort.sendEnergy(prod) ;
	}
	
	public void receivePower(double power) {
		isCharging = true;
		this.currentCharge = currentCharge + power;
		this.chargePercentage = currentCharge*100.0/maxCharge;
		this.logMessage("receiving "+power+"kw from charger");
	}

	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Battery component.") ;
	}
	
	
//------------------------------------------------------------------------
//----------------------INITIALISE & EXECUTE------------------------------
//------------------------------------------------------------------------
	
	
	protected void		initialise() throws Exception {
		Architecture localArchitecture = this.createLocalArchitecture(null) ;
		this.asp = new BatterySimulatorPlugin() ;
		this.asp.setPluginURI(localArchitecture.getRootModelURI()) ;
		this.asp.setSimulationArchitecture(localArchitecture) ;
		this.installPlugin(this.asp) ;
		this.toggleLogging() ;
	}
	
	
	
	
	@Override
	public void execute() throws Exception {
		super.execute();
		
		
		//---------------SIMULATION---------------
		SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 500L ;
		HashMap<String,Object> simParams = new HashMap<String,Object>() ;
		simParams.put("batteryRef", this) ;
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
