package bcm.interfaces.sensors;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

public interface WindSensorI extends OfferedI,RequiredI {
	
	public void sendWindSpeed(double speed) throws Exception;
}
