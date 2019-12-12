package ports.charger;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.charger.ChargerI;

public class ChargerOutboundPort extends AbstractOutboundPort implements ChargerI{

	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public ChargerOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri,ChargerI.class, owner);
	}
	
	public ChargerOutboundPort(ComponentI owner) throws Exception {
		super(ChargerI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	@Override
	public void startCharger() throws Exception {
		((ChargerI)this.connector).startCharger() ;
		
	}

	@Override
	public void stopCharger() throws Exception {
		((ChargerI)this.connector).stopCharger() ;		
	}

	@Override
	public double sendConsumption() throws Exception {
		//shouldn't be used
		return 0;
	}
}
