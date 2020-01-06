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
import simulation.models.battery.BatteryModel;

@RequiredInterfaces(required = {BatteryI.class, BatteryChargerI.class})
@OfferedInterfaces(offered = {BatteryI.class, BatteryChargerI.class})
public class Battery extends		AbstractCyPhyComponent implements	EmbeddingComponentStateAccessI{

	protected final String				uri ;
	protected final String				batteryInboundPortURI ;	
	protected final String				batteryOutboundPortURI ;
	protected BatteryOutboundPort		batteryOutboundPort ;
	protected BatteryInboundPort		batteryInboundPort ;
	
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
					   String batteryInboundPortURI) throws Exception{
		super(uri, 1, 1);

		assert uri != null;
		assert batteryOutboundPortURI != null;
		assert batteryInboundPortURI != null;

		this.uri = uri;
		this.batteryInboundPortURI = batteryInboundPortURI;
		this.batteryOutboundPortURI = batteryOutboundPortURI;
		this.prod = 0;

		//-------------------PUBLISH-------------------
		batteryInboundPort = new BatteryInboundPort(batteryInboundPortURI, this) ;
		batteryInboundPort.publishPort() ;
		batteryOutboundPort = new BatteryOutboundPort(batteryOutboundPortURI, this) ;
		batteryOutboundPort.localPublishPort() ;

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
		this.chargePercentage = 100;
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
		simParams.put("componentRef2", this) ;
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
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}


	@Override
	protected Architecture createLocalArchitecture(String architectureURI) throws Exception {
		return BatteryCoupledModel.build() ;
	}
	
	@Override
	public Object getEmbeddingComponentStateValue(String name) throws Exception {
		return this.asp.getModelStateValue(BatteryModel.URI, "mode") + " " + 
			   this.asp.getModelStateValue(BatteryModel.URI, "temperature");
	}
}
