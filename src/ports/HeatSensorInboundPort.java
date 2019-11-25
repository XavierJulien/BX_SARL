package ports;

import components.CapteurChaleur;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.CapteurChaleurI;

public class CapteurChaleurInboundPort extends AbstractInboundPort implements CapteurChaleurI {

	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public CapteurChaleurInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, CapteurChaleurI.class, owner);
	}
	
	public CapteurChaleurInboundPort(ComponentI owner) throws Exception {
		super(CapteurChaleurI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	@Override
	public double sendChaleur() throws Exception {
		return this.getOwner().handleRequestSync(
				owner -> ((CapteurChaleur)owner).sendChaleur()) ;	
	}
}
