package ports;

import components.Charger;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ChargerElectricMeterI;
import interfaces.ChargerI;

public class ChargerInboundPort extends AbstractInboundPort implements ChargerI,ChargerElectricMeterI{

	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public ChargerInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ChargerI.class, owner);
	}
	
	public ChargerInboundPort(ComponentI owner) throws Exception {
		super(ChargerI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	@Override
	public void startCharger() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Charger)this.getServiceOwner()).startCharger();
						return null;
					}
				}) ;
	}

	@Override
	public void stopCharger() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Charger)this.getServiceOwner()).stopCharger();
						return null;
					}
				}) ;
		
	}

	@Override
	public double sendConsumption() throws Exception {
		return this.getOwner().handleRequestSync(
				owner -> ((Charger)owner).sendConsumption()) ;	
	}
}
