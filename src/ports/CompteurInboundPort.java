package ports;

import components.Compteur;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.CompteurI;

public class CompteurInboundPort extends AbstractInboundPort implements CompteurI{

	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public CompteurInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, CompteurI.class, owner);
	}

	public CompteurInboundPort(ComponentI owner) throws Exception {
		super(CompteurI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	public void startCompteur() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Compteur)this.getServiceOwner()).startCompteur();
						return null;
					}
				}) ;
	}

	@Override
	public void stopCompteur() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Compteur)this.getServiceOwner()).stopCompteur();
						return null;
					}
				}) ;
	}
}