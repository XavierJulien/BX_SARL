package ports.windturbine;

import components.ElectricMeter;
import components.WindTurbine;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.windturbine.WindTurbineI;

public class WindTurbineInboundPort extends AbstractInboundPort implements WindTurbineI{

	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public WindTurbineInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, WindTurbineI.class, owner);
	}
	
	public WindTurbineInboundPort(ComponentI owner) throws Exception {
		super(WindTurbineI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	public void startWindTurbine() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((WindTurbine)this.getServiceOwner()).startWindTurbine();
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
						((WindTurbine)this.getServiceOwner()).stopWindTurbine();
						return null;
					}
				}) ;
	}

	@Override
	public void sendProduction(double production) throws Exception {
		//unused
	}

	@Override
	public void getWindSpeed(double speed) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((WindTurbine)this.getServiceOwner()).getWindSpeed(speed);
						return null;
					}
				}) ;
	}
}
