package interfaces;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface ChauffageI extends DataOfferedI,DataRequiredI{

	public void startChauffage() throws Exception;
	public void stopChauffage() throws Exception;
}
