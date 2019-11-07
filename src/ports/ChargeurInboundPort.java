package ports;

import components.Chargeur;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ChargeurI;

public class ChargeurInboundPort extends AbstractInboundPort implements ChargeurI{

	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public ChargeurInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ChargeurI.class, owner);
	}
	
	public ChargeurInboundPort(ComponentI owner) throws Exception {
		super(ChargeurI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	@Override
	public void startChargeur() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Chargeur)this.getServiceOwner()).startChargeur();
						return null;
					}
				}) ;
	}

	@Override
	public void stopChargeur() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Chargeur)this.getServiceOwner()).stopChargeur();
						return null;
					}
				}) ;
		
	}
}
