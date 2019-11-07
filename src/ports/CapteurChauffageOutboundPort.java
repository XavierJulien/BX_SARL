package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.CapteurChaleurToChauffageI;

public class CapteurChauffageOutboundPort  extends AbstractOutboundPort implements CapteurChaleurToChauffageI{

	private static final long serialVersionUID = 1L;

	//--------------------------------------------------------------
	//-------------------------CONSTRUCTORS-------------------------
	//--------------------------------------------------------------
		public CapteurChauffageOutboundPort(String uri, ComponentI owner) throws Exception {
			super(uri, CapteurChaleurToChauffageI.class, owner);
		}
		
		public CapteurChauffageOutboundPort(ComponentI owner) throws Exception {
			super(CapteurChaleurToChauffageI.class, owner);
		}

		

	//--------------------------------------------------------------
	//-------------------------SERVICES-----------------------------
	//--------------------------------------------------------------
		
		
		@Override
		public double getHeating() throws Exception {
			return ((CapteurChaleurToChauffageI)this.connector).getHeating();
		}
	
}
