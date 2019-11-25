package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

public interface WindSensorI extends OfferedI,RequiredI {
	
	public double sendWind() throws Exception;
}
