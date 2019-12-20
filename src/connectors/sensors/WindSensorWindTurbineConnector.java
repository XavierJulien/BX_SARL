package connectors.sensors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.sensors.WindSensorI;
import interfaces.windturbine.WindTurbineI;

public class WindSensorWindTurbineConnector extends AbstractConnector implements WindSensorI {

	@Override
	public void sendWindSpeed(double speed) throws Exception {
		((WindTurbineI)this.offering).getWindSpeed(speed);		
		
	}

}
