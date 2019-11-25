package ports;

import components.HeatSensor;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.HeatSensorI;

public class HeatSensorInboundPort extends AbstractInboundPort implements HeatSensorI {

	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public HeatSensorInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, HeatSensorI.class, owner);
	}
	
	public HeatSensorInboundPort(ComponentI owner) throws Exception {
		super(HeatSensorI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	@Override
	public double sendTemperature() throws Exception {
		return this.getOwner().handleRequestSync(
				owner -> ((HeatSensor)owner).sendTemperature()) ;	
	}
}
