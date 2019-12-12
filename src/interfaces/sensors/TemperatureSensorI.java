package interfaces.sensors;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

public interface TemperatureSensorI extends OfferedI,RequiredI {
	
	public void sendTemperature(double temperature) throws Exception;
}
