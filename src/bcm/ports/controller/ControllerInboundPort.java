package bcm.ports.controller;

import bcm.components.Controller;
import bcm.interfaces.controller.ControllerI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

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
		//unused
	}
	@Override
	public void stopWindTurbine() throws Exception {
		//unused
	}
	
	
	@Override
	public void getProduction(double production) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controller)this.getServiceOwner()).getProduction(production);
						return null;
					}
				}) ;
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
	
	@Override
	public void slowHeating(int power) throws Exception {
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
	public void getAllConsumption(double total) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controller)this.getServiceOwner()).getAllConsumption(total);
						return null;
					}
				}) ;	
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
	public void getBatteryChargePercentage(double percentage) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controller)this.getServiceOwner()).getBatteryChargePercentage(percentage);
						return null;
					}
				}) ;
	}
	@Override
	public void getBatteryProduction(double energy) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controller)this.getServiceOwner()).getBatteryProduction(energy);
						return null;
					}
				}) ;
	}
	
	
	//---------------------------------------------------
	//--------------------CAPTEUR------------------------
	//---------------------------------------------------
	@Override
	public void getWindSpeed(double speed) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controller)this.getServiceOwner()).getWindSpeed(speed);
						return null;
					}
				}) ;
	}
	
	@Override
	public void getTemperature(double temperature) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Controller)this.getServiceOwner()).getTemperature(temperature);
						return null;
					}
				}) ;
	}


	
}
