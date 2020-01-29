package bcm.interfaces.electricmeter;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

/**
 * This class defines the interface ElectricMeterControllerI used for the Electric Meter -> Controller communication
 * @author Julien Xavier et Alexis Belanger
 *
 */
public interface ElectricMeterControllerI extends DataOfferedI,DataRequiredI{
	/**
	 * This method is called to send the total consumption to the controller
	 * @param conso the current consumption of the Kettle, the heating and the charger
	 * @throws Exception
	 */
	public void sendTotalConsumption(double conso) throws Exception;

}
