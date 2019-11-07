package interfaces;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface BouilloireI extends DataOfferedI,DataRequiredI{

	public void startBouilloire() throws Exception;
	public void stopBouilloire() throws Exception;
}
