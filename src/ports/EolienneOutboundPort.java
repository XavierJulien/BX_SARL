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
import interfaces.EolienneI;

public class EolienneOutboundPort extends AbstractOutboundPort implements EolienneI {
	
	private static final long serialVersionUID = 1L;

	public EolienneOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, EolienneI.class, owner);
	}
	
	public EolienneOutboundPort(ComponentI owner) throws Exception {
		super(EolienneI.class, owner);
	}

	@Override
	public void startEolienne() throws Exception {
		((EolienneI)this.connector).startEolienne() ;
	}

	@Override
	public void stopEolienne() throws Exception {
		((EolienneI)this.connector).stopEolienne() ;
	}

	@Override
	public double sendProduction() throws Exception {
		return 0;
		
	}
	
	
	
}
