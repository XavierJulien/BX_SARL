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

	@Override
	public void startBouilloire() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controleur)this.getServiceOwner()).startBouilloire();
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
						((Controleur)this.getServiceOwner()).stopBouilloire();
						return null;
					}
				}) ;		
	}

	@Override
	public void startChauffage() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controleur)this.getServiceOwner()).startChauffage();
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
						((Controleur)this.getServiceOwner()).stopChauffage();
						return null;
					}
				}) ;		
	}

	@Override
	public void startChargeur() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controleur)this.getServiceOwner()).startChargeur();
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
						((Controleur)this.getServiceOwner()).stopChargeur();
						return null;
					}
				}) ;		
	}

	@Override
	public void startBatterie() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controleur)this.getServiceOwner()).startBatterie();
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
						((Controleur)this.getServiceOwner()).stopBatterie();
						return null;
					}
				}) ;		
	}

	@Override
	public double getBatteryChargePercentage() throws Exception {
		// shouldn't be used
		return 0;
	}

	@Override
	public double getBatteryProduction() throws Exception {
		// shouldn't be used
		return 0;
	}
}
