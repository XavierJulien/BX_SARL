package ports;

import components.CapteurVent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.CapteurVentI;

public class CapteurVentInboundPort extends AbstractInboundPort implements CapteurVentI {

	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public CapteurVentInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, CapteurVentI.class, owner);
	}
	
	public CapteurVentInboundPort(ComponentI owner) throws Exception {
		super(CapteurVentI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	@Override
	public double sendWind() throws Exception {
		return this.getOwner().handleRequestSync(
				owner -> ((CapteurVent)owner).sendWind()) ;	
	}	
}
