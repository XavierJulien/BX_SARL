package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

public interface ControleurI extends OfferedI,RequiredI{

	public void startEolienne() throws Exception;
	public void stopEolienne() throws Exception;
	public void startBouilloire() throws Exception;
	public void stopBouilloire() throws Exception;
	public double getProd() throws Exception;
	public double getVent() throws Exception;
	
}
