package interfaces;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface ChargeurToBatterieI extends DataOfferedI,DataRequiredI  {
	
	public void sendPower(double power) throws Exception;

}
