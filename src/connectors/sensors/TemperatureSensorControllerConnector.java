package connectors.sensors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.controller.ControllerI;
import interfaces.sensors.TemperatureSensorI;

public class TemperatureSensorControllerConnector extends AbstractConnector implements TemperatureSensorI  {

	@Override
	public void sendTemperature(double temperature) throws Exception {
		 ((ControllerI)this.offering).getTemperature(temperature);		
	}

}
