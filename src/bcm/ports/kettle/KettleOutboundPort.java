package bcm.ports.kettle;

import bcm.interfaces.kettle.KettleElectricMeterI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class KettleOutboundPort extends AbstractOutboundPort implements  KettleElectricMeterI{
	
	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public KettleOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, KettleElectricMeterI.class, owner);
	}
	
	public KettleOutboundPort(ComponentI owner) throws Exception {
		super(KettleElectricMeterI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------


	@Override
	public void sendConsumption(double consumption) throws Exception {
		((KettleElectricMeterI)this.connector).sendConsumption(consumption) ;
	}


}
