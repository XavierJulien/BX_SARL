package bcm.connectors.sensors;

import bcm.interfaces.heating.HeatingTemperatureSensorI;
import bcm.interfaces.sensors.TemperatureSensorHeatingI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class TemperatureSensorHeatingConnector extends AbstractConnector implements TemperatureSensorHeatingI {

	@Override
	public double getHeating() throws Exception {
		return ((HeatingTemperatureSensorI)this.offering).sendHeating() ;
		
	}

}
