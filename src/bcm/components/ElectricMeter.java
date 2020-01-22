package bcm.components;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import bcm.interfaces.electricmeter.ElectricMeterControllerI;
import bcm.interfaces.electricmeter.ElectricMeterI;
import bcm.ports.electricmeter.ElectricMeterInboundPort;
import bcm.ports.electricmeter.ElectricMeterOutboundPort;
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
import simulation.models.electricMeter.ElectricMeterCoupledModel;
import simulation.simulatorplugins.ElectricMeterSimulatorPlugin;

@RequiredInterfaces(required = {ElectricMeterI.class, ElectricMeterControllerI.class})
@OfferedInterfaces(offered = {ElectricMeterI.class, ElectricMeterControllerI.class})
public class ElectricMeter extends AbstractCyPhyComponent implements EmbeddingComponentStateAccessI {
	
	protected final String				uri ;
	protected final String				electricMeterInboundPortURI ;	
	protected final String				electricMeterOutboundPortURI ;
	protected final String				electricMeterHeatingInboundPortURI ;	
	protected final String				electricMeterHeatingOutboundPortURI ;
	protected final String				electricMeterKettleInboundPortURI ;	
	protected final String				electricMeterKettleOutboundPortURI ;
	protected final String				electricMeterChargerInboundPortURI ;	
	protected final String				electricMeterChargerOutboundPortURI ;

	
	protected ElectricMeterOutboundPort		electricMeterOutboundPort ;
	protected ElectricMeterInboundPort		electricMeterInboundPort ;
	protected ElectricMeterOutboundPort		electricMeterHeatingOutboundPort ;
	protected ElectricMeterInboundPort		electricMeterHeatingInboundPort ;
	protected ElectricMeterOutboundPort		electricMeterKettleOutboundPort ;
	protected ElectricMeterInboundPort		electricMeterKettleInboundPort ;
	protected ElectricMeterOutboundPort		electricMeterChargerOutboundPort ;
	protected ElectricMeterInboundPort		electricMeterChargerInboundPort ;
	
	protected boolean 					isOn;
	protected double					consumptionHeating;
	protected double					consumptionCharger;
	protected double					consumptionKettle;
	
	protected ElectricMeterSimulatorPlugin		asp ;


	
//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected ElectricMeter(String uri,
					   String electricMeterOutboundPortURI,
					   String electricMeterInboundPortURI,
					   String electricMeterHeatingOutboundPortURI,
					   String electricMeterHeatingInboundPortURI,
					   String electricMeterKettleOutboundPortURI,
					   String electricMeterKettleInboundPortURI,
					   String electricMeterChargerOutboundPortURI,
					   String electricMeterChargerInboundPortURI) throws Exception{
		super(uri, 2, 1);

		assert uri != null;
		assert electricMeterOutboundPortURI != null;
		assert electricMeterInboundPortURI != null;

		this.uri = uri;
		this.electricMeterInboundPortURI = electricMeterInboundPortURI;
		this.electricMeterOutboundPortURI = electricMeterOutboundPortURI;
		this.electricMeterHeatingOutboundPortURI = electricMeterHeatingOutboundPortURI;
		this.electricMeterHeatingInboundPortURI = electricMeterHeatingInboundPortURI;
		this.electricMeterKettleOutboundPortURI = electricMeterKettleOutboundPortURI;
		this.electricMeterKettleInboundPortURI = electricMeterKettleInboundPortURI;
		this.electricMeterChargerOutboundPortURI = electricMeterChargerOutboundPortURI;
		this.electricMeterChargerInboundPortURI = electricMeterChargerInboundPortURI;

		//-------------------PUBLISH-------------------
		electricMeterInboundPort = new ElectricMeterInboundPort(electricMeterInboundPortURI, this) ;
		electricMeterInboundPort.publishPort() ;
		this.electricMeterOutboundPort = new ElectricMeterOutboundPort(electricMeterOutboundPortURI, this) ;
		this.electricMeterOutboundPort.localPublishPort() ;
		
		electricMeterHeatingInboundPort = new ElectricMeterInboundPort(electricMeterHeatingInboundPortURI, this) ;
		electricMeterHeatingInboundPort.publishPort() ;
		this.electricMeterHeatingOutboundPort = new ElectricMeterOutboundPort(electricMeterHeatingOutboundPortURI, this) ;
		this.electricMeterHeatingOutboundPort.localPublishPort() ;
		
		electricMeterKettleInboundPort = new ElectricMeterInboundPort(electricMeterKettleInboundPortURI, this) ;
		electricMeterKettleInboundPort.publishPort() ;
		this.electricMeterKettleOutboundPort = new ElectricMeterOutboundPort(electricMeterKettleOutboundPortURI, this) ;
		this.electricMeterKettleOutboundPort.localPublishPort() ;
		
		electricMeterChargerInboundPort = new ElectricMeterInboundPort(electricMeterChargerInboundPortURI, this) ;
		electricMeterChargerInboundPort.publishPort() ;
		this.electricMeterChargerOutboundPort = new ElectricMeterOutboundPort(electricMeterChargerOutboundPortURI, this) ;
		this.electricMeterChargerOutboundPort.localPublishPort() ;

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		//-------------------GUI-------------------
		this.tracer.setTitle(uri) ;
		this.tracer.setRelativePosition(3, 3) ;
		
		//----------------Variables----------------

		isOn=false;
		consumptionHeating = 0;
		consumptionCharger = 0;
		consumptionKettle = 0;
		
		
		//----------------Modelisation-------------
		this.initialise();
	}


