package bcm.ports.heating;

import bcm.components.Heating;
import bcm.interfaces.heating.HeatingTemperatureSensorI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class HeatingTemperatureSensorInboundPort extends AbstractInboundPort implements HeatingTemperatureSensorI {

	private static final long serialVersionUID = 1L;

	//--------------------------------------------------------------
	//-------------------------CONSTRUCTORS-------------------------
	//--------------------------------------------------------------
		public HeatingTemperatureSensorInboundPort(String uri, ComponentI owner) throws Exception {
			super(uri, HeatingTemperatureSensorI.class, owner);
		}
		
		public HeatingTemperatureSensorInboundPort(ComponentI owner) throws Exception {
			super(HeatingTemperatureSensorI.class, owner);
		}

		@Override
		public double sendHeating() throws Exception {
			return this.getOwner().handleRequestSync(
					owner -> ((Heating)owner).sendHeating()) ;	
		}

	
}
