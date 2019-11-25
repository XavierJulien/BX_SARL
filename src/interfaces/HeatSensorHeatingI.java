package interfaces;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface CapteurChaleurChauffageI extends DataOfferedI,DataRequiredI {
	
	public double getHeating() throws Exception;

}
