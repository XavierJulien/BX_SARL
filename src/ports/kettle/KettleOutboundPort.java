package ports.kettle;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.kettle.KettleI;

public class KettleOutboundPort extends AbstractOutboundPort implements KettleI{
	
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
	public double sendConsumption() throws Exception {
		//shouldn't be used
		return 0;
	}


}
