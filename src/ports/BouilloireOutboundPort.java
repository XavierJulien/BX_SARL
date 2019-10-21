package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.BouilloireI;

public class BouilloireOutboundPort extends AbstractOutboundPort implements BouilloireI{
	
	private static final long serialVersionUID = 1L;

	public BouilloireOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, BouilloireI.class, owner);
	}
	
	public BouilloireOutboundPort(ComponentI owner) throws Exception {
		super(BouilloireI.class, owner);
	}

	@Override
	public void startBouilloire() throws Exception {
		((BouilloireI)this.connector).startBouilloire() ;
	}

	@Override
	public void stopBouilloire() throws Exception {
		((BouilloireI)this.connector).stopBouilloire() ;
	}


}
