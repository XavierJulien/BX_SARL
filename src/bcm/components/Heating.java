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

	
//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected Heating(String uri,
						String heatingOutboundPortURI,
						String heatingInboundPortURI,
						String heatingToHeatSensorInboundPortURI,
						String heatingElectricMeterOutboundPortURI,
						String heatingElectricMeterInboundPortURI) throws Exception{
		super(uri, 2, 2);

		assert uri != null;
		assert heatingOutboundPortURI != null;
		assert heatingInboundPortURI != null;
		assert heatingToHeatSensorInboundPortURI != null;

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
		maxPower = 10;
		powerPercentage = 0;
		maxConsumption = 10;
		consumption = 0;
		
		//----------------Modelisation-------------
		
		this.initialise();
	}


//------------------------------------------------------------------------
//----------------------------SERVICES------------------------------------
//------------------------------------------------------------------------
	
	public void startHeating() throws Exception{
		this.logMessage("The heating is starting his job....") ;
		powerPercentage = 10;
		consumption = (int) ((powerPercentage/100.0)*maxConsumption);
		isOn = true;
	}

	public void stopHeating() throws Exception{
		this.logMessage("The heating is stopping his job....") ;
		powerPercentage = 0;
		consumption = 0;
		isOn =false;
	}

	public void sendConsumption() throws Exception {
		this.logMessage("Sending comsumption....") ;
		if(isOn) {
			this.heatingElectricMeterOutboundPort.sendConsumption((powerPercentage/100.0)*maxConsumption) ;
		}else {
			this.heatingElectricMeterOutboundPort.sendConsumption(0);
		}
		
	}
	
	public void putExtraPowerInHeating(double power) throws Exception {
		powerPercentage = (int) Math.min(100, powerPercentage+power);
		this.logMessage("The heating is now running at "+powerPercentage+"% of his maximum power") ;
	}
	
	
	public void slowHeating(double power) throws Exception {
		powerPercentage = (int) Math.max(0, powerPercentage-power);
		this.logMessage("The heating is now running at "+powerPercentage+"% of his maximum power") ;
	}
	
	public double sendHeating() throws Exception {
		if(isOn) {
			this.logMessage("Sending Heat....") ;
			return maxPower*(powerPercentage/100.0);
		}else {
			return 0;
		}
	}

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
	
	@Override
	public void execute() throws Exception{
		super.execute();
		
		
		//---------------SIMULATION---------------
		SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 500L ;
		HashMap<String,Object> simParams = new HashMap<String,Object>() ;
		simParams.put("heatingRef", this) ;
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
