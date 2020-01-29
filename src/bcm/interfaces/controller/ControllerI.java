package bcm.interfaces.controller;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

/**
 * This class defines the interface ControllerI for all the controller behaviour
 * @author Julien Xavier et Alexis Belanger
 *
 */
public interface ControllerI extends OfferedI,RequiredI{

	//------------EOLIENNE------------
	
	/**
	 * This method is called start the wind Turbine
	 * @throws Exception
	 */
	public void startWindTurbine() throws Exception;
	/**
	 * this method is called to stop the wind turbine
	 * @throws Exception
	 */
	public void stopWindTurbine() throws Exception;
	
	/**
	 * this method is called to get the energy production of the wind turbine
	 * @param production the amount of energy sent by the wind turbine
	 * @throws Exception
	 */
	public void getProduction(double production) throws Exception;

	
	//------------CHAUFFAGE------------
	/**
	 * this method is called to start the heating
	 * @throws Exception
	 */
	public void startHeating() throws Exception;
	
	/**
	 * this method is called to stop the heating
	 * @throws Exception
	 */
	public void stopHeating() throws Exception;
	
	/**
	 * this method is called to put more power in the heating to produce more heat
	 * @param power the percentage of power the controller wants to put in the heating
	 * @throws Exception
	 */
	public void putExtraPowerInHeating(int power) throws Exception;
	
	/**
	 * this method is called to slow the heating
	 * @param power hte percentage of power the controller wants to reduce to the current heating power
	 * @throws Exception
	 */
	public void slowHeating(int power) throws Exception;
	
	//------------COMPTEUR------------
	/**
	 * this method is called to start the electric meter
	 * @throws Exception
	 */
	public void startElectricMeter() throws Exception;
	
	/**
	 * this method is called to stop the electric meter (not used, could be used in a more complex behaviour)
	 * @throws Exception
	 */
	public void stopElectricMeter() throws Exception;
	
	/**
	 * this method is called to get the total house consumption
	 * @param total the sum of the kettle, the heating and the charger current consumption
	 * @throws Exception
	 */
	public void getAllConsumption(double total) throws Exception;
	
	//------------CHARGEUR------------
	
	/**
	 * this method is called to start the charger
	 * @throws Exception
	 */
	public void startCharger() throws Exception;
	
	/**
	 * this method is called to stop the charger
	 * @throws Exception
	 */
	public void stopCharger() throws Exception;
	
	//------------BATTERIE------------
	/**
	 * this method is called to start the battery
	 * @throws Exception
	 */
	public void startBattery() throws Exception;
	
	/**
	 * this method is called to stop the battery
	 * @throws Exception
	 */
	public void stopBattery() throws Exception;
	
	/**
	 * this method is called to get the current battery charge
	 * @param percentage the current charge percentage
	 * @throws Exception
	 */
	public void getBatteryChargePercentage(double percentage) throws Exception;
	
	/**
	 * this method is called to get the current battery energy production
	 * @param energy the current battery production
	 * @throws Exception
	 */
	public void getBatteryProduction(double energy) throws Exception;
	
	//------------CAPTEURS------------
	
	/**
	 * this method is called to get the wind speed from the wind sensor
	 * @param speed the current wind speed
	 * @throws Exception
	 */
	public void getWindSpeed(double speed) throws Exception;
	
	/**
	 * his method is called to get the temperature from th temperature sensor
	 * @param temperature the current temperature
	 * @throws Exception
	 */
	public void getTemperature(double temperature) throws Exception;
	
	
}
