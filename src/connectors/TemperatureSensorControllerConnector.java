package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ControllerI;
import interfaces.TemperatureSensorI;

public class TemperatureSensorControllerConnector extends AbstractConnector implements TemperatureSensorI  {

	@Override
	public void sendTemperature(double temperature) throws Exception {
		 ((ControllerI)this.offering).getTemperature(temperature);		
	}

}
