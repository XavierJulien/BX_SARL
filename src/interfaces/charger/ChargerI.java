package interfaces.charger;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface ChargerI extends DataOfferedI,DataRequiredI{

	public void startCharger() throws Exception;
	public void stopCharger() throws Exception;
	public double sendConsumption() throws Exception;
}
