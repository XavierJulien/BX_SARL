package bcm.interfaces.battery;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

/**
 * THis class defines the interface BatteryChargerI 
 * @author Julien Xavier et Alexis Belanger
 *
 */
public interface BatteryChargerI  extends DataOfferedI,DataRequiredI {
	
	/**
	 * this method is called to transmit energy from the charger to the battery
	 * @param power the quantity of energy that the charger puts in the battery
	 * @throws Exception
	 */
	public void receivePower(double power) throws Exception;
	
}
