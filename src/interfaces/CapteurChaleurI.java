package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

public interface CapteurChaleurI extends OfferedI,RequiredI {
	
	public double sendChaleur() throws Exception;
}
