package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

public interface EolienneI extends OfferedI,RequiredI{
	
	public void startEolienne() throws Exception;
	
	public void stopEolienne() throws Exception;
	
	
	public void sendProduction(double prod) throws Exception;
}
