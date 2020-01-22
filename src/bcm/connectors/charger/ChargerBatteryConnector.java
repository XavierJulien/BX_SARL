package bcm.connectors.charger;

import bcm.interfaces.battery.BatteryI;
import bcm.interfaces.charger.ChargerBatteryI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
public class ChargerBatteryConnector extends AbstractConnector implements ChargerBatteryI{

	@Override
	public void sendPower(double power) throws Exception {
		((BatteryI)this.offering).receivePower(power);
	}

}
