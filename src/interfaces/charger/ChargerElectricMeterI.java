package interfaces.charger;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface ChargerElectricMeterI extends DataOfferedI,DataRequiredI{

	public double sendConsumption() throws Exception;
}
