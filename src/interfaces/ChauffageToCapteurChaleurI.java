package interfaces;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface ChauffageToCapteurChaleurI extends DataOfferedI,DataRequiredI  {
	
	public double sendHeating() throws Exception;

}