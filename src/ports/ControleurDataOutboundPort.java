package ports;

import components.Controleur;
import components.Eolienne;
import data.EolienneD;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;
import fr.sorbonne_u.components.ports.AbstractDataOutboundPort;

public class ControleurDataOutboundPort extends AbstractDataOutboundPort{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ControleurDataOutboundPort(String uri,ComponentI owner) throws Exception {
		super(uri,DataRequiredI.PullI.class,DataRequiredI.PushI.class,owner);
	}

	public ControleurDataOutboundPort(ComponentI owner) throws Exception
	{
		super(DataRequiredI.PullI.class,DataRequiredI.PushI.class, owner);
	}

	/**
	 * @see fr.sorbonne_u.components.interfaces.DataRequiredI.PushI#receive(fr.sorbonne_u.components.interfaces.DataRequiredI.DataI)
	 */
	@Override
	public void	receive(DataRequiredI.DataI d) throws Exception{
		this.owner.handleRequestAsync(new AbstractComponent.AbstractService<Void>() {
			@Override
			public Void call() throws Exception {
				((/*Eolienne ?*/Controleur)this.getServiceOwner()).shutdownEolienne((EolienneD) d) ;
				return null;
			}
		}) ;
	}
	
	

}
