package interfaces;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface BatterieToChargeurI  extends DataOfferedI,DataRequiredI {

	public void receivePower(double power) throws Exception;
	
}
