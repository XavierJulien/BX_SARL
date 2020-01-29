package bcm.interfaces.charger;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;


/**
 * This classs defines the interface ChargerBatteryI, it is used in the Battery-Charger communication
 * @author Julien Xavier et Alexis Belanger 
 *
 */
public interface ChargerBatteryI extends DataOfferedI,DataRequiredI  {
	
	
	/**
	 * This method is called by the Charger Outboundport and calls the BAttery OutboundPort get receivePower method to get the charger energy and put it in the battery
	 * @param power the mount of energy send by the charger
	 * @throws Exception
	 */
	public void sendPower(double power) throws Exception;

}
