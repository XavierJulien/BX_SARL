package ports;

import components.Controleur;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ControleurI;

public class ControleurInboundPort extends AbstractInboundPort implements ControleurI{

	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public ControleurInboundPort(String uri, ComponentI owner) throws Exception {

		super(uri, ControleurI.class, owner);
	}

	public ControleurInboundPort(ComponentI owner) throws Exception {
		super(ControleurI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	
	//---------------------------------------------------
	//--------------------EOLIENNE-----------------------
	//---------------------------------------------------
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

	//---------------------------------------------------
	//--------------------BOUILLOIRE---------------------
	//---------------------------------------------------
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
	
	//---------------------------------------------------
	//--------------------CHAUFFAGE----------------------
	//---------------------------------------------------
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

	//---------------------------------------------------
	//--------------------COMPTEUR-----------------------
	//---------------------------------------------------
	@Override
	public void startCompteur() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controleur)this.getServiceOwner()).startCompteur();
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
						((Controleur)this.getServiceOwner()).stopCompteur();
						return null;
					}
				}) ;		
	}

	//---------------------------------------------------
	//---------------------CHARGEUR----------------------
	//---------------------------------------------------
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

	//---------------------------------------------------
	//---------------------BATTERIE----------------------
	//---------------------------------------------------
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
	
	
	//---------------------------------------------------
	//--------------------CAPTEUR------------------------
	//---------------------------------------------------
	@Override
	public double getVent() throws Exception {
		return 0;
	}
	@Override
	public double getChaleur() throws Exception {
		return 0;
	}
}
