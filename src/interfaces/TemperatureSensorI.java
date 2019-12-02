package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

public interface TemperatureSensorI extends OfferedI,RequiredI {
	
	public double sendTemperature() throws Exception;
}
