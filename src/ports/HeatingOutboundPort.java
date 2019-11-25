package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ChauffageI;

public class ChauffageOutboundPort extends AbstractOutboundPort implements ChauffageI{
	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public ChauffageOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ChauffageI.class, owner);
	}
	
	public ChauffageOutboundPort(ComponentI owner) throws Exception {
		super(ChauffageI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	@Override
	public void startChauffage() throws Exception {
		((ChauffageI)this.connector).startChauffage() ;
	}

	@Override
	public void stopChauffage() throws Exception {
		((ChauffageI)this.connector).stopChauffage() ;
	}
	
}
