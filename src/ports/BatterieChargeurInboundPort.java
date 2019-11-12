package ports;

import components.Chauffage;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.BatterieToChargeurI;
import interfaces.CapteurChaleurToChauffageI;

public class BatterieChargeurInboundPort extends AbstractInboundPort implements BatterieToChargeurI{

	private static final long serialVersionUID = 1L;

	//--------------------------------------------------------------
	//-------------------------CONSTRUCTORS-------------------------
	//--------------------------------------------------------------
		public BatterieChargeurInboundPort(String uri, ComponentI owner) throws Exception {
			super(uri, CapteurChaleurToChauffageI.class, owner);
		}
		
		public BatterieChargeurInboundPort(ComponentI owner) throws Exception {
			super(BatterieToChargeurI.class, owner);
		}

	//--------------------------------------------------------------
	//-------------------------SERVICES-----------------------------
	//--------------------------------------------------------------
		
		@Override
		public void receivePower(double power) throws Exception {
			this.getOwner().handleRequestSync(
					owner -> ((Batterie)owner).sendHeating()) ;
		}
}
