package interfaces;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface ChargeurI extends DataOfferedI,DataRequiredI{

	public void startChargeur() throws Exception;
	public void stopChargeur() throws Exception;
	public double sendConso() throws Exception;
}
