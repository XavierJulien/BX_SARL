package bcm.connectors.charger;

import bcm.interfaces.charger.ChargerElectricMeterI;
import bcm.interfaces.electricmeter.ElectricMeterI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ChargerElectricMeterConnector extends AbstractConnector implements ChargerElectricMeterI{

	@Override
	public void sendConsumption(double consumption) throws Exception {
		((ElectricMeterI)this.offering).getChargerConsumption(consumption);
	}

}