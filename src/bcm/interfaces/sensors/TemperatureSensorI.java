package bcm.interfaces.sensors;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

/**
 * This class defines the interface TemperatureSensorI used in the Temperature Sensor to Controller Communication
 * @author Julien Xavier et Alexis Belanger
 *
 */
public interface TemperatureSensorI extends OfferedI,RequiredI {
	
	/**
	 * This method is called by the temperature sensor side to send the current temperature to the controller
	 * @param temperature the current temperature
	 * @throws Exception
	 */
	public void sendTemperature(double temperature) throws Exception;
}
