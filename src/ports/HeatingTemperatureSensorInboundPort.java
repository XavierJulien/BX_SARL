package ports;

import components.Heating;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.HeatingTemperatureSensorI;

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
