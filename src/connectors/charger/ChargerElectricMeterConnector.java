package connectors.charger;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.charger.ChargerElectricMeterI;
import interfaces.electricmeter.ElectricMeterI;

public class ChargerElectricMeterConnector extends AbstractConnector implements ChargerElectricMeterI{

	@Override
	public double sendConsumption() throws Exception {
		return ((ElectricMeterI)this.offering).getChargerConsumption();
	}

}