package launcher;

import components.Battery;
import components.Charger;
import components.Controller;
import components.ElectricMeter;
import components.Heating;
import components.Kettle;
import components.TemperatureSensor;
import components.WindSensor;
import components.WindTurbine;
import connectors.battery.BatteryControllerConnector;
import connectors.charger.ChargerControllerConnector;
import connectors.charger.ChargerElectricMeterConnector;
import connectors.controller.ControllerConnector;
import connectors.electricmeter.ElectricMeterConnector;
import connectors.electricmeter.ElectricMeterControllerConnector;
import connectors.heating.HeatingControllerConnector;
import connectors.heating.HeatingElectricMeterConnector;
import connectors.kettle.KettleControllerConnector;
import connectors.kettle.KettleElectricMetterConnector;
import connectors.sensors.TemperatureSensorHeatingConnector;
import connectors.windturbine.WindTurbineControllerConnector;
import fr.sorbonne_u.components.AbstractComponent;

//Copyright Jacques Malenfant, Sorbonne Universite.
//
//Jacques.Malenfant@lip6.fr
//
//This software is a computer program whose purpose is to provide a
//basic component programming model to program with components
//distributed applications in the Java programming language.
//
//This software is governed by the CeCILL-C license under French law and
//abiding by the rules of distribution of free software.  You can use,
//modify and/ or redistribute the software under the terms of the
//CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
//URL "http://www.cecill.info".
//
//As a counterpart to the access to the source code and  rights to copy,
//modify and redistribute granted by the license, users are provided only
//with a limited warranty  and the software's author,  the holder of the
//economic rights,  and the successive licensors  have only  limited
//liability. 
//
//In this respect, the user's attention is drawn to the risks associated
//with loading,  using,  modifying and/or developing or reproducing the
//software by the user in light of its specific status of free software,
//that may mean  that it is complicated to manipulate,  and  that  also
//therefore means  that it is reserved for developers  and  experienced
//professionals having in-depth computer knowledge. Users are therefore
//encouraged to load and test the software's suitability as regards their
//requirements in conditions enabling the security of their systems and/or 
//data to be ensured and,  more generally, to use and operate it in the 
//same conditions as regards security. 
//
//The fact that you are presently reading this means that you have had
//knowledge of the CeCILL-C license and that you accept its terms.

import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;

//-----------------------------------------------------------------------------
/**
 * The class <code>DistributedCVM</code> implements the multi-JVM assembly for
 * the basic client/server example.
 *
 * <p><strong>Description</strong></p>
 * 
 * An URI provider component defined by the class <code>URIProvider</code>
 * offers an URI creation service, which is used by an URI consumer component
 * defined by the class <code>URIConsumer</code>.
 * 
 * The URI provider is deployed within a JVM running an instance of the CVM
 * called <code>provider</code> in the <code>config.xml</code> file. The URI
 * consumer is deployed in the instance called <code>consumer</code>.
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2014-01-22</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class				DistributedCVM
extends		AbstractDistributedCVM
{
	//--------------------------------------------------------------
	//-------------------------VARIABLES----------------------------
	//--------------------------------------------------------------
		
		//--------------------------------------------------------------
		//-------------------------CONTROLLER---------------------------
		//--------------------------------------------------------------
		public static final String	CONTROLLER_COMPONENT_URI = "my-URI-controller" ;
		
		//--------------------------------------------------------------
		//----------------------- WIND TURBINE -------------------------
		//--------------------------------------------------------------
		public static final String	WINDTURBINE_COMPONENT_URI = "my-URI-windTurbine" ;
		protected static final String	URIWindTurbineOutboundPortURI = "windTurbineOPort" ;
		protected static final String	URIWindTurbineInboundPortURI = "windTurbineIPort" ;	
		protected static final String	URIControllerWindTurbineOutboundPortURI = "controllerWindTurbineOPort" ;
		protected static final String	URIControllerWindTurbineInboundPortURI = "controllerWindTurbineIPort" ;
		
		//--------------------------------------------------------------
		//-------------------------- KETTLE ----------------------------
		//--------------------------------------------------------------	
		public static final String	KETTLE_COMPONENT_URI = "my-URI-kettle" ;
		protected static final String	URIKettleOutboundPortURI = "kettleOPort" ;
		protected static final String	URIKettleInboundPortURI = "kettleIPort" ;
		protected static final String	URIControllerKettleOutboundPortURI = "controllerKettleOPort" ;
		protected static final String	URIControllerKettleInboundPortURI = "controllerKettleIPort" ;
		protected static final String	URIKettleElectricMeterOutboundPortURI = "kettleElectricMeterOPort" ;
		protected static final String	URIKettleElectricMeterInboundPortURI = "kettleElectricMeterIPort" ;
		
		//--------------------------------------------------------------
		//------------------------- HEATING ----------------------------
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
		//--------------------- ELECTRIC METER -------------------------
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


	// URI of the CVM instances as defined in the config.xml file
	protected static String			Controller_JVM_URI = "controller" ;
	protected static String			Apparels_JVM_URI = "apparels" ;

	public				DistributedCVM(String[] args, int xLayout, int yLayout)
	throws Exception
	{
		super(args, xLayout, yLayout);
	}

	/**
	 * do some initialisation before anything can go on.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true				// no more preconditions.
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.components.cvm.AbstractDistributedCVM#initialise()
	 */
	@Override
	public void			initialise() throws Exception
	{
		// debugging mode configuration; comment and uncomment the line to see
		// the difference
//		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.PUBLIHSING) ;
//		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CONNECTING) ;
//		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.COMPONENT_DEPLOYMENT) ;

		super.initialise() ;
		// any other application-specific initialisation must be put here

	}

	/**
	 * instantiate components and publish their ports.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true				// no more preconditions.
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.components.cvm.AbstractDistributedCVM#instantiateAndPublish()
	 */
	@Override
	public void			instantiateAndPublish() throws Exception
	{
		if (thisJVMURI.equals(Controller_JVM_URI)) {

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


		} else if (thisJVMURI.equals(Apparels_JVM_URI)) {
			//--------------------------------------------------------------
			//------------------------ WIND TURBINE ------------------------
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
			//------------------------- HEATING ----------------------------
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
			//-------------------------- KETTLE ----------------------------
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
			//-------------------------- HEAT SENSOR -----------------------
			//--------------------------------------------------------------
			this.uriHeatSensorURI =
					AbstractComponent.createComponent(
							TemperatureSensor.class.getCanonicalName(),
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

			
			
			
			
		} else {

			System.out.println("Unknown JVM URI... " + thisJVMURI) ;

		}

		super.instantiateAndPublish();
	}

	/**
	 * interconnect the components.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true				// no more preconditions.
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.components.cvm.AbstractDistributedCVM#interconnect()
	 */
	@Override
	public void			interconnect() throws Exception
	{
		assert	this.isIntantiatedAndPublished() ;

		if (thisJVMURI.equals(Controller_JVM_URI)) {

			assert	this.uriControllerURI != null
					&& this.uriElectricMeterURI != null;
			
			//KETTLE <=> CONTROLLER
			this.doPortConnection(
					this.uriControllerURI,
					URIControllerKettleOutboundPortURI,
					URIKettleInboundPortURI,
					ControllerConnector.class.getCanonicalName()) ;	

			//KETTLE <=> ELECTRICMETER
			this.doPortConnection(
					this.uriElectricMeterURI,
					URIKettleElectricMeterOutboundPortURI,
					URIElectricMeterKettleInboundPortURI,
					ElectricMeterConnector.class.getCanonicalName()) ;
			
			//HEATING <=> CONTROLLER
			this.doPortConnection(
					this.uriControllerURI,
					URIControllerHeatingOutboundPortURI,
					URIHeatingInboundPortURI,
					ControllerConnector.class.getCanonicalName()) ;	
			
			//HEATING <=> ELECTRICMETER
			this.doPortConnection(
					this.uriElectricMeterURI,
					URIHeatingElectricMeterOutboundPortURI,
					URIElectricMeterHeatingInboundPortURI,
					ElectricMeterConnector.class.getCanonicalName()) ;
	
			//CHARGER <=> ELECTRICMETER
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
					this.uriControllerURI,
					URIControllerWindTurbineOutboundPortURI,
					URIWindTurbineInboundPortURI,
					ControllerConnector.class.getCanonicalName()) ;	
			
			//CAPTEUR <=> CONTROLLER
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
			
			//CHARGER <=> CONTROLLER
			this.doPortConnection(
					this.uriControllerURI,
					URIControllerChargerOutboundPortURI,
					URIChargerInboundPortURI,
					ControllerConnector.class.getCanonicalName()) ;	
			
			//BATTERY <=> CONTROLLER
			this.doPortConnection(
					this.uriControllerURI,
					URIControllerBatteryOutboundPortURI,
					URIBatteryInboundPortURI,
					ControllerConnector.class.getCanonicalName()) ;	
			
		} else if (thisJVMURI.equals(Apparels_JVM_URI)) {

			assert	this.uriWindTurbineURI != null 
					 && this.uriHeatSensorURI != null && this.uriHeatingURI != null
					 &&	this.uriWindSensorURI != null && this.uriKettleURI != null
					 &&	this.uriBatteryURI != null && this.uriChargerURI != null;
			
			//KETTLE <=> CONTROLLER
			this.doPortConnection(
					this.uriKettleURI,
					URIKettleOutboundPortURI,
					URIControllerKettleInboundPortURI,
					KettleControllerConnector.class.getCanonicalName()) ;

			//HEATING <=> CONTROLLER
			this.doPortConnection(
					this.uriHeatingURI,
					URIHeatingOutboundPortURI,
					URIControllerHeatingInboundPortURI,
					HeatingControllerConnector.class.getCanonicalName()) ;

			//HEATING <=> ELECTRICMETER
			this.doPortConnection(
					this.uriHeatingURI,
					URIElectricMeterHeatingOutboundPortURI,
					URIHeatingElectricMeterInboundPortURI,
					HeatingElectricMeterConnector.class.getCanonicalName()) ;

			//KETTLE <=> ELECTRICMETER
			this.doPortConnection(
					this.uriKettleURI,
					URIElectricMeterKettleOutboundPortURI,
					URIKettleElectricMeterInboundPortURI,
					KettleElectricMetterConnector.class.getCanonicalName()) ;
			
			//CHARGER <=> ELECTRICMETER
			this.doPortConnection(
					this.uriChargerURI,
					URIElectricMeterChargerOutboundPortURI,
					URIChargerElectricMeterInboundPortURI,
					ChargerElectricMeterConnector.class.getCanonicalName()) ;
			
			//WINDTURBINE <=> CONTROLLER
			this.doPortConnection(
					this.uriWindTurbineURI,
					URIWindTurbineOutboundPortURI,
					URIControllerWindTurbineInboundPortURI,
					WindTurbineControllerConnector.class.getCanonicalName()) ;
			
			//CHARGER <=> CONTROLLER
			this.doPortConnection(
					this.uriChargerURI,
					URIChargerOutboundPortURI,
					URIControllerChargerInboundPortURI,
					ChargerControllerConnector.class.getCanonicalName()) ;

			//BATTERY <=> CONTROLLER
			this.doPortConnection(
					this.uriBatteryURI,
					URIBatteryOutboundPortURI,
					URIControllerBatteryInboundPortURI,
					BatteryControllerConnector.class.getCanonicalName()) ;

			//CAPTEUR => HEATING
			this.doPortConnection(
					this.uriHeatSensorURI,
					URIHeatSensorToHeatingOutboundPortURI,
					URIHeatingToCapteurInboundPortURI,
					TemperatureSensorHeatingConnector.class.getCanonicalName()) ;
			
		} else {

			System.out.println("Unknown JVM URI... " + thisJVMURI) ;

		}

		super.interconnect();
	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractDistributedCVM#finalise()
	 */
	@Override
	public void			finalise() throws Exception
	{
		if (thisJVMURI.equals(Controller_JVM_URI)) {

			assert	this.uriControllerURI != null
					&& this.uriElectricMeterURI != null;

		} else if (thisJVMURI.equals(Apparels_JVM_URI)) {

			assert	this.uriWindTurbineURI != null 
					&& this.uriHeatSensorURI != null && this.uriHeatingURI != null
					&& this.uriWindSensorURI != null && this.uriKettleURI != null
					&& this.uriBatteryURI != null && this.uriChargerURI != null;
			
		} else {

			System.out.println("Unknown JVM URI... " + thisJVMURI) ;

		}

		super.finalise() ;
	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractDistributedCVM#shutdown()
	 */
	@Override
	public void			shutdown() throws Exception
	{
		if (thisJVMURI.equals(Controller_JVM_URI)) {
			assert	this.uriControllerURI != null
					&& this.uriElectricMeterURI != null;
		} else if (thisJVMURI.equals(Apparels_JVM_URI)) {

			assert	this.uriWindTurbineURI != null 
					&& this.uriHeatSensorURI != null && this.uriHeatingURI != null
					&& this.uriWindSensorURI != null && this.uriKettleURI != null
					&& this.uriBatteryURI != null && this.uriChargerURI != null;

		} else {

			System.out.println("Unknown JVM URI... " + thisJVMURI) ;

		}

		super.shutdown();
	}

	public static void	main(String[] args)
	{
		try {
			DistributedCVM da  = new DistributedCVM(args, 2, 5) ;
			da.startStandardLifeCycle(15000L) ;
			Thread.sleep(10000L) ;
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
}
//-----------------------------------------------------------------------------
