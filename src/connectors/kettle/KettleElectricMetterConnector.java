package connectors.kettle;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.electricmeter.ElectricMeterI;
import interfaces.kettle.KettleElectricMeterI;

public class KettleElectricMetterConnector extends AbstractConnector implements KettleElectricMeterI{

	@Override
	public double sendConsumption() throws Exception {
		return ((ElectricMeterI)this.offering).getKettleConsumption();
	}

}