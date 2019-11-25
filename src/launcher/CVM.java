package launcher;


import components.Battery;
import components.Kettle;
import components.HeatSensor;
import components.WindSensor;
import components.Charger;
import components.Heating;
import components.ElectricMeter;
import components.Controller;
import components.WindTurbine;
import connectors.BatteryControllerConnector;
import connectors.KettleElectricMetterConnector;
import connectors.KettleControllerConnector;
import connectors.HeatSensorHeatingConnector;
import connectors.ChargerElectricMeterConnector;
import connectors.ChargerControllerConnector;
import connectors.HeatingElectricMeterConnector;
import connectors.HeatingControllerConnector;
import connectors.ElectricMeterConnector;
import connectors.ElectricMeterControllerConnector;
import connectors.ControllerConnector;
import connectors.WIndTurbineControllerConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

public class CVM extends AbstractCVM {
	
//--------------------------------------------------------------
//-------------------------VARIABLES----------------------------
//--------------------------------------------------------------
	
	//--------------------------------------------------------------
	//-------------------------CONTROLLER---------------------------
	//--------------------------------------------------------------
	public static final String	CONTROLLER_COMPONENT_URI = "my-URI-controller" ;
	
	//--------------------------------------------------------------
	//-------------------------WINDTURBINE-----------------------------
	//--------------------------------------------------------------
	public static final String	WINDTURBINE_COMPONENT_URI = "my-URI-windTurbine" ;
	protected static final String	URIWindTurbineOutboundPortURI = "windTurbineOPort" ;
	protected static final String	URIWindTurbineInboundPortURI = "windTurbineIPort" ;	
	protected static final String	URIControllerWindTurbineOutboundPortURI = "controllerWindTurbineOPort" ;
	protected static final String	URIControllerWindTurbineInboundPortURI = "controllerWindTurbineIPort" ;
	
	//--------------------------------------------------------------
	//-------------------------KETTLE---------------------------
	//--------------------------------------------------------------	
	public static final String	KETTLE_COMPONENT_URI = "my-URI-kettle" ;
	protected static final String	URIKettleOutboundPortURI = "kettleOPort" ;
	protected static final String	URIKettleInboundPortURI = "kettleIPort" ;
	protected static final String	URIControllerKettleOutboundPortURI = "controllerKettleOPort" ;
	protected static final String	URIControllerKettleInboundPortURI = "controllerKettleIPort" ;
	protected static final String	URIKettleElectricMeterOutboundPortURI = "kettleElectricMeterOPort" ;
	protected static final String	URIKettleElectricMeterInboundPortURI = "kettleElectricMeterIPort" ;
	
	//--------------------------------------------------------------
	//-------------------------HEATING----------------------------
	//--------------------------------------------------------------
	public static final String	HEATING_COMPONENT_URI = "my-URI-heating" ;
	protected static final String	URIHeatingOutboundPortURI = "heatingOPort" ;
	protected static final String	URIHeatingInboundPortURI = "heatingIPort" ;
	protected static final String	URIHeatingToCapteurInboundPortURI = "heatingToCapteurIPort" ;
	protected static final String	URIControllerHeatingOutboundPortURI = "controllerHeatingOPort" ;
	protected static final String	URIControllerHeatingInboundPortURI = "controllerHeatingIPort" ;
	protected static final String	URIHeatingElectricMeterOutboundPortURI = "heatingElectricMeterOPort" ;
	protected static final String	URIHeatingElectricMeterInboundPortURI = "heatingElectricMeterIPort" ;
	
