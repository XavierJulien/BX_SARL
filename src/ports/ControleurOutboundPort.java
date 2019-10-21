package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ControleurI;

public class ControleurOutboundPort extends AbstractOutboundPort implements	ControleurI {
	
	private static final long serialVersionUID = 1L;

	public ControleurOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ControleurI.class, owner);
	}
	
	public ControleurOutboundPort(ComponentI owner) throws Exception {
		super(ControleurI.class, owner);
	}

	@Override
	public void startEolienne() throws Exception {
		((ControleurI)this.connector).startEolienne() ;
	}

	@Override
	public void stopEolienne() throws Exception {
		((ControleurI)this.connector).stopEolienne() ;
	}

	@Override
	public double getProd() throws Exception {
		System.out.println("getprod controleur outbound");
		return ((ControleurI)this.connector).getProd();
		
	}

	@Override
	public double getVent() throws Exception {
		return ((ControleurI)this.connector).getVent();
	}
	
	
	
	
	
}
