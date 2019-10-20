package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.connectors.ConnectorI;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;
import fr.sorbonne_u.components.exceptions.PostconditionException;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.components.ports.PortI;
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
	public void getProd(double prod) throws Exception {
		
	}
	
	
	
	
	
}
