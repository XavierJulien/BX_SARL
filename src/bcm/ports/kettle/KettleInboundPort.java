package bcm.ports.kettle;

import bcm.interfaces.kettle.KettleElectricMeterI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class KettleInboundPort extends AbstractInboundPort implements KettleElectricMeterI{
	
	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public KettleInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, KettleElectricMeterI.class, owner);
	}
	
	public KettleInboundPort(ComponentI owner) throws Exception {
		super(KettleElectricMeterI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------


	@Override
	public void sendConsumption(double consumption) throws Exception {
		//unused
	}
}