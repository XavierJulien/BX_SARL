package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.CompteurControleurI;
import interfaces.CompteurI;

public class CompteurOutboundPort extends AbstractOutboundPort implements CompteurI{
	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public CompteurOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, CompteurI.class, owner);
	}
	
	public CompteurOutboundPort(ComponentI owner) throws Exception {
		super(CompteurI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	
	@Override
	public void startCompteur() throws Exception {
		((CompteurI)this.connector).startCompteur();		

	}

	@Override
	public void stopCompteur() throws Exception {
		((CompteurI)this.connector).stopCompteur();		

	}

	@Override
	public double sendAllConso() throws Exception {
		return ((CompteurI)this.connector).sendAllConso();		
	}

	@Override
	public double getChauffageConso() throws Exception {
		return ((CompteurI)this.connector).getChauffageConso();		
	}

	@Override
	public double getBouilloireConso() throws Exception {
		return ((CompteurI)this.connector).getBouilloireConso();	
	}

	@Override
	public double getChargeurConso() throws Exception {
		return ((CompteurI)this.connector).getChargeurConso();	
	}

}