	//--------------------------------------------------------------
	//-------------------------ELECTRICMETER-----------------------------
	//--------------------------------------------------------------
	/** URI of the electricMeter component (convenience).						*/
	public static final String	ELECTRICMETER_COMPONENT_URI = "my-URI-electricMeter" ;
	protected static final String	URIElectricMeterOutboundPortURI = "electricMeterOPort" ;
	protected static final String	URIElectricMeterInboundPortURI = "electricMeterIPort" ;	
	protected static final String	URIControllerElectricMeterOutboundPortURI = "controllerElectricMeterOPort" ;
	protected static final String	URIControllerElectricMeterInboundPortURI = "controllerElectricMeterIPort" ;
	protected static final String	URIElectricMeterHeatingOutboundPortURI = "electricMeterHeatingOPort" ;
	protected static final String	URIElectricMeterHeatingInboundPortURI = "electricMeterHeatingIPort" ;
	protected static final String	URIElectricMeterKettleOutboundPortURI = "electricMeterKettleOPort" ;
	protected static final String	URIElectricMeterKettleInboundPortURI = "electricMeterKettleIPort" ;
	protected static final String	URIElectricMeterChargerOutboundPortURI = "electricMeterChargerOPort" ;
	protected static final String	URIElectricMeterChargerInboundPortURI = "electricMeterChargerIPort" ;
	//--------------------------------------------------------------
	//-------------------------CHARGER-----------------------------
	//--------------------------------------------------------------
	public static final String	CHARGER_COMPONENT_URI = "my-URI-charger" ;
	protected static final String	URIChargerOutboundPortURI = "chargerOPort" ;
	protected static final String	URIChargerInboundPortURI = "chargerIPort" ;
	protected static final String	URIControllerChargerOutboundPortURI = "controllerChargerOPort" ;
	protected static final String	URIControllerChargerInboundPortURI = "controllerChargerIPort" ;
	protected static final String	URIChargerElectricMeterOutboundPortURI = "chargerElectricMeterOPort" ;
	protected static final String	URIChargerElectricMeterInboundPortURI = "chargerElectricMeterIPort" ;
	
	//--------------------------------------------------------------
	//-------------------------BATTERY-----------------------------
	//--------------------------------------------------------------
	public static final String	BATTERY_COMPONENT_URI = "my-URI-battery" ;
	protected static final String	URIBatteryOutboundPortURI = "batteryOPort" ;
	protected static final String	URIBatteryInboundPortURI = "batteryIPort" ;	
	protected static final String	URIControllerBatteryOutboundPortURI = "controllerBatteryOPort" ;
	protected static final String	URIControllerBatteryInboundPortURI = "controllerBatteryIPort" ;
	
	//--------------------------------------------------------------
	//-------------------------CAPTEUR VENT------------------------
	//--------------------------------------------------------------
	public static final String	WIND_SENSOR_COMPONENT_URI = "my-URI-wind-sensor" ;
	protected static final String	URIControllerWindSensorOutboundPortURI = "windSensorOPort" ;
	protected static final String	URIWindSensorInboundPortURI = "windSensorIPort" ;
	
	//--------------------------------------------------------------
	//-------------------------CAPTEUR CHALEUR----------------------
	//--------------------------------------------------------------
	public static final String	HEAT_SENSOR_COMPONENT_URI = "my-URI-heat-sensor" ;
	protected static final String	URIHeatSensorToHeatingOutboundPortURI = "heatSensorToHeatingOPort" ;
	protected static final String	URIControllerHeatSensorOutboundPortURI = "heatSensorOPort" ;
	protected static final String	URIHeatSensorInboundPortURI = "heatSensorIPort" ;
	
	
	
