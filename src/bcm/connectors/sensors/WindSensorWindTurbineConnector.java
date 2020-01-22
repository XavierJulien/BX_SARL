package bcm.connectors.sensors;

import bcm.interfaces.sensors.WindSensorI;
import bcm.interfaces.windturbine.WindTurbineI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class WindSensorWindTurbineConnector extends AbstractConnector implements WindSensorI {

	@Override
	public void sendWindSpeed(double speed) throws Exception {
		((WindTurbineI)this.offering).getWindSpeed(speed);		
		
	}

}
