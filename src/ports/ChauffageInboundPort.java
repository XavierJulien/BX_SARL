package ports;

import components.Bouilloire;
import components.Chauffage;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ChauffageI;

public class ChauffageInboundPort extends AbstractInboundPort implements ChauffageI{

	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public ChauffageInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ChauffageI.class, owner);
	}

	public ChauffageInboundPort(ComponentI owner) throws Exception {
		super(ChauffageI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	public void startChauffage() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Chauffage)this.getServiceOwner()).startChauffage();
						return null;
					}
				}) ;
	}

	@Override
	public void stopChauffage() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Chauffage)this.getServiceOwner()).stopChauffage();
						return null;
					}
				}) ;
	}

	@Override
	public double sendConso() throws Exception {
		return this.getOwner().handleRequestSync(
				owner -> ((Chauffage)owner).sendConso()) ;	
	}
}