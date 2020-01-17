package connectors.charger;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.battery.BatteryI;
import interfaces.charger.ChargerBatteryI;
public class ChargerBatteryConnector extends AbstractConnector implements ChargerBatteryI{

	@Override
	public void sendPower(double power) throws Exception {
		((BatteryI)this.offering).receivePower(power);
	}

}
