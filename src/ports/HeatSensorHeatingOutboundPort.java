package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.HeatSensorHeatingI;

public class HeatSensorHeatingOutboundPort  extends AbstractOutboundPort implements HeatSensorHeatingI{

	private static final long serialVersionUID = 1L;

	//--------------------------------------------------------------
	//-------------------------CONSTRUCTORS-------------------------
	//--------------------------------------------------------------
		public HeatSensorHeatingOutboundPort(String uri, ComponentI owner) throws Exception {
			super(uri, HeatSensorHeatingI.class, owner);
		}
		
		public HeatSensorHeatingOutboundPort(ComponentI owner) throws Exception {
			super(HeatSensorHeatingI.class, owner);
		}

		

	//--------------------------------------------------------------
	//-------------------------SERVICES-----------------------------
	//--------------------------------------------------------------
		
		
		@Override
		public double getHeating() throws Exception {
			return ((HeatSensorHeatingI)this.connector).getHeating();
		}
	
}
