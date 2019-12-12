package connectors.heating;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.electricmeter.ElectricMeterI;
import interfaces.heating.HeatingElectricMeterI;

public class HeatingElectricMeterConnector extends AbstractConnector implements HeatingElectricMeterI{

	@Override
	public void sendConsumption(double total) throws Exception {
		((ElectricMeterI)this.offering).getHeatingConsumption(total);
	}

}