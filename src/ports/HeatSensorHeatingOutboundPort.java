package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.CapteurChaleurChauffageI;

public class CapteurChauffageOutboundPort  extends AbstractOutboundPort implements CapteurChaleurChauffageI{

	private static final long serialVersionUID = 1L;

	//--------------------------------------------------------------
	//-------------------------CONSTRUCTORS-------------------------
	//--------------------------------------------------------------
		public CapteurChauffageOutboundPort(String uri, ComponentI owner) throws Exception {
			super(uri, CapteurChaleurChauffageI.class, owner);
		}
		
		public CapteurChauffageOutboundPort(ComponentI owner) throws Exception {
			super(CapteurChaleurChauffageI.class, owner);
		}

		

	//--------------------------------------------------------------
	//-------------------------SERVICES-----------------------------
	//--------------------------------------------------------------
		
		
		@Override
		public double getHeating() throws Exception {
			return ((CapteurChaleurChauffageI)this.connector).getHeating();
		}
	
}
