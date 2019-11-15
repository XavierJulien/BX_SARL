package interfaces;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface ChauffageCapteurChaleurI extends DataOfferedI,DataRequiredI  {
	
	public double sendHeating() throws Exception;

}
