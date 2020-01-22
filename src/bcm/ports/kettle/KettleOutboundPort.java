package bcm.ports.kettle;

import bcm.interfaces.kettle.KettleElectricMeterI;
import bcm.interfaces.kettle.KettleI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class KettleOutboundPort extends AbstractOutboundPort implements KettleI, KettleElectricMeterI{
	
	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public KettleOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, KettleI.class, owner);
	}
	
	public KettleOutboundPort(ComponentI owner) throws Exception {
		super(KettleI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	@Override
	public void startKettle() throws Exception {
		((KettleI)this.connector).startKettle() ;
	}

	@Override
	public void stopKettle() throws Exception {
		((KettleI)this.connector).stopKettle() ;
	}

	@Override
	public void sendConsumption(double consumption) throws Exception {
		((KettleElectricMeterI)this.connector).sendConsumption(consumption) ;
	}


}
