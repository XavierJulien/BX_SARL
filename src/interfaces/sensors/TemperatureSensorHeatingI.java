package interfaces.sensors;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface TemperatureSensorHeatingI extends DataOfferedI,DataRequiredI {
	
	public double getHeating() throws Exception;

}
