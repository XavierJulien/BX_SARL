package interfaces;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface HeatingI extends DataOfferedI,DataRequiredI{

	public void startHeating() throws Exception;
	public void stopHeating() throws Exception;
}