//------------------------------------------------------------------------
//----------------------------SERVICES------------------------------------
//------------------------------------------------------------------------
	
	public void startElectricMeter() throws Exception{
		this.logMessage("The electric Meter is starting his job....") ;
		isOn = true;
	}

	public void stopElectricMeter() throws Exception{
		this.logMessage("The electric Meter is stopping his job....") ;
		isOn =false;
	}
	
	public void sendAllConsumption() throws Exception {
		this.logMessage("Sending all comsumption....") ;
		this.electricMeterOutboundPort.sendAllConsumption(consumptionKettle+consumptionCharger+consumptionHeating) ;
	}
	
	public void getHeatingConsumption(double consumption) throws Exception {
		this.consumptionHeating = consumption;
		this.logMessage("The electric Meter is informed that the heating consumes "+consumption+" units of energy.") ;
	}
	
	public void getKettleConsumption(double consumption) throws Exception {
		this.consumptionKettle = consumption;
		this.logMessage("The electric Meter is informed that the kettle consumes "+consumption+" units of energy.") ;
	}
	
	public void getChargerConsumption(double consumption) throws Exception {
		this.consumptionCharger = consumption;
		this.logMessage("The electric Meter is informed that the charger consumes "+consumption+" units of energy.") ;
	}
	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Electric Meter component.") ;
	}
	

//------------------------------------------------------------------------
//----------------------INITIALISE & EXECUTE------------------------------
//------------------------------------------------------------------------

	
	protected void initialise() throws Exception {
		Architecture localArchitecture = this.createLocalArchitecture(null) ;
		this.asp = new ElectricMeterSimulatorPlugin() ;
		this.asp.setPluginURI(localArchitecture.getRootModelURI()) ;
		this.asp.setSimulationArchitecture(localArchitecture) ;
		this.installPlugin(this.asp) ;
		this.toggleLogging() ;
	}
	
	
	@Override
	public void execute() throws Exception{
		super.execute();
		
		//---------------SIMULATION---------------
		SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 1000L ;
		HashMap<String,Object> simParams = new HashMap<String,Object>() ;
		simParams.put("electricMeterRef", this) ;
		this.asp.setSimulationRunParameters(simParams) ;
		// Start the simulation.
		this.runTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							Thread.sleep(1000);
							asp.doStandAloneSimulation(0.0, 50000.0) ;
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
								((ElectricMeter)this.getTaskOwner()).sendAllConsumption();
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
		return ElectricMeterCoupledModel.build() ;
	}

	
	
	@Override
	public Object getEmbeddingComponentStateValue(String name) throws Exception {
		if(name.equals("total")) {
			return new Double(consumptionCharger+consumptionHeating+consumptionKettle);
		}else {
			return null;
		}
		
	}

	
//------------------------------------------------------------------------
//----------------------------FINALISE------------------------------------
//------------------------------------------------------------------------
	@Override
	public void finalise() throws Exception {
		electricMeterOutboundPort.doDisconnection();
		electricMeterHeatingOutboundPort.doDisconnection();
		electricMeterKettleOutboundPort.doDisconnection();
		electricMeterChargerOutboundPort.doDisconnection();
		super.finalise();
	}

//------------------------------------------------------------------------
//----------------------------SHUTDOWN------------------------------------
//------------------------------------------------------------------------
	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			electricMeterInboundPort.unpublishPort();
			electricMeterOutboundPort.unpublishPort();
			electricMeterHeatingInboundPort.unpublishPort();
			electricMeterHeatingOutboundPort.unpublishPort();
			electricMeterKettleInboundPort.unpublishPort();
			electricMeterKettleOutboundPort.unpublishPort();
			electricMeterChargerInboundPort.unpublishPort();
			electricMeterChargerOutboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
