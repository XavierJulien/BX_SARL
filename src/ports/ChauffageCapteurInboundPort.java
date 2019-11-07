package ports;

import components.Chauffage;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ChauffageToCapteurChaleurI;

public class ChauffageCapteurInboundPort extends AbstractInboundPort implements ChauffageToCapteurChaleurI {

	private static final long serialVersionUID = 1L;

	//--------------------------------------------------------------
	//-------------------------CONSTRUCTORS-------------------------
	//--------------------------------------------------------------
		public ChauffageCapteurInboundPort(String uri, ComponentI owner) throws Exception {
			super(uri, ChauffageToCapteurChaleurI.class, owner);
		}
		
		public ChauffageCapteurInboundPort(ComponentI owner) throws Exception {
			super(ChauffageToCapteurChaleurI.class, owner);
		}

		@Override
		public double sendHeating() throws Exception {
			return this.getOwner().handleRequestSync(
					owner -> ((Chauffage)owner).sendHeating()) ;	
		}

	
}
