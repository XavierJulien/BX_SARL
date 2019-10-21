package ports;

import components.Eolienne;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.EolienneI;

public class EolienneInboundPort extends AbstractInboundPort implements EolienneI
{

	private static final long serialVersionUID = 1L;

	public EolienneInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, EolienneI.class, owner);
	}
	
	public EolienneInboundPort(ComponentI owner) throws Exception {
		super(EolienneI.class, owner);
	}

	public void startEolienne() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Eolienne)this.getServiceOwner()).startEolienne();
						return null;
					}
				}) ;
	}

	@Override
	public void stopEolienne() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Eolienne)this.getServiceOwner()).stopEolienne();
						return null;
					}
				}) ;
	}

	@Override
	public double sendProduction() throws Exception {
		return this.getOwner().handleRequestSync(
				owner -> ((Eolienne)owner).sendProduction()) ;
		
	}

}
