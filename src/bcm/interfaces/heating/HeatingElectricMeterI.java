package bcm.interfaces.heating;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;


/**
 * this class defines the interface HeatingElectricMeterI used in the Heating to electricMeter communication
 * @author Julien Xavier et Alexis Belanger
 *
 */
public interface HeatingElectricMeterI extends DataOfferedI,DataRequiredI{

	/**
	 * this method is called to send the current Heating Consumption
	 * @param consumption the heating consumption
	 * @throws Exception
	 */
	public void sendConsumption(double consumption) throws Exception;
}
