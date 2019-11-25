package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.KettleElectricMeterI;
import interfaces.ElectricMeterI;

public class KettleElectricMetterConnector extends AbstractConnector implements KettleElectricMeterI{

	@Override
	public double sendConsumption() throws Exception {
		return ((ElectricMeterI)this.offering).getKettleConsumption();
	}

}