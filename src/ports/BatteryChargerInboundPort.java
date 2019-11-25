package ports;

import components.Batterie;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.BatterieChargeurI;
import interfaces.CapteurChaleurChauffageI;

public class BatterieChargeurInboundPort extends AbstractInboundPort implements BatterieChargeurI{

	private static final long serialVersionUID = 1L;

	//--------------------------------------------------------------
	//-------------------------CONSTRUCTORS-------------------------
	//--------------------------------------------------------------
		public BatterieChargeurInboundPort(String uri, ComponentI owner) throws Exception {
			super(uri, CapteurChaleurChauffageI.class, owner);
		}
		
		public BatterieChargeurInboundPort(ComponentI owner) throws Exception {
			super(BatterieChargeurI.class, owner);
		}

	//--------------------------------------------------------------
	//-------------------------SERVICES-----------------------------
	//--------------------------------------------------------------
		
		@Override
		public void receivePower(double power) throws Exception {
			this.getOwner().handleRequestSync(
					owner -> ((Batterie)owner).sendEnergy()) ;
		}
}