	//--------------------------------------------------------------
	//-------------------------URI COMPONENTS-----------------------
	//--------------------------------------------------------------
	protected String uriWindTurbineURI ;
	protected String uriKettleURI ;
	protected String uriHeatingURI ;
	protected String uriElectricMeterURI ;
	protected String uriControllerURI ;
	protected String uriWindSensorURI ;
	protected String uriHeatSensorURI ;
	protected String uriChargerURI ;
	protected String uriBatteryURI ;

//--------------------------------------------------------------
//-------------------------CONSTRUCTOR--------------------------
//--------------------------------------------------------------
	protected CVM() throws Exception{
		super();
	}


//--------------------------------------------------------------
//-------------------------DEPLOY-------------------------------
//--------------------------------------------------------------	
	@Override
	public void	deploy() throws Exception{
		assert	!this.deploymentDone() ;

		//--------------------------------------------------------------
		//-------------------------WINDTURBINE-----------------------------
		//--------------------------------------------------------------
		this.uriWindTurbineURI =
				AbstractComponent.createComponent(
						WindTurbine.class.getCanonicalName(),
						new Object[]{WINDTURBINE_COMPONENT_URI,
								URIWindTurbineOutboundPortURI,
								URIWindTurbineInboundPortURI}) ;
		assert	this.isDeployedComponent(this.uriWindTurbineURI) ;
		this.toggleTracing(this.uriWindTurbineURI) ;
		this.toggleLogging(this.uriWindTurbineURI) ;

		//--------------------------------------------------------------
		//-------------------------KETTLE---------------------------
		//--------------------------------------------------------------
		this.uriKettleURI =
				AbstractComponent.createComponent(
						Kettle.class.getCanonicalName(),
						new Object[]{KETTLE_COMPONENT_URI,
								URIKettleOutboundPortURI,
								URIKettleInboundPortURI,
								URIElectricMeterKettleOutboundPortURI,
								URIElectricMeterKettleInboundPortURI}) ;

		assert	this.isDeployedComponent(this.uriKettleURI) ;
		this.toggleTracing(this.uriKettleURI) ;
		this.toggleLogging(this.uriKettleURI) ;

		//--------------------------------------------------------------
		//-------------------------HEATING----------------------------
		//--------------------------------------------------------------
		this.uriHeatingURI =
				AbstractComponent.createComponent(
						Heating.class.getCanonicalName(),
						new Object[]{HEATING_COMPONENT_URI,
								URIHeatingOutboundPortURI,
								URIHeatingInboundPortURI,
								URIHeatingToCapteurInboundPortURI,
								URIElectricMeterHeatingOutboundPortURI,
								URIElectricMeterHeatingInboundPortURI}) ;

		assert	this.isDeployedComponent(this.uriHeatingURI) ;
		this.toggleTracing(this.uriHeatingURI) ;
		this.toggleLogging(this.uriHeatingURI) ;
		
		//--------------------------------------------------------------
		//-------------------------ELECTRICMETER-----------------------------
		//--------------------------------------------------------------
		this.uriElectricMeterURI =
				AbstractComponent.createComponent(
						ElectricMeter.class.getCanonicalName(),
						new Object[]{ELECTRICMETER_COMPONENT_URI,
								URIElectricMeterOutboundPortURI,
								URIElectricMeterInboundPortURI,
								URIHeatingElectricMeterOutboundPortURI,
								URIHeatingElectricMeterInboundPortURI,
								URIKettleElectricMeterOutboundPortURI,
								URIKettleElectricMeterInboundPortURI,
								URIChargerElectricMeterOutboundPortURI,
								URIChargerElectricMeterInboundPortURI}) ;

		assert	this.isDeployedComponent(this.uriElectricMeterURI) ;
		this.toggleTracing(this.uriElectricMeterURI) ;
		this.toggleLogging(this.uriElectricMeterURI) ;

		//--------------------------------------------------------------
		//------------------------- WIND SENSOR ------------------------
		//--------------------------------------------------------------
		this.uriWindSensorURI =
				AbstractComponent.createComponent(
						WindSensor.class.getCanonicalName(),
						new Object[]{WIND_SENSOR_COMPONENT_URI,
								URIWindSensorInboundPortURI}) ;
		assert	this.isDeployedComponent(this.uriWindSensorURI) ;
		this.toggleTracing(this.uriWindSensorURI) ;
		this.toggleLogging(this.uriWindSensorURI) ;
		
		//--------------------------------------------------------------
		//------------------------- HEAT SENSOR ------------------------
		//--------------------------------------------------------------
		this.uriHeatSensorURI =
				AbstractComponent.createComponent(
						HeatSensor.class.getCanonicalName(),
						new Object[]{HEAT_SENSOR_COMPONENT_URI,
								URIHeatSensorInboundPortURI,
								URIHeatSensorToHeatingOutboundPortURI}) ;
		assert	this.isDeployedComponent(this.uriHeatSensorURI) ;
		this.toggleTracing(this.uriHeatSensorURI) ;
		this.toggleLogging(this.uriHeatSensorURI) ;
		
		//--------------------------------------------------------------
		//-------------------------CHARGER-----------------------------
		//--------------------------------------------------------------
		this.uriChargerURI =
				AbstractComponent.createComponent(
						Charger.class.getCanonicalName(),
						new Object[]{CHARGER_COMPONENT_URI,
								URIChargerOutboundPortURI,
								URIChargerInboundPortURI,
								URIElectricMeterChargerOutboundPortURI,
								URIElectricMeterChargerInboundPortURI});
		assert	this.isDeployedComponent(this.uriChargerURI) ;
		this.toggleTracing(this.uriChargerURI) ;
		this.toggleLogging(this.uriChargerURI) ;
				
				
		//--------------------------------------------------------------
		//-------------------------BATTERY-----------------------------
		//--------------------------------------------------------------
		this.uriBatteryURI =
				AbstractComponent.createComponent(
						Battery.class.getCanonicalName(),
						new Object[]{BATTERY_COMPONENT_URI,
								URIBatteryOutboundPortURI,
								URIBatteryInboundPortURI}) ;
		assert	this.isDeployedComponent(this.uriBatteryURI) ;
		this.toggleTracing(this.uriBatteryURI) ;
		this.toggleLogging(this.uriBatteryURI) ;

		//--------------------------------------------------------------
		//-------------------------CONTROLLER---------------------------
		//--------------------------------------------------------------
		this.uriControllerURI =
				AbstractComponent.createComponent(
						Controller.class.getCanonicalName(),
						new Object[]{CONTROLLER_COMPONENT_URI,
								URIControllerWindTurbineOutboundPortURI,
								URIControllerWindTurbineInboundPortURI,
								URIControllerKettleOutboundPortURI,
								URIControllerKettleInboundPortURI,
								URIControllerHeatingOutboundPortURI,
								URIControllerHeatingInboundPortURI,
								URIControllerElectricMeterOutboundPortURI,
								URIControllerElectricMeterInboundPortURI,
								URIControllerChargerOutboundPortURI,
								URIControllerChargerInboundPortURI,
								URIControllerBatteryOutboundPortURI,
								URIControllerBatteryInboundPortURI,
								URIControllerWindSensorOutboundPortURI,
								URIControllerHeatSensorOutboundPortURI}) ;
		assert	this.isDeployedComponent(this.uriControllerURI) ;
		this.toggleTracing(this.uriControllerURI) ;
		this.toggleLogging(this.uriControllerURI) ;


//--------------------------------------------------------------
//-------------------------CONNECTION PHASE---------------------
//--------------------------------------------------------------

		//KETTLE <=> CONTROLLER
		this.doPortConnection(
				this.uriKettleURI,
				URIKettleOutboundPortURI,
				URIControllerKettleInboundPortURI,
				KettleControllerConnector.class.getCanonicalName()) ;
		this.doPortConnection(
				this.uriControllerURI,
				URIControllerKettleOutboundPortURI,
				URIKettleInboundPortURI,
				ControllerConnector.class.getCanonicalName()) ;	

		
		//HEATING <=> CONTROLLER
		this.doPortConnection(
				this.uriHeatingURI,
				URIHeatingOutboundPortURI,
				URIControllerHeatingInboundPortURI,
				HeatingControllerConnector.class.getCanonicalName()) ;
		this.doPortConnection(
				this.uriControllerURI,
				URIControllerHeatingOutboundPortURI,
				URIHeatingInboundPortURI,
				ControllerConnector.class.getCanonicalName()) ;	
		
		//HEATING <=> ELECTRICMETER
		this.doPortConnection(
				this.uriHeatingURI,
				URIElectricMeterHeatingOutboundPortURI,
				URIHeatingElectricMeterInboundPortURI,
				HeatingElectricMeterConnector.class.getCanonicalName()) ;
		this.doPortConnection(
				this.uriElectricMeterURI,
				URIHeatingElectricMeterOutboundPortURI,
				URIElectricMeterHeatingInboundPortURI,
				ElectricMeterConnector.class.getCanonicalName()) ;	
		
		//KETTLE <=> ELECTRICMETER
		this.doPortConnection(
				this.uriKettleURI,
				URIElectricMeterKettleOutboundPortURI,
				URIKettleElectricMeterInboundPortURI,
				KettleElectricMetterConnector.class.getCanonicalName()) ;
		this.doPortConnection(
				this.uriElectricMeterURI,
				URIKettleElectricMeterOutboundPortURI,
				URIElectricMeterKettleInboundPortURI,
				ElectricMeterConnector.class.getCanonicalName()) ;	
			
		//CHARGER <=> CONTROLLER
		this.doPortConnection(
				this.uriChargerURI,
				URIChargerOutboundPortURI,
				URIControllerChargerInboundPortURI,
				ChargerControllerConnector.class.getCanonicalName()) ;
		this.doPortConnection(
				this.uriControllerURI,
				URIControllerChargerOutboundPortURI,
				URIChargerInboundPortURI,
				ControllerConnector.class.getCanonicalName()) ;	
				
		//CHARGER <=> ELECTRICMETER
		this.doPortConnection(
				this.uriChargerURI,
				URIElectricMeterChargerOutboundPortURI,
				URIChargerElectricMeterInboundPortURI,
				ChargerElectricMeterConnector.class.getCanonicalName()) ;
		this.doPortConnection(
				this.uriElectricMeterURI,
				URIChargerElectricMeterOutboundPortURI,
				URIElectricMeterChargerInboundPortURI,
				ElectricMeterConnector.class.getCanonicalName()) ;	

		
		//ELECTRICMETER <=> CONTROLLER
		this.doPortConnection(
				this.uriElectricMeterURI,
				URIElectricMeterOutboundPortURI,
				URIControllerElectricMeterInboundPortURI,
				ElectricMeterControllerConnector.class.getCanonicalName()) ;
		this.doPortConnection(
				this.uriControllerURI,
				URIControllerElectricMeterOutboundPortURI,
				URIElectricMeterInboundPortURI,
				ControllerConnector.class.getCanonicalName()) ;	
				
		
		//WINDTURBINE <=> CONTROLLER
		this.doPortConnection(
				this.uriWindTurbineURI,
				URIWindTurbineOutboundPortURI,
				URIControllerWindTurbineInboundPortURI,
				WIndTurbineControllerConnector.class.getCanonicalName()) ;
		this.doPortConnection(
				this.uriControllerURI,
				URIControllerWindTurbineOutboundPortURI,
				URIWindTurbineInboundPortURI,
				ControllerConnector.class.getCanonicalName()) ;	

		
		//WIND SENSOR <=> CONTROLLER
		this.doPortConnection(
				this.uriControllerURI,
				URIControllerWindSensorOutboundPortURI,
				URIWindSensorInboundPortURI,
				ControllerConnector.class.getCanonicalName()) ;
		this.doPortConnection(
				this.uriControllerURI,
				URIControllerHeatSensorOutboundPortURI,
				URIHeatSensorInboundPortURI,
				ControllerConnector.class.getCanonicalName()) ;
		
		
		
				
		
		//BATTERY <=> CONTROLLER
		this.doPortConnection(
				this.uriBatteryURI,
				URIBatteryOutboundPortURI,
				URIControllerBatteryInboundPortURI,
				BatteryControllerConnector.class.getCanonicalName()) ;
		this.doPortConnection(
				this.uriControllerURI,
				URIControllerBatteryOutboundPortURI,
				URIBatteryInboundPortURI,
				ControllerConnector.class.getCanonicalName()) ;	
		
		//HEAT SENSOR => HEATING
		this.doPortConnection(
				this.uriHeatSensorURI,
				URIHeatSensorToHeatingOutboundPortURI,
				URIHeatingToCapteurInboundPortURI,
				HeatSensorHeatingConnector.class.getCanonicalName()) ;
 		

//--------------------------------------------------------------
//-------------------------DEPLOYMENT PHASE---------------------
//--------------------------------------------------------------
		super.deploy();
		assert this.deploymentDone() ;
	}
	
//--------------------------------------------------------------
//-------------------------FINALISE-----------------------------
//--------------------------------------------------------------
	@Override
	public void	finalise() throws Exception{
		super.finalise();
	}

//--------------------------------------------------------------
//-------------------------SHUTDOWN-----------------------------
//--------------------------------------------------------------
	@Override
	public void	shutdown() throws Exception{
		assert this.allFinalised() ;
		super.shutdown();
	}

//--------------------------------------------------------------
//-------------------------MAIN---------------------------------
//--------------------------------------------------------------	
	public static void main(String[] args) {
		try {
			CVM a = new CVM();
			a.startStandardLifeCycle(5000L);
			Thread.sleep(5000L);
			System.exit(0);
		} catch (Exception e) {throw new RuntimeException(e);}
	}

//--------------------------------------------------------------
//-------------------------DO_PORT_CONNECTION-------------------
//--------------------------------------------------------------
	@Override
	public void	doPortConnection(
			String componentURI,
			String outboundPortURI,
			String inboundPortURI,
			String connectorClassname) throws Exception{
		assert componentURI != null && outboundPortURI != null &&
				inboundPortURI != null && connectorClassname != null ;
		assert this.isDeployedComponent(componentURI) ;
		this.uri2component.get(componentURI)
						  .doPortConnection(outboundPortURI, inboundPortURI, connectorClassname);
	}
}
