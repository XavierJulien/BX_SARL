package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ChargeurI;

public class ChargeurOutboundPort extends AbstractOutboundPort implements ChargeurI{

	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public ChargeurOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri,ChargeurI.class, owner);
	}
	
	public ChargeurOutboundPort(ComponentI owner) throws Exception {
		super(ChargeurI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	@Override
	public void startChargeur() throws Exception {
		((ChargeurI)this.connector).startChargeur() ;
		
	}

	@Override
	public void stopChargeur() throws Exception {
		((ChargeurI)this.connector).stopChargeur() ;		
	}
}
