package interfaces;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface BatterieChargeurI  extends DataOfferedI,DataRequiredI {

	public void receivePower(double power) throws Exception;
	
}
