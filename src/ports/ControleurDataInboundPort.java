package ports;

import components.Controleur;
import components.Eolienne;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.ports.AbstractDataInboundPort;

public class ControleurDataInboundPort 
extends		AbstractDataInboundPort{
	
	
	private static final long serialVersionUID = 1L;

	public ControleurDataInboundPort(
			String uri, Class<?> implementedPullInterface, 
			Class<?> implementedPushInterface,
			ComponentI owner) throws Exception {
		super(uri, implementedPullInterface, implementedPushInterface, owner);
	}

	public ControleurDataInboundPort(String uri,ComponentI owner) throws Exception{
		super(uri,DataOfferedI.PullI.class,DataOfferedI.PushI.class,owner);
	}

	public ControleurDataInboundPort(ComponentI owner) throws Exception{
		super(DataOfferedI.PullI.class,DataOfferedI.PushI.class, owner);
	}

	/**
	 * @see fr.sorbonne_u.components.interfaces.DataOfferedI.PullI#get()
	 */
	@Override
	public DataOfferedI.DataI get() throws Exception
	{
		return this.owner.handleRequestSync(
				new AbstractComponent.AbstractService<DataOfferedI.DataI>() {
					@Override
					public DataOfferedI.DataI call() throws Exception {
						return ((Controleur)this.getServiceOwner()).getEolienneD() ;
					}
				}) ;
	}
	
	

}
