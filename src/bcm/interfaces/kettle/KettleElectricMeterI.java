package bcm.interfaces.kettle;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

/**
 * This class defines the intrface KettleElectricMeterI used in the Kettl - Electric Meter communication
 * @author Julien Xavier et Alexis Belanger
 *
 */
public interface KettleElectricMeterI extends DataOfferedI,DataRequiredI{

	/**
	 * this method is called by the Kettle side to send its consumption to the Electric Meters
	 * @param consumption the kettle consumption
	 * @throws Exception
	 */
	public void sendConsumption(double consumption) throws Exception;
}
