package ports;

import components.Controller;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ControllerI;

public class ControllerInboundPort extends AbstractInboundPort implements ControllerI{

	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public ControllerInboundPort(String uri, ComponentI owner) throws Exception {

		super(uri, ControllerI.class, owner);
	}

	public ControllerInboundPort(ComponentI owner) throws Exception {
		super(ControllerI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	
	//---------------------------------------------------
	//--------------------EOLIENNE-----------------------
	//---------------------------------------------------
	public void startWindTurbine() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controller)this.getServiceOwner()).startWindTurbine();
						return null;
					}
				}) ;
	}
	@Override
	public void stopWindTurbine() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controller)this.getServiceOwner()).stopWindTurbine();
						return null;
					}
				}) ;
	}
	
	
	@Override
	public double getProduction() throws Exception {
		System.out.println("ERREUR");
		return 0;
	}

	//---------------------------------------------------
	//--------------------BOUILLOIRE---------------------
	//---------------------------------------------------
	@Override
	public void startKettle() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controller)this.getServiceOwner()).startKettle();
						return null;
					}
				}) ;		
	}
	@Override
	public void stopKettle() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controller)this.getServiceOwner()).stopKettle();
						return null;
					}
				}) ;		
	}
	

	
	//---------------------------------------------------
	//--------------------CHAUFFAGE----------------------
	//---------------------------------------------------
	@Override
	public void startHeating() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controller)this.getServiceOwner()).startHeating();
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
						((Controller)this.getServiceOwner()).stopHeating();
						return null;
					}
				}) ;		
	}
	
	@Override
	public void putExtraPowerInHeating(int power) throws Exception {
		//shouldn't be used
	}

	//---------------------------------------------------
	//--------------------COMPTEUR-----------------------
	//---------------------------------------------------
	@Override
	public void startElectricMeter() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controller)this.getServiceOwner()).startElectricMeter();
						return null;
					}
				}) ;

	}
	@Override
	public void stopElectricMeter() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controller)this.getServiceOwner()).stopElectricMeter();
						return null;
					}
				}) ;		
	}
	
	@Override
	public double getAllConsumption() throws Exception {
		System.out.println("ERREUR");
		return 0;	
	}
	
	//---------------------------------------------------
	//---------------------CHARGEUR----------------------
	//---------------------------------------------------
	@Override
	public void startCharger() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controller)this.getServiceOwner()).startCharger();
						return null;
					}
				}) ;
	}
	@Override
	public void stopCharger() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controller)this.getServiceOwner()).stopCharger();
						return null;
					}
				}) ;		
	}

	//---------------------------------------------------
	//---------------------BATTERIE----------------------
	//---------------------------------------------------
	@Override
	public void startBattery() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controller)this.getServiceOwner()).startBattery();
						return null;
					}
				}) ;
	}
	@Override
	public void stopBattery() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controller)this.getServiceOwner()).stopBattery();
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
	public double getWind() throws Exception {
		return 0;
	}
	@Override
	public double getTemperature() throws Exception {
		return 0;
	}

	
}
