package ports.sensors;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.sensors.TemperatureSensorI;

@SuppressWarnings("serial")
public class TemperatureSensorOutboundPort extends AbstractOutboundPort implements TemperatureSensorI {

	public TemperatureSensorOutboundPort(ComponentI owner) throws Exception {
		super(TemperatureSensorI.class, owner);
		// TODO Auto-generated constructor stub
	}
	public TemperatureSensorOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, TemperatureSensorI.class, owner);
	}

	@Override
	public void sendTemperature(double temperature) throws Exception {
		((TemperatureSensorI)this.connector).sendTemperature(temperature) ;
	 }

}
