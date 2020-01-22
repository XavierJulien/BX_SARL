package bcm.interfaces.kettle;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface KettleElectricMeterI extends DataOfferedI,DataRequiredI{

	public void sendConsumption(double consumption) throws Exception;
}
