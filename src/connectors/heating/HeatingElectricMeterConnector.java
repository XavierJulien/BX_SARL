package connectors.heating;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.electricmeter.ElectricMeterI;
import interfaces.heating.HeatingElectricMeterI;

public class HeatingElectricMeterConnector extends AbstractConnector implements HeatingElectricMeterI{

	@Override
	public double sendConsumption() throws Exception {
		return ((ElectricMeterI)this.offering).getHeatingConsumption();
	}

}