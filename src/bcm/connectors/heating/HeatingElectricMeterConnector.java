package bcm.connectors.heating;

import bcm.interfaces.electricmeter.ElectricMeterI;
import bcm.interfaces.heating.HeatingElectricMeterI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class HeatingElectricMeterConnector extends AbstractConnector implements HeatingElectricMeterI{

	@Override
	public void sendConsumption(double total) throws Exception {
		((ElectricMeterI)this.offering).getHeatingConsumption(total);
	}

}