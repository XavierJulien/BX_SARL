package bcm.ports.sensors;

import bcm.interfaces.sensors.WindSensorI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

@SuppressWarnings("serial")
public class WindSensorOutboundPort extends AbstractOutboundPort implements WindSensorI {

	public WindSensorOutboundPort(ComponentI owner) throws Exception {
		super(WindSensorI.class, owner);
		// TODO Auto-generated constructor stub
	}
	public WindSensorOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, WindSensorI.class, owner);
	}
	@Override
	public void sendWindSpeed(double speed) throws Exception {
		((WindSensorI)this.connector).sendWindSpeed(speed) ;
	}

	
}
