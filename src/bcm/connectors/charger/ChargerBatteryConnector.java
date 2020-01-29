package bcm.connectors.charger;

import bcm.interfaces.battery.BatteryI;

import bcm.interfaces.charger.ChargerBatteryI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
/**
 * This class is used to link the in/out ports between the battery and the charger
 * @author Julien Xavier et Alexis Belanger
 *
 */
public class ChargerBatteryConnector extends AbstractConnector implements ChargerBatteryI{

	/**
	 * this method is called by the charger outboundport to send the quantity of energy to the battery
	 */
	@Override
	public void sendPower(double power) throws Exception {
		((BatteryI)this.offering).receivePower(power);
	}

}
