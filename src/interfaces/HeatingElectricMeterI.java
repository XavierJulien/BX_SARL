package interfaces;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface HeatingElectricMeterI extends DataOfferedI,DataRequiredI{

	public double sendConsumption() throws Exception;
}
