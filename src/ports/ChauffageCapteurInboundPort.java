package ports;

import components.Chauffage;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ChauffageCapteurChaleurI;

public class ChauffageCapteurInboundPort extends AbstractInboundPort implements ChauffageCapteurChaleurI {

	private static final long serialVersionUID = 1L;

	//--------------------------------------------------------------
	//-------------------------CONSTRUCTORS-------------------------
	//--------------------------------------------------------------
		public ChauffageCapteurInboundPort(String uri, ComponentI owner) throws Exception {
			super(uri, ChauffageCapteurChaleurI.class, owner);
		}
		
		public ChauffageCapteurInboundPort(ComponentI owner) throws Exception {
			super(ChauffageCapteurChaleurI.class, owner);
		}

		@Override
		public double sendHeating() throws Exception {
			return this.getOwner().handleRequestSync(
					owner -> ((Chauffage)owner).sendHeating()) ;	
		}

	
}
