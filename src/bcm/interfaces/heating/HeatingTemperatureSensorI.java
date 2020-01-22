package bcm.interfaces.heating;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface HeatingTemperatureSensorI extends DataOfferedI,DataRequiredI  {
	
	public double sendHeating() throws Exception;

}
