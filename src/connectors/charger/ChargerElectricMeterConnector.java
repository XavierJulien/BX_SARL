package connectors.charger;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.charger.ChargerElectricMeterI;
import interfaces.electricmeter.ElectricMeterI;

public class ChargerElectricMeterConnector extends AbstractConnector implements ChargerElectricMeterI{

	@Override
	public void sendConsumption(double consumption) throws Exception {
		((ElectricMeterI)this.offering).getChargerConsumption(consumption);
	}

}