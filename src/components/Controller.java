package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.controller.ControllerI;
import ports.controller.ControllerInboundPort;
import ports.controller.ControllerOutboundPort;

@RequiredInterfaces(required = {ControllerI.class})
@OfferedInterfaces(offered = {ControllerI.class})
public class Controller extends AbstractComponent {

	//--------------------------------------------------------------
	//-------------------------URI COMPONENTS-----------------------
	//--------------------------------------------------------------
	protected final String				uri ;
	protected final String				controllerWindTurbineInboundPortURI ;	
	protected final String				controllerWindTurbineOutboundPortURI ;
	protected final String				controllerKettleOutboundPortURI ;
	protected final String				controllerKettleInboundPortURI ;
	protected final String				controllerHeatingOutboundPortURI ;
	protected final String				controllerHeatingInboundPortURI ;
	protected final String				controllerElectricMeterOutboundPortURI ;
	protected final String				controllerElectricMeterInboundPortURI ;
	protected final String				controllerChargerOutboundPortURI ;
	protected final String				controllerChargerInboundPortURI ;
	protected final String				controllerBatteryOutboundPortURI ;
	protected final String				controllerBatteryInboundPortURI ;
	protected final String				controllerWindSensorOutboundPortURI ;
	protected final String				controllerHeatSensorOutboundPortURI ;
	protected final String				controllerHeatSensorInboundPortURI;

	//--------------------------------------------------------------
	//-------------------------INBOUND PORT-------------------------
	//--------------------------------------------------------------
	protected ControllerInboundPort		controllerWindTurbineInboundPort ;
	protected ControllerInboundPort		controllerKettleInboundPort ;
	protected ControllerInboundPort		controllerHeatingInboundPort ;
	protected ControllerInboundPort		controllerElectricMeterInboundPort ;
	protected ControllerInboundPort		controllerChargerInboundPort ;
	protected ControllerInboundPort		controllerBatteryInboundPort ;
	protected ControllerInboundPort		controllerHeatSensorInboundPort;
	//--------------------------------------------------------------
	//-------------------------OUTBOUND PORT------------------------
	//--------------------------------------------------------------
	protected ControllerOutboundPort	controllerWindTurbineOutboundPort ;
	protected ControllerOutboundPort 	controllerKettleOutboundPort;
	protected ControllerOutboundPort 	controllerHeatingOutboundPort;
	protected ControllerOutboundPort 	controllerElectricMeterOutboundPort;
	protected ControllerOutboundPort 	controllerChargerOutboundPort;
	protected ControllerOutboundPort 	controllerBatteryOutboundPort;
	protected ControllerOutboundPort 	controllerWindSensorOutboundPort;
	protected ControllerOutboundPort 	controllerHeatSensorOutboundPort;

	
	protected double windSpeed = 0;
	protected double temperature = 0;
	public boolean isWindTurbineOn = false;
	public double batteryPercentage = 0;
	protected double prod = 0;

//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected Controller(String uri,
						 String controllerWindTurbineOutboundPortURI,
						 String controllerWindTurbineInboundPortURI, 
						 String controllerKettleOutboundPortURI, 
						 String controllerKettleInboundPortURI, 
						 String controllerHeatingOutboundPortURI, 
						 String controllerHeatingInboundPortURI,
						 String controllerElectricMeterOutboundPortURI, 
						 String controllerElectricMeterInboundPortURI,
						 String controllerChargerOutboundPortURI, 
						 String controllerChargerInboundPortURI,
						 String controllerBatteryOutboundPortURI, 
						 String controllerBatteryInboundPortURI,
						 String controllerWindSensorOutboundPortURI, 
						 String controllerHeatSensorOutboundPortURI,
						 String controllerHeatSensorInboundPortURI) throws Exception{
		super(uri, 8, 8);

		assert uri != null;
		//--------------------------------------------------------------
		//-------------------------INBOUND PORT-------------------------
		//--------------------------------------------------------------
		assert controllerWindTurbineInboundPortURI != null;
		assert controllerKettleInboundPortURI != null;
		assert controllerHeatingInboundPortURI != null;
		assert controllerElectricMeterInboundPortURI != null;
		assert controllerChargerInboundPortURI != null;
		assert controllerBatteryInboundPortURI != null;
		assert controllerHeatSensorInboundPortURI != null;

		//--------------------------------------------------------------
		//-------------------------OUTBOUND PORT------------------------
		//--------------------------------------------------------------
		assert controllerWindTurbineOutboundPortURI != null;
		assert controllerHeatingOutboundPortURI != null;
		assert controllerElectricMeterOutboundPortURI != null;
		assert controllerWindSensorOutboundPortURI != null;
		assert controllerHeatSensorOutboundPortURI != null;
		assert controllerKettleOutboundPortURI != null;
		assert controllerChargerOutboundPortURI != null;
		assert controllerBatteryOutboundPortURI != null;
		

		this.uri = uri;
		this.controllerWindTurbineInboundPortURI = controllerWindTurbineInboundPortURI;
		this.controllerWindTurbineOutboundPortURI = controllerWindTurbineOutboundPortURI;
		this.controllerWindSensorOutboundPortURI = controllerWindSensorOutboundPortURI;
		this.controllerHeatSensorOutboundPortURI = controllerHeatSensorOutboundPortURI;
		this.controllerKettleOutboundPortURI = controllerKettleOutboundPortURI;
		this.controllerKettleInboundPortURI = controllerKettleInboundPortURI;
		this.controllerHeatingOutboundPortURI = controllerHeatingOutboundPortURI;
		this.controllerHeatingInboundPortURI = controllerHeatingInboundPortURI;
		this.controllerElectricMeterOutboundPortURI = controllerElectricMeterOutboundPortURI;
		this.controllerElectricMeterInboundPortURI = controllerElectricMeterInboundPortURI;
		this.controllerChargerOutboundPortURI = controllerChargerOutboundPortURI;
		this.controllerChargerInboundPortURI = controllerChargerInboundPortURI;
		this.controllerBatteryOutboundPortURI = controllerChargerOutboundPortURI;
		this.controllerBatteryInboundPortURI = controllerChargerInboundPortURI;
		this.controllerHeatSensorInboundPortURI = controllerHeatSensorInboundPortURI;

		//-------------------PUBLISH INBOUND PORT-------------------
		controllerWindTurbineInboundPort = new ControllerInboundPort(controllerWindTurbineInboundPortURI, this) ;
		controllerWindTurbineInboundPort.publishPort() ;
		controllerKettleInboundPort = new ControllerInboundPort(controllerKettleInboundPortURI, this) ;
		controllerKettleInboundPort.publishPort() ;
		controllerHeatingInboundPort = new ControllerInboundPort(controllerHeatingInboundPortURI, this) ;
		controllerHeatingInboundPort.publishPort() ;
		controllerElectricMeterInboundPort = new ControllerInboundPort(controllerElectricMeterInboundPortURI, this) ;
		controllerElectricMeterInboundPort.publishPort() ;
		controllerChargerInboundPort = new ControllerInboundPort(controllerChargerInboundPortURI, this) ;
		controllerChargerInboundPort.publishPort() ;
		controllerBatteryInboundPort = new ControllerInboundPort(controllerBatteryInboundPortURI, this) ;
		controllerBatteryInboundPort.publishPort() ;
		controllerHeatSensorInboundPort = new ControllerInboundPort(controllerHeatSensorInboundPortURI, this) ;
		controllerHeatSensorInboundPort.publishPort() ;
		
		//-------------------PUBLISH INBOUND PORT-------------------
		this.controllerWindTurbineOutboundPort = new ControllerOutboundPort(controllerWindTurbineOutboundPortURI, this) ;
		this.controllerWindTurbineOutboundPort.localPublishPort() ;
		this.controllerWindSensorOutboundPort = new ControllerOutboundPort(controllerWindSensorOutboundPortURI, this) ;
		this.controllerWindSensorOutboundPort.localPublishPort() ;
		this.controllerHeatSensorOutboundPort = new ControllerOutboundPort(controllerHeatSensorOutboundPortURI, this) ;
		this.controllerHeatSensorOutboundPort.localPublishPort() ;
		this.controllerKettleOutboundPort = new ControllerOutboundPort(controllerKettleOutboundPortURI, this) ;
		this.controllerKettleOutboundPort.localPublishPort() ;
		this.controllerHeatingOutboundPort = new ControllerOutboundPort(controllerHeatingOutboundPortURI, this) ;
		this.controllerHeatingOutboundPort.localPublishPort() ;
		this.controllerElectricMeterOutboundPort = new ControllerOutboundPort(controllerElectricMeterOutboundPortURI, this) ;
		this.controllerElectricMeterOutboundPort.localPublishPort() ;
		this.controllerChargerOutboundPort = new ControllerOutboundPort(controllerChargerOutboundPortURI, this) ;
		this.controllerChargerOutboundPort.localPublishPort() ;		
		this.controllerBatteryOutboundPort = new ControllerOutboundPort(controllerBatteryOutboundPortURI, this) ;
		this.controllerBatteryOutboundPort.localPublishPort() ;
		
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}
		
		//-------------------GUI-------------------
		this.tracer.setTitle("Controller") ;
		this.tracer.setRelativePosition(0, 0) ;
	}


//------------------------------------------------------------------------
//----------------------------SERVICES------------------------------------
//------------------------------------------------------------------------
	
	
	//--------------------------------------------------------------
	//-------------------------EOLIENNE-----------------------------
	//--------------------------------------------------------------
	
	public void getProduction(double production) throws Exception {
		this.prod += production;
		this.logMessage("The controller is getting "+production+" units of energy from the wind turbine") ;
	}

	//--------------------------------------------------------------
	//-------------------------BOUILLOIRE---------------------------
	//--------------------------------------------------------------
	public void startKettle() throws Exception{	
		this.logMessage("Controller "+this.uri+" : tells kettle to start.") ;
		this.controllerKettleOutboundPort.startKettle();
	}
	public void stopKettle() throws Exception{
		this.logMessage("Controller "+this.uri+" : tells kettle to stop.") ;
		this.controllerKettleOutboundPort.stopKettle();
	}

	//--------------------------------------------------------------
	//-------------------------CHARGEUR-----------------------------
	//--------------------------------------------------------------
	public void startCharger() throws Exception{	
		this.logMessage("Controller "+this.uri+" : tells charger to start.") ;
		this.controllerChargerOutboundPort.startCharger();
	}
	public void stopCharger() throws Exception{
		this.logMessage("Controller "+this.uri+" : tells charger to stop.") ;
		this.controllerChargerOutboundPort.stopCharger();
	}	
	
	//--------------------------------------------------------------
	//-------------------------CHAUFFAGE----------------------------
	//--------------------------------------------------------------
	public void startHeating() throws Exception{	
		this.logMessage("Controller "+this.uri+" : tells heating to start.") ;
		this.controllerHeatingOutboundPort.startHeating();
	}
	public void stopHeating() throws Exception{
		this.logMessage("Controller "+this.uri+" : tells heating to stop.") ;
		this.controllerHeatingOutboundPort.stopHeating();
	}
	
	public void putExtraPowerInHeating(int power) throws Exception{
		this.logMessage("Controller puts "+ power+"% more power in the Heating");
		this.controllerHeatingOutboundPort.putExtraPowerInHeating(power);
	}
	
	public void slowHeating(int power) throws Exception{
		this.logMessage("Controller decreases the Heating power by "+power+"%");
		this.controllerHeatingOutboundPort.putExtraPowerInHeating(power);
	}
	
	
	//--------------------------------------------------------------
	//-------------------------COMPTEUR-----------------------------
	//--------------------------------------------------------------
	public void startElectricMeter() throws Exception{	
		this.logMessage("Controller "+this.uri+" : tells electric meter to start.") ;
		this.controllerElectricMeterOutboundPort.startElectricMeter();
	}
	public void stopElectricMeter() throws Exception{
		this.logMessage("Controller "+this.uri+" : tells electric meter to stop.") ;
		this.controllerElectricMeterOutboundPort.stopElectricMeter();
	}
	public void getAllConsumption(double total) throws Exception {
		this.prod -= total;
		this.logMessage("All the consumers consume "+total);
	}
	
	//--------------------------------------------------------------
	//-------------------------BATTERIE-----------------------------
	//--------------------------------------------------------------
	public void startBattery() throws Exception{	
		this.logMessage("Controller "+this.uri+" : tells battery to start.") ;
		this.controllerBatteryOutboundPort.startBattery();
	}
	public void stopBattery() throws Exception{
		this.logMessage("Controller "+this.uri+" : tells battery to stop.") ;
		this.controllerBatteryOutboundPort.stopBattery();
	}
	public void getBatteryChargePercentage(double percentage) throws Exception {
		this.batteryPercentage = percentage;
		this.logMessage("The battery is "+percentage+"% loaded");
	}
	public void getBatteryProduction(double energy) throws Exception {
		this.prod += energy;
		this.logMessage("The controller is getting "+prod+" units of energy from the Battery");
	}

	//--------------------------------------------------------------
	//-------------------------CAPTEURS-----------------------------
	//--------------------------------------------------------------
	public void getWindSpeed(double speed) throws Exception{
		windSpeed = speed;
		this.logMessage("The controller is informed that the wind power is"+speed) ;
	}	
	
	public void getTemperature(double temperature) throws Exception{
		this.temperature = temperature;
	}
	
	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Controller component.") ;	
	}
	
	
	// TODO A COMPLETER AVEC LES NOUVEAUX TRUCS/COMPORTEMENTS
	public void execute() throws Exception {
		super.execute();
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							((Controller)this.getTaskOwner()).startElectricMeter() ;
							boolean isOnHeating = false;
							while(true) {
								((Controller)this.getTaskOwner()).logMessage("The temperature is "+temperature+" degrees");
								if(temperature < 10) {
									if(!isOnHeating) {
										((Controller)this.getTaskOwner()).startHeating();
									}
									isOnHeating=true;
									((Controller)this.getTaskOwner()).putExtraPowerInHeating(10);
								}else {
									if(temperature < 20) {
										if(isOnHeating) {
											((Controller)this.getTaskOwner()).putExtraPowerInHeating(10);
										}
									}else {
										if(isOnHeating) {
											((Controller)this.getTaskOwner()).stopHeating();
											isOnHeating = false;
										}
										
									}
								}
								if(prod<0) {
									//TODO s'occuper de ca
									((Controller)this.getTaskOwner()).logMessage("TOO MUCH CONSUMPTION");
								}
								
								Thread.sleep(1000);
							}
						} catch (Exception e) {throw new RuntimeException(e) ;}
					}
				},
				1000, TimeUnit.MILLISECONDS);
	}


