package bcm.interfaces.heating;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface HeatingElectricMeterI extends DataOfferedI,DataRequiredI{

	public void sendConsumption(double consumption) throws Exception;
}
