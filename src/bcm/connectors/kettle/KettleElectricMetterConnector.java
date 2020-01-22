package bcm.connectors.kettle;

import bcm.interfaces.electricmeter.ElectricMeterI;
import bcm.interfaces.kettle.KettleElectricMeterI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class KettleElectricMetterConnector extends AbstractConnector implements KettleElectricMeterI{

	@Override
	public void sendConsumption(double consumption) throws Exception {
		((ElectricMeterI)this.offering).getKettleConsumption(consumption);
	}

}