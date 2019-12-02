package ports;

import components.TemperatureSensor;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.TemperatureSensorI;

public class TemperatureSensorInboundPort extends AbstractInboundPort implements TemperatureSensorI {

	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public TemperatureSensorInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, TemperatureSensorI.class, owner);
	}
	
	public TemperatureSensorInboundPort(ComponentI owner) throws Exception {
		super(TemperatureSensorI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	@Override
	public double sendTemperature() throws Exception {
		return this.getOwner().handleRequestSync(
				owner -> ((TemperatureSensor)owner).sendTemperature()) ;	
	}
}
