package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.HeatingElectricMeterI;
import interfaces.ElectricMeterI;

public class HeatingElectricMeterConnector extends AbstractConnector implements HeatingElectricMeterI{

	@Override
	public double sendConsumption() throws Exception {
		return ((ElectricMeterI)this.offering).getHeatingConsumption();
	}

}