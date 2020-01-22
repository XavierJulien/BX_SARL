package bcm.interfaces.windturbine;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

public interface WindTurbineI extends OfferedI,RequiredI{
	
	public void startWindTurbine() throws Exception;
	public void stopWindTurbine() throws Exception;
	public void sendProduction(double production) throws Exception;
	public void getWindSpeed(double speed) throws Exception;
}
