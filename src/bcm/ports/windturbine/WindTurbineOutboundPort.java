package bcm.ports.windturbine;

import bcm.interfaces.windturbine.WindTurbineI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class WindTurbineOutboundPort extends AbstractOutboundPort implements WindTurbineI {
	
	private static final long serialVersionUID = 1L;
	
//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public WindTurbineOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, WindTurbineI.class, owner);
	}
	
	public WindTurbineOutboundPort(ComponentI owner) throws Exception {
		super(WindTurbineI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	@Override
	public void startWindTurbine() throws Exception {
		((WindTurbineI)this.connector).startWindTurbine() ;
	}

	@Override
	public void stopWindTurbine() throws Exception {
		((WindTurbineI)this.connector).stopWindTurbine() ;
	}

	@Override
	public void sendProduction(double production) throws Exception {
		((WindTurbineI)this.connector).sendProduction(production) ;
	}

	@Override
	public void getWindSpeed(double speed) throws Exception {
		//unused
		
	}
}
