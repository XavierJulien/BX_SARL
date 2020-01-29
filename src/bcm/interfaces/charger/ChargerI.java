package bcm.interfaces.charger;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

/**
 * This class defines the interfcae ChargerI used to define the different methods involved in the charger behaviour
 * @author Julien Xavier et Alexis Belanger
 *
 */
public interface ChargerI extends DataOfferedI,DataRequiredI{

	/**
	 * This method is called to start the charger
	 * @throws Exception
	 */
	public void startCharger() throws Exception;
	
	/**
	 * This method is called to stop the charger
	 * @throws Exception
	 */
	public void stopCharger() throws Exception;
	
	/**
	 *  
	 * @param consumption
	 * @throws Exception
	 */
	public void sendConsumption(double consumption) throws Exception;
	
	/**
	 * This method is called to send energy from the charger to the battery
	 * @param power the amount of energy send by the charger
	 * @throws Exception
	 */
	public void sendPower(double power) throws Exception;
}
