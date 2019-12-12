package interfaces.kettle;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface KettleElectricMeterI extends DataOfferedI,DataRequiredI{

	public double sendConsumption() throws Exception;
}
