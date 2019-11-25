package ports;

import components.Heating;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.HeatingHeatSensorI;

public class HeatingHeatSensorInboundPort extends AbstractInboundPort implements HeatingHeatSensorI {

	private static final long serialVersionUID = 1L;

	//--------------------------------------------------------------
	//-------------------------CONSTRUCTORS-------------------------
	//--------------------------------------------------------------
		public HeatingHeatSensorInboundPort(String uri, ComponentI owner) throws Exception {
			super(uri, HeatingHeatSensorI.class, owner);
		}
		
		public HeatingHeatSensorInboundPort(ComponentI owner) throws Exception {
			super(HeatingHeatSensorI.class, owner);
		}

		@Override
		public double sendHeating() throws Exception {
			return this.getOwner().handleRequestSync(
					owner -> ((Heating)owner).sendHeating()) ;	
		}

	
}
