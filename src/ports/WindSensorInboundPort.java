package ports;

import components.WindSensor;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.WindSensorI;

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
	public double sendWind() throws Exception {
		return this.getOwner().handleRequestSync(
				owner -> ((WindSensor)owner).sendWind()) ;	
	}	
}
