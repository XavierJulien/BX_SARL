package ports;

import components.Controleur;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ControleurI;

public class ControleurInboundPort extends AbstractInboundPort implements ControleurI{

	private static final long serialVersionUID = 1L;

	public ControleurInboundPort(String uri, ComponentI owner) throws Exception {
		
		super(uri, ControleurI.class, owner);
	}
	
	public ControleurInboundPort(ComponentI owner) throws Exception {
		super(ControleurI.class, owner);
	}
	
	public void startEolienne() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controleur)this.getServiceOwner()).startEolienne();
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
						((Controleur)this.getServiceOwner()).stopEolienne();
						return null;
					}
				}) ;
	}

	@Override
	public double getProd() throws Exception {
		System.out.println("ERREUR");
		return 0;
	}

	@Override
	public double getVent() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
}
