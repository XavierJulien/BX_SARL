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
import fr.sorbonne_u.devs_simulation.simulators.SimulationEngine;
import interfaces.battery.BatteryChargerI;
import interfaces.battery.BatteryI;
import ports.battery.BatteryInboundPort;
import ports.battery.BatteryOutboundPort;
import simulation.components.battery.BatterySimulatorPlugin;
import simulation.models.battery.BatteryCoupledModel;

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
	protected boolean 					isOn=false;

	
//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected Battery(String uri,
					   String batteryOutboundPortURI,
					   String batteryInboundPortURI,
					   String batteryChargerInboundPortURI) throws Exception{
		super(uri, 2, 2);

		assert uri != null;
		assert batteryOutboundPortURI != null;
		assert batteryInboundPortURI != null;
		assert batteryChargerInboundPortURI != null;

		this.uri = uri;
		this.batteryInboundPortURI = batteryInboundPortURI;
		this.batteryOutboundPortURI = batteryOutboundPortURI;
		this.batteryChargerInboundPortURI = batteryChargerInboundPortURI;
		this.prod = 0;

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
		
		this.maxCharge = 500;
		this.chargePercentage = 0;
		this.prod = 10;
		
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
		this.logMessage("Sending energy....") ;
		this.batteryOutboundPort.sendEnergy(prod) ;
	}
	
	public void receivePower(double power) {
		this.chargePercentage = chargePercentage + power;
		this.logMessage("receiving "+power+"kw from charger");
	}

	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Battery component.") ;
	}
	
	
	//------------------------------------------------------------------------
	//-----------------------------EXECUTE------------------------------------
	//------------------------------------------------------------------------
	
	
	protected void		initialise() throws Exception
	{
		// The coupled model has been made able to create the simulation
		// architecture description.
		Architecture localArchitecture = this.createLocalArchitecture(null) ;
		// Create the appropriate DEVS simulation plug-in.
		this.asp = new BatterySimulatorPlugin() ;
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
		
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							((Battery)this.getTaskOwner()).startBattery();
							while(true) {
								if(isOn) {
									((Battery)this.getTaskOwner()).sendEnergy();
									((Battery)this.getTaskOwner()).sendChargePercentage();
								}
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
		return BatteryCoupledModel.build() ;
	}

	@Override
	public Object getEmbeddingComponentStateValue(String name) throws Exception{
		if(name.equals("isOn")) {
			return (Boolean)isOn;
		}else if(name.equals("charge")) {
			return chargePercentage;
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
