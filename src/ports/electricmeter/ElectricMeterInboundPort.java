package ports.electricmeter;

import components.ElectricMeter;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.electricmeter.ElectricMeterI;

public class ElectricMeterInboundPort extends AbstractInboundPort implements ElectricMeterI{

	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public ElectricMeterInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ElectricMeterI.class, owner);
	}

	public ElectricMeterInboundPort(ComponentI owner) throws Exception {
		super(ElectricMeterI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	public void startElectricMeter() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((ElectricMeter)this.getServiceOwner()).startElectricMeter();
						return null;
					}
				}) ;
	}

	@Override
	public void stopElectricMeter() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((ElectricMeter)this.getServiceOwner()).stopElectricMeter();
						return null;
					}
				}) ;
	}

	@Override
	public void sendAllConsumption(double total) throws Exception {
		//unused
	}

	@Override
	public void getHeatingConsumption(double consumption) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((ElectricMeter)this.getServiceOwner()).getHeatingConsumption(consumption);
						return null;
					}
				}) ;
	}

	@Override
	public void getKettleConsumption(double consumption) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((ElectricMeter)this.getServiceOwner()).getKettleConsumption(consumption);
						return null;
					}
				}) ;
	}

	@Override
	public void getChargerConsumption(double consumption) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((ElectricMeter)this.getServiceOwner()).getChargerConsumption(consumption);
						return null;
					}
				}) ;
	}

}