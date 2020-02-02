package bcm.components;

import java.util.concurrent.TimeUnit;

import bcm.interfaces.controller.ControllerI;
import bcm.ports.controller.ControllerInboundPort;
import bcm.ports.controller.ControllerOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;


/**
 * This class represents the controller of the house, this central component decides of what component can/have to start and stop to manage the global consumption
 * @author Julien Xavier et Alexis Belanger
 *
 */
@RequiredInterfaces(required = {ControllerI.class})
@OfferedInterfaces(offered = {ControllerI.class})
public class Controller extends AbstractComponent {

	//--------------------------------------------------------------
	//-------------------------URI COMPONENTS-----------------------
	//--------------------------------------------------------------
	protected final String				uri ;
	protected final String				controllerWindTurbineInboundPortURI ;	
	protected final String				controllerWindTurbineOutboundPortURI ;
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
	protected ControllerInboundPort		controllerHeatingInboundPort ;
	protected ControllerInboundPort		controllerElectricMeterInboundPort ;
	protected ControllerInboundPort		controllerChargerInboundPort ;
	protected ControllerInboundPort		controllerBatteryInboundPort ;
	protected ControllerInboundPort		controllerHeatSensorInboundPort;
	//--------------------------------------------------------------
	//-------------------------OUTBOUND PORT------------------------
	//--------------------------------------------------------------
	protected ControllerOutboundPort	controllerWindTurbineOutboundPort ;
	protected ControllerOutboundPort 	controllerHeatingOutboundPort;
	protected ControllerOutboundPort 	controllerElectricMeterOutboundPort;
	protected ControllerOutboundPort 	controllerChargerOutboundPort;
	protected ControllerOutboundPort 	controllerBatteryOutboundPort;
	protected ControllerOutboundPort 	controllerWindSensorOutboundPort;
	protected ControllerOutboundPort 	controllerHeatSensorOutboundPort;


	protected double windSpeed;
	protected double temperature;
	public boolean isWindTurbineOn;
	public boolean isBatteryOn;
	public double batteryPercentage;
	protected double remainingEnergy;

	protected double[] maxConso = {15.0,25.0,35.0,20.0};

	protected boolean[] turnOn = {false,false};

	//------------------------------------------------------------------------
	//----------------------------CONSTRUCTOR---------------------------------
	//------------------------------------------------------------------------
	/**
	 * the Controller constructor
	 * @param uri the uri of the controller component
	 * @param controllerWindTurbineOutboundPortURI Port for the controller -&gt wind turbine connection
	 * @param controllerWindTurbineInboundPortURI Port for the controller &lt- wind turbine connection
	 * @param controllerHeatingOutboundPortURI Port for the controller -&gt wind turbine connection
	 * @param controllerHeatingInboundPortURI Port for the controller &lt- heating connection
	 * @param controllerElectricMeterOutboundPortURI Port for the controller -&gt electric meter connection
	 * @param controllerElectricMeterInboundPortURI Port for the controller &lt- electric meter connection
	 * @param controllerChargerOutboundPortURI Port for the controller -&gt Charger connection
	 * @param controllerChargerInboundPortURI Port for the controller &lt- charger connection
	 * @param controllerBatteryOutboundPortURI Port for the controller -&gt Battery connection
	 * @param controllerBatteryInboundPortURI Port for the controller &lt- battery connection
	 * @param controllerWindSensorOutboundPortURI Port for the controller -&gt wind sensor connection
	 * @param controllerHeatSensorOutboundPortURI Port for the controller -&gt heat sensor connection
	 * @param controllerHeatSensorInboundPortURI Port for the controller &lt- heat sensor connection
	 * @throws Exception
	 * 
	 * <pre>
	 * pre uri != null
	 * 
	 * pre controllerWindTurbineInboundPortURI != null
	 * pre controllerHeatingInboundPortURI != null
	 * pre controllerElectricMeterInboundPortURI != null
	 * pre controllerChargerInboundPortURI != null
	 * pre controllerBatteryInboundPortURI != null
	 * pre controllerHeatSensorInboundPortURI != null
     * 
     * pre controllerWindTurbineOutboundPortURI != null
	 * pre controllerHeatingOutboundPortURI != null
	 * pre controllerElectricMeterOutboundPortURI != null
	 * pre controllerWindSensorOutboundPortURI != null
	 * pre controllerHeatSensorOutboundPortURI != null
  	 * pre controllerChargerOutboundPortURI != null
 	 * pre controllerBatteryOutboundPortURI != null
 	 * 
	 * </pre>
	 */
	protected Controller(String uri,
			String controllerWindTurbineOutboundPortURI,
			String controllerWindTurbineInboundPortURI,
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
		assert controllerChargerOutboundPortURI != null;
		assert controllerBatteryOutboundPortURI != null;


		this.uri = uri;
		this.controllerWindTurbineInboundPortURI = controllerWindTurbineInboundPortURI;
		this.controllerWindTurbineOutboundPortURI = controllerWindTurbineOutboundPortURI;
		this.controllerWindSensorOutboundPortURI = controllerWindSensorOutboundPortURI;
		this.controllerHeatSensorOutboundPortURI = controllerHeatSensorOutboundPortURI;
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

		//---------------Variables-----------------
		windSpeed = 0;
		temperature = 0;
		isWindTurbineOn = false;
		isBatteryOn = false;
		batteryPercentage = 100;
		remainingEnergy = 0;


		
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

	/**
	 * THis method is called by an inbound port to transmit the wind turbine production to the controller
	 * @param production the energy got from the windTurbine
	 * @throws Exception 
	 */
	public void getProduction(double production) throws Exception {
		this.remainingEnergy += production;
		this.logMessage("The controller is getting "+production+" units of energy from the wind turbine") ;
	}


	//--------------------------------------------------------------
	//-------------------------CHARGEUR-----------------------------
	//--------------------------------------------------------------
	
	/**
	 * This method is used to start the charger
	 * @throws Exception
	 */
	public void startCharger() throws Exception{	
		this.logMessage("Controller "+this.uri+" : tells charger to start.") ;
		this.controllerChargerOutboundPort.startCharger();
	}
	
	/**
	 * This method is used to stop the charger
	 * @throws Exception
	 */
	public void stopCharger() throws Exception{
		this.logMessage("Controller "+this.uri+" : tells charger to stop.") ;
		this.controllerChargerOutboundPort.stopCharger();
	}	

	//--------------------------------------------------------------
	//-------------------------CHAUFFAGE----------------------------
	//--------------------------------------------------------------
	
	/**
	 * This method is used to start the heating
	 * @throws Exception
	 */
	public void startHeating() throws Exception{	
		this.logMessage("Controller "+this.uri+" : tells heating to start.") ;
		this.controllerHeatingOutboundPort.startHeating();
	}
	
	/**
	 * This method is used to stop the heating
	 * @throws Exception
	 */
	public void stopHeating() throws Exception{
		this.logMessage("Controller "+this.uri+" : tells heating to stop.") ;
		this.controllerHeatingOutboundPort.stopHeating();
	}

	/**
	 * THis method is used to increase the heating power
	 * @param power this is the value  of the increasing
	 * @throws Exception
	 */
	public void putExtraPowerInHeating(int power) throws Exception{
		this.logMessage("Controller puts "+ power+"% more power in the Heating");
		this.controllerHeatingOutboundPort.putExtraPowerInHeating(power);
	}

	
	/**
	 * THis method is used to decrease the heating power
	 * @param power this is the value  of the decreasing
	 * @throws Exception
	 */
	public void slowHeating(int power) throws Exception{
		this.logMessage("Controller decreases the Heating power by "+power+"%");
		this.controllerHeatingOutboundPort.putExtraPowerInHeating(power);
	}


	//--------------------------------------------------------------
	//-------------------------COMPTEUR-----------------------------
	//--------------------------------------------------------------
	
	/**
	 * This method is used to start the electric meter (this is used only once at the start of the execution)
	 * @throws Exception
	 */
	public void startElectricMeter() throws Exception{	
		this.logMessage("Controller "+this.uri+" : tells electric meter to start.") ;
		this.controllerElectricMeterOutboundPort.startElectricMeter();
	}
	
	/**
	 * This method is used to stop the electric meter (unused for now, but could be used if we want to simulate the electric meter disjunction)
	 * @throws Exception
	 */
	public void stopElectricMeter() throws Exception{
		this.logMessage("Controller "+this.uri+" : tells electric meter to stop.") ;
		this.controllerElectricMeterOutboundPort.stopElectricMeter();
	}
	
	/**
	 * this method is used to collect the total consumption of the components from the electric meter
	 * @param total this is the total consumption of the kettle, the heating and the charger
	 * @throws Exception
	 */
	public void getAllConsumption(double total) throws Exception {
		this.remainingEnergy -= total;
		this.logMessage("All the consumers consume "+total);
	}

	//--------------------------------------------------------------
	//-------------------------BATTERIE-----------------------------
	//--------------------------------------------------------------
	
	/**
	 * This method is used to start the battery
	 * @throws Exception
	 */
	public void startBattery() throws Exception{	
		this.logMessage("Controller "+this.uri+" : tells battery to start.") ;
		this.controllerBatteryOutboundPort.startBattery();
	}
	
	/**
	 * This method is used to stop the battery
	 * @throws Exception
	 */
	public void stopBattery() throws Exception{
		this.logMessage("Controller "+this.uri+" : tells battery to stop.") ;
		this.controllerBatteryOutboundPort.stopBattery();
	}
	
	/**
	 * this method is called by an inboundport to get the remaining energy percentage of the battery
	 * @param percentage the percentage left
	 * @throws Exception
	 */
	public void getBatteryChargePercentage(double percentage) throws Exception {
		this.batteryPercentage = percentage;
		this.logMessage("The battery is "+percentage+"% loaded");
	}
	
	/**
	 * this method is called by an inboundport to get the battery energy production
	 * @param energy the received energy
	 * @throws Exception
	 */
	public void getBatteryProduction(double energy) throws Exception {
		this.remainingEnergy += energy;
		this.logMessage("The controller is getting "+energy+" units of energy from the Battery");
	}

	//--------------------------------------------------------------
	//-------------------------CAPTEURS-----------------------------
	//--------------------------------------------------------------
	
	/**
	 * This method is called by an inboundport to inform the controller of the windspeed
	 * @param speed the wind speed
	 * @throws Exception
	 */
	public void getWindSpeed(double speed) throws Exception{
		windSpeed = speed;
		this.logMessage("The controller is informed that the wind power is"+speed) ;
	}	

	
	/**
	 * This method is called by an inboundport to inform the controller of the current temperature
	 * @param temperature the current temperature
	 * @throws Exception
	 */
	public void getTemperature(double temperature) throws Exception{
		this.temperature = temperature;
	}

	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Controller component.") ;	
	}


	
	/**
	 * the controller execution, first the simulation then the component behaviour
	 */
	public void execute() throws Exception {
		super.execute();
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							((Controller)this.getTaskOwner()).startElectricMeter() ;
							boolean isOnHeating = false;
							boolean isOnCharging = false;
							while(true) {
								//SET PRIORITY
								//turnOn = priorityChecker();
								((Controller)this.getTaskOwner()).logMessage("The temperature is "+temperature+" degrees");
								if(remainingEnergy > 10) {
									if(temperature < 10) {
										if(!isOnHeating) {
											((Controller)this.getTaskOwner()).startHeating();
										}
										isOnHeating=true;
										((Controller)this.getTaskOwner()).putExtraPowerInHeating(10);
									}else {
										if(temperature < 15) {
											if(isOnHeating) {
												((Controller)this.getTaskOwner()).putExtraPowerInHeating(5);
											}
										}else {
											if(temperature < 20) {
												if(isOnHeating) {
													((Controller)this.getTaskOwner()).slowHeating(5);
												}
											}else {
												if(isOnHeating) {
													((Controller)this.getTaskOwner()).stopHeating();
													isOnHeating = false;
												}
											}


										}
									}
								}else {
									((Controller)this.getTaskOwner()).stopHeating();
									isOnHeating = false;
								}
								if(remainingEnergy<15) {
									if(!isBatteryOn) {
										if(batteryPercentage > 40) {
											((Controller)this.getTaskOwner()).startBattery();
											isBatteryOn = true;
										}

									}else {
										if(batteryPercentage <10) {
											((Controller)this.getTaskOwner()).stopBattery();	
											isBatteryOn = false;
										}
									}
								}else {
									if(isBatteryOn) {
										((Controller)this.getTaskOwner()).stopBattery();	
										isBatteryOn = false;
									}
								}

								if(batteryPercentage < 90 && remainingEnergy > 50 ) {
									if(!isOnCharging) {
										((Controller)this.getTaskOwner()).stopBattery();
										((Controller)this.getTaskOwner()).startCharger();
										isOnCharging=true;
									}

								}else {
									if(batteryPercentage >= 100 || remainingEnergy < 10) {
										if(isOnCharging) {
											((Controller)this.getTaskOwner()).stopCharger();
											isOnCharging = false;
										}
									}
								}



								Thread.sleep(1000);
							}
						} catch (Exception e) {throw new RuntimeException(e) ;}
					}
				},
				1000, TimeUnit.MILLISECONDS);
	}

	/**
	 * 	protected double[] maxConso = {15.0,25.0,35.0,20.0};

	 * @return
	 */
	public boolean[] priorityChecker() {
		boolean[] result = new boolean[2];
		if(remainingEnergy > maxConso[2]) {
			result[0] = result[1] = true;
		}
		if(remainingEnergy > maxConso[1]) {
			result[0] = result[1] = true;
		}
		if(remainingEnergy > maxConso[0]) {
			result[0] = true;
		}
		if(remainingEnergy > maxConso[3]) {
			if(temperature < 5) {
				result[1] = true;
			}else {
				if(batteryPercentage < 30) {
					result[1] = true;
				}else {
					result[1] = true;									
				}
			}
		}
		if(remainingEnergy > maxConso[4]) {
			result[1] = true;
		}
		return result;
	}

	//------------------------------------------------------------------------
	//----------------------------FINALISE------------------------------------
	//------------------------------------------------------------------------
	@Override
	public void finalise() throws Exception {
		controllerWindTurbineOutboundPort.doDisconnection();
		controllerWindSensorOutboundPort.doDisconnection();
		controllerHeatSensorOutboundPort.doDisconnection();
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
