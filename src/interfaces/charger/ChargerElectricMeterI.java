package interfaces.charger;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface ChargerElectricMeterI extends DataOfferedI,DataRequiredI{

	public void sendConsumption(double consumption) throws Exception;
}
