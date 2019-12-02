package ports;

import components.Heating;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.HeatingElectricMeterI;
import interfaces.HeatingI;

public class HeatingInboundPort extends AbstractInboundPort implements HeatingI,HeatingElectricMeterI{

	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public HeatingInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, HeatingI.class, owner);
	}

	public HeatingInboundPort(ComponentI owner) throws Exception {
		super(HeatingI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	public void startHeating() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Heating)this.getServiceOwner()).startHeating();
						return null;
					}
				}) ;
	}

	@Override
	public void stopHeating() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Heating)this.getServiceOwner()).stopHeating();
						return null;
					}
				}) ;
	}

	@Override
	public double sendConsumption() throws Exception {
		return this.getOwner().handleRequestSync(
				owner -> ((Heating)owner).sendConsumption()) ;	
	}

	@Override
	public void putExtraPowerInHeating(int power) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Heating)this.getServiceOwner()).putExtraPowerInHeating(power);
						return null;
					}
				}) ;
	}

}