package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.TemperatureSensorHeatingI;
import interfaces.HeatingTemperatureSensorI;

public class TemperatureSensorHeatingConnector extends AbstractConnector implements TemperatureSensorHeatingI {

	@Override
	public double getHeating() throws Exception {
		return ((HeatingTemperatureSensorI)this.offering).sendHeating() ;
	}

}
