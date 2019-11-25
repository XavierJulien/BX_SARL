package ports;

import components.ElectricMeter;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ElectricMeterI;

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
	public double sendAllConsumption() throws Exception {
		return this.getOwner().handleRequestSync(
				owner -> ((ElectricMeter)owner).sendAllConsumption()) ;	
	}

	@Override
	public double getHeatingConsumption() throws Exception {
		System.out.println("ERREUR");
		return 0;
	}

	@Override
	public double getKettleConsumption() throws Exception {
		System.out.println("ERREUR");
		return 0;
	}

	@Override
	public double getChargerConsumption() throws Exception {
		System.out.println("ERREUR");
		return 0;
	}

}