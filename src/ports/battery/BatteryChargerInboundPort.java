package ports.battery;

import components.Battery;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.battery.BatteryChargerI;
import interfaces.sensors.TemperatureSensorHeatingI;

public class BatteryChargerInboundPort extends AbstractInboundPort implements BatteryChargerI{

	private static final long serialVersionUID = 1L;

	//--------------------------------------------------------------
	//-------------------------CONSTRUCTORS-------------------------
	//--------------------------------------------------------------
		public BatteryChargerInboundPort(String uri, ComponentI owner) throws Exception {
			super(uri, TemperatureSensorHeatingI.class, owner);
		}
		
		public BatteryChargerInboundPort(ComponentI owner) throws Exception {
			super(BatteryChargerI.class, owner);
		}

	//--------------------------------------------------------------
	//-------------------------SERVICES-----------------------------
	//--------------------------------------------------------------
		
		@Override
		public void receivePower(double power) throws Exception {
			this.getOwner().handleRequestSync(
					owner -> ((Battery)owner).sendEnergy()) ;
		}
}