//------------------------------------------------------------------------
//----------------------------FINALISE------------------------------------
//------------------------------------------------------------------------
	@Override
	public void finalise() throws Exception {
		controllerWindTurbineOutboundPort.doDisconnection();
		controllerWindSensorOutboundPort.doDisconnection();
		controllerHeatSensorOutboundPort.doDisconnection();
		controllerKettleOutboundPort.doDisconnection();
		controllerHeatingOutboundPort.doDisconnection();
		controllerElectricMeterOutboundPort.doDisconnection();
		controllerChargerOutboundPort.doDisconnection();
		controllerBatteryOutboundPort.doDisconnection();
		super.finalise();
	}

//------------------------------------------------------------------------
//----------------------------SHUTDOWN------------------------------------
//------------------------------------------------------------------------
	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			controllerWindTurbineInboundPort.unpublishPort();
			controllerWindTurbineOutboundPort.unpublishPort();
			controllerWindSensorOutboundPort.unpublishPort();
			controllerHeatSensorOutboundPort.unpublishPort();
			controllerHeatSensorInboundPort.unpublishPort();
			controllerKettleInboundPort.unpublishPort();
			controllerKettleOutboundPort.unpublishPort();
			controllerHeatingInboundPort.unpublishPort();
			controllerHeatingOutboundPort.unpublishPort();
			controllerElectricMeterInboundPort.unpublishPort();
			controllerElectricMeterOutboundPort.unpublishPort();
			controllerChargerInboundPort.unpublishPort();
			controllerChargerOutboundPort.unpublishPort();
			controllerBatteryInboundPort.unpublishPort();
			controllerBatteryOutboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
