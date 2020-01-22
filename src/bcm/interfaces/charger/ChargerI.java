package bcm.interfaces.charger;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface ChargerI extends DataOfferedI,DataRequiredI{

	public void startCharger() throws Exception;
	public void stopCharger() throws Exception;
	public void sendConsumption(double consumption) throws Exception;
	public void sendPower(double power) throws Exception;
}
