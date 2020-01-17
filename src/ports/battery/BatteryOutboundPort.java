package ports.battery;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.battery.BatteryI;

public class BatteryOutboundPort extends AbstractOutboundPort implements BatteryI {
	
	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public BatteryOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, BatteryI.class, owner);
	}
	
	public BatteryOutboundPort(ComponentI owner) throws Exception {
		super(BatteryI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-------------------------
//--------------------------------------------------------------
	@Override
	public void startBattery() throws Exception {
		((BatteryI)this.connector).startBattery() ;
	}
	
	@Override
	public void stopBattery() throws Exception {
		((BatteryI)this.connector).stopBattery() ;
	}
	
	@Override
	public void sendChargePercentage(double percentage) throws Exception {
		((BatteryI)this.connector).sendChargePercentage(percentage) ;
	}

	@Override
	public void sendEnergy(double energy) throws Exception {
		((BatteryI)this.connector).sendEnergy(energy) ;
	}

	@Override
	public void receivePower(double power) throws Exception {
		//unused
		
	}
}