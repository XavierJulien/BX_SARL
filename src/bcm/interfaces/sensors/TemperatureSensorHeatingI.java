package bcm.interfaces.sensors;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

/**
 * This class defines the interface TemperatureSensorHetingI used in the Temperature Sensor -> Heating communication
 * @author Julien Xavier et Alexis Belanger
 *
 */
public interface TemperatureSensorHeatingI extends DataOfferedI,DataRequiredI {
	
	/**
	 * This method is called to get the heat produced by the heating
	 * @return
	 * @throws Exception
	 */
	public double getHeating() throws Exception;

}
