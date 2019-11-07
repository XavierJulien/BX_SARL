package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.CompteurI;

public class CompteurOutboundPort extends AbstractOutboundPort implements CompteurI{
	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public CompteurOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, CompteurI.class, owner);
	}
	
	public CompteurOutboundPort(ComponentI owner) throws Exception {
		super(CompteurI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	@Override
	public void startCompteur() throws Exception {
		((CompteurI)this.connector).startCompteur() ;
	}

	@Override
	public void stopCompteur() throws Exception {
		((CompteurI)this.connector).stopCompteur() ;
	}
}
