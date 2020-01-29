package bcm.interfaces.electricmeter;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

/**
 * This class defines the interface ElectricMeterI, it is used to represent the Electric Meter Behaviour
 * @author Julien Xavier et Alexis Belanger
 *
 */
public interface ElectricMeterI extends DataOfferedI,DataRequiredI{

	/**
	 * This method is called to start the Electric Meter
	 * @throws Exception
	 */
	public void startElectricMeter() throws Exception;
	
	/**
	 * This method is called to stop the Electric Meter
	 * @throws Exception
	 */
	public void stopElectricMeter() throws Exception;
	
	/**
	 * This method is called to send the the total house consumption to the controller
	 * @param total the current total consumption
	 * @throws Exception
	 */
	public void sendAllConsumption(double total) throws Exception;
	
	/**
	 * this method is called to get the heating consumption
	 * @param consumption the heating consumption
	 * @throws Exception
	 */
	public void getHeatingConsumption(double consumption) throws Exception;

	/**
	 * this method is called to get the Kettle consumption
	 * @param consumption the Kettle consumption
	 * @throws Exception
	 */
	public void getKettleConsumption(double consumption) throws Exception;

	/**
	 * this method is called to get the charger consumption
	 * @param consumption the charger consumption
	 * @throws Exception
	 */
	public void getChargerConsumption(double consumption) throws Exception;
	

}
