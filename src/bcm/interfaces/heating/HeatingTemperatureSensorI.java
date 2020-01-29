package bcm.interfaces.heating;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;
/**
 * this class define the interface HeatingTemperatureSensorI used in the Heating - Temperature Sensor communication
 * @author Julien Xavier et Alexis Belanger
 *
 */
public interface HeatingTemperatureSensorI extends DataOfferedI,DataRequiredI  {
	
	/**
	 * this method is called to send the heat produced by the heating to the temeprature sensor
	 * @return
	 * @throws Exception
	 */
	public double sendHeating() throws Exception;

}
