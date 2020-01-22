package bcm.ports.sensors;

import bcm.interfaces.sensors.WindSensorI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class WindSensorInboundPort extends AbstractInboundPort implements WindSensorI {

	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public WindSensorInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, WindSensorI.class, owner);
	}
	
	public WindSensorInboundPort(ComponentI owner) throws Exception {
		super(WindSensorI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	@Override
	public void sendWindSpeed(double speed) throws Exception {
		//unused
	}
}
