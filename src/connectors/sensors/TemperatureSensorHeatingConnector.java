package connectors.sensors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.heating.HeatingTemperatureSensorI;
import interfaces.sensors.TemperatureSensorHeatingI;

public class TemperatureSensorHeatingConnector extends AbstractConnector implements TemperatureSensorHeatingI {

	@Override
	public double getHeating() throws Exception {
		return ((HeatingTemperatureSensorI)this.offering).sendHeating() ;
	}

}
