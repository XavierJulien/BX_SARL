package interfaces;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface EolienneI extends DataOfferedI,DataRequiredI{
	
	public void startEolienne() throws Exception;
	
	public void stopEolienne() throws Exception;
}
