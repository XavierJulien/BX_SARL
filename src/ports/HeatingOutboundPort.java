package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.HeatingI;

public class HeatingOutboundPort extends AbstractOutboundPort implements HeatingI{
	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public HeatingOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, HeatingI.class, owner);
	}
	
	public HeatingOutboundPort(ComponentI owner) throws Exception {
		super(HeatingI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	@Override
	public void startHeating() throws Exception {
		((HeatingI)this.connector).startHeating() ;
	}

	@Override
	public void stopHeating() throws Exception {
		((HeatingI)this.connector).stopHeating() ;
	}

	@Override
	public void putExtraPowerInHeating(int power) throws Exception {
		//Shouldn't be used
	}

	@Override
	public void slowHeating(int power) throws Exception {
		//Shouldn't be used
	}
	
}
