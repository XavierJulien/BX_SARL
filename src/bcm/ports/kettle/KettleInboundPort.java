package bcm.ports.kettle;

import bcm.components.Kettle;
import bcm.interfaces.kettle.KettleElectricMeterI;
import bcm.interfaces.kettle.KettleI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class KettleInboundPort extends AbstractInboundPort implements KettleI,KettleElectricMeterI{
	
	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public KettleInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, KettleI.class, owner);
	}
	
	public KettleInboundPort(ComponentI owner) throws Exception {
		super(KettleI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	public void startKettle() throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Kettle)this.getServiceOwner()).startKettle();
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
						((Kettle)this.getServiceOwner()).stopKettle();
						return null;
					}
				}) ;
	}

	@Override
	public void sendConsumption(double consumption) throws Exception {
		//unused
	}
}