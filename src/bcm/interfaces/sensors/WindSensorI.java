package bcm.interfaces.sensors;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

/**
 * This class defines the interface WindSensorI used in the Wind sensor -> wind turbine communication
 * @author Julien Xavier et Alexis Belanger
 *
 */
public interface WindSensorI extends OfferedI,RequiredI {
	
	/**
	 * this method is called by the wind sensor side to send the current wind speed to the wind turbine
	 * @param speed the wind speed
	 * @throws Exception
	 */
	public void sendWindSpeed(double speed) throws Exception;
}
