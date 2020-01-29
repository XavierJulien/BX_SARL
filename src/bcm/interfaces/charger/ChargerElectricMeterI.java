package bcm.interfaces.charger;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;


/**
 * THis class defines the interface ChargerElectricMEterI used in the Charger - ElectricMeter interactions
 * @author Julien Xavier et Alexis Belanger
 *
 */
public interface ChargerElectricMeterI extends DataOfferedI,DataRequiredI{

	/**
	 * This method is called by the Charger Outboundport and calls the Electric Meter OutboundPort getChargerConsumption method to get the current Charger consumption
	 * @param consumption the charger consumption
	 * @throws Exception
	 */
	public void sendConsumption(double consumption) throws Exception;
}
