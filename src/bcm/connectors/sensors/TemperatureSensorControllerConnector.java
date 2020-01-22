package bcm.connectors.sensors;

import bcm.interfaces.controller.ControllerI;
import bcm.interfaces.sensors.TemperatureSensorI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class TemperatureSensorControllerConnector extends AbstractConnector implements TemperatureSensorI  {

	@Override
	public void sendTemperature(double temperature) throws Exception {
		 ((ControllerI)this.offering).getTemperature(temperature);		
	}

}
