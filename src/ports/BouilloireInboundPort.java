package ports;

import components.Bouilloire;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.BouilloireI;

public class BouilloireInboundPort extends AbstractInboundPort implements BouilloireI{
	
	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public BouilloireInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, BouilloireI.class, owner);
	}
	
	public BouilloireInboundPort(ComponentI owner) throws Exception {
		super(BouilloireI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	public void startBouilloire() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Bouilloire)this.getServiceOwner()).startBouilloire();
						return null;
					}
				}) ;
	}

	@Override
	public void stopBouilloire() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Bouilloire)this.getServiceOwner()).stopBouilloire();
						return null;
					}
				}) ;
	}
}