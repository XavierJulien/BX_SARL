package bcm.ports.battery;

import bcm.components.Battery;
import bcm.interfaces.battery.BatteryI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class BatteryInboundPort extends AbstractInboundPort implements BatteryI{

	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public BatteryInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, BatteryI.class, owner);
	}
	
	public BatteryInboundPort(ComponentI owner) throws Exception {
		super(BatteryI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	public void startBattery() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Battery)this.getServiceOwner()).startBattery();
						return null;
					}
				}) ;
	}

	@Override
	public void stopBattery() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Battery)this.getServiceOwner()).stopBattery();
						return null;
					}
				}) ;
	}

	@Override
	public void sendChargePercentage(double energy) throws Exception {
		//unused
	}

	@Override
	public void sendEnergy(double energy) throws Exception {
		//unused
	}

	@Override
	public void receivePower(double power) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Battery)this.getServiceOwner()).receivePower(power);
						return null;
					}
				}) ;
	}
}
