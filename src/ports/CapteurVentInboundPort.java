package ports;

import components.CapteurVent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.CapteurVentI;

public class CapteurVentInboundPort extends AbstractInboundPort implements CapteurVentI {

	public CapteurVentInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, CapteurVentI.class, owner);
	}
	
	
	public CapteurVentInboundPort(ComponentI owner) throws Exception {
		super(CapteurVentI.class, owner);
	}


	private static final long serialVersionUID = 1L;


	@Override
	public double sendWind() throws Exception {
		return this.getOwner().handleRequestSync(
				owner -> ((CapteurVent)owner).sendWind()) ;
		
	}
	
	
	
	
}
