package bcm.ports.sensors;

import bcm.interfaces.sensors.TemperatureSensorHeatingI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class TemperatureSensorHeatingOutboundPort  extends AbstractOutboundPort implements TemperatureSensorHeatingI{

	private static final long serialVersionUID = 1L;

	//--------------------------------------------------------------
	//-------------------------CONSTRUCTORS-------------------------
	//--------------------------------------------------------------
		public TemperatureSensorHeatingOutboundPort(String uri, ComponentI owner) throws Exception {
			super(uri, TemperatureSensorHeatingI.class, owner);
		}
		
		public TemperatureSensorHeatingOutboundPort(ComponentI owner) throws Exception {
			super(TemperatureSensorHeatingI.class, owner);
		}

		

	//--------------------------------------------------------------
	//-------------------------SERVICES-----------------------------
	//--------------------------------------------------------------
		
		
		@Override
		public double getHeating() throws Exception {
			return ((TemperatureSensorHeatingI)this.connector).getHeating();
		}
	
}
