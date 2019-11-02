package ports;

import components.Batterie;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.BatterieI;

public class BatterieInboundPort extends AbstractInboundPort implements BatterieI
{

	private static final long serialVersionUID = 1L;

	public BatterieInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, BatterieI.class, owner);
	}
	
	public BatterieInboundPort(ComponentI owner) throws Exception {
		super(BatterieI.class, owner);
	}

	public void startBatterie() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Batterie)this.getServiceOwner()).startBatterie();
						return null;
					}
				}) ;
	}

	@Override
	public void stopBatterie() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Batterie)this.getServiceOwner()).stopBatterie();
						return null;
					}
				}) ;
	}

	@Override
	public double sendChargePercentage() throws Exception {
		return this.getOwner().handleRequestSync(
				owner -> ((Batterie)owner).sendChargePercentage()) ;
		
	}

	@Override
	public double sendEnergy() throws Exception {
		return this.getOwner().handleRequestSync(
				owner -> ((Batterie)owner).sendEnergy()) ;
	}

}
