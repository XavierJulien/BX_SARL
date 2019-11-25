package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ChargerElectricMeterI;
import interfaces.ElectricMeterI;

public class ChargerElectricMeterConnector extends AbstractConnector implements ChargerElectricMeterI{

	@Override
	public double sendConsumption() throws Exception {
		return ((ElectricMeterI)this.offering).getChargerConsumption();
	}

}