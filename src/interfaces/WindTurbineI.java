package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

public interface WindTurbineI extends OfferedI,RequiredI{
	
	public void startWindTurbine() throws Exception;
	public void stopWindTurbine() throws Exception;
	public double sendProduction() throws Exception;
}
