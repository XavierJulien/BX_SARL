package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.EolienneI;

public class EolienneOutboundPort extends AbstractOutboundPort implements EolienneI {
	
	private static final long serialVersionUID = 1L;
	
//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public EolienneOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, EolienneI.class, owner);
	}
	
	public EolienneOutboundPort(ComponentI owner) throws Exception {
		super(EolienneI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	@Override
	public void startEolienne() throws Exception {
		((EolienneI)this.connector).startEolienne() ;
	}

	@Override
	public void stopEolienne() throws Exception {
		((EolienneI)this.connector).stopEolienne() ;
	}

	@Override
	public double sendProduction() throws Exception {
		return 0;
	}
}
