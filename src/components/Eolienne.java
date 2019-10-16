package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import interfaces.EolienneI;
import launcher.CVM;
import ports.EolienneInboundPort;
import ports.EolienneOutboundPort;

public class Eolienne extends AbstractComponent implements EolienneI{

	/** URI of the component (eolienne).*/
	protected final String				uri ;
	/** The inbound port URI for the eolienne.*/
	protected final String				eolienneInboundPortURI ;	
	/** The outbound port URI for the eolienne.*/
	protected final String				eolienneOutboundPortURI ;
	/** outbound port for the component (eolienne).*/
	protected EolienneOutboundPort	eolienneOutboundPort ;
	/** inbound port for the component (eolienne).*/
	protected EolienneInboundPort	eolienneInboundPort ;

	protected Eolienne(String uri,String eolienneOutboundPortURI,String eolienneInboundPortURI) throws Exception{
		super(uri, 1, 1);

		//check arguments 
		assert uri != null;
		assert eolienneOutboundPortURI != null;
		assert eolienneOutboundPortURI != null;

		// init variables 
		this.uri = uri;
		this.eolienneInboundPortURI = eolienneInboundPortURI;
		this.eolienneOutboundPortURI = eolienneOutboundPortURI;
		
		// The  interfaces and ports.
		//ici on n'est producteur du coup on garde que les OfferedI
		this.addOfferedInterface(EolienneI.class) ;
		this.addRequiredInterface(EolienneI.class) ;
		
		
		// init des ports avec les uri et de la donnée qui circule
		this.eolienneInboundPort = new EolienneInboundPort(this.eolienneInboundPortURI, this) ;
		this.eolienneOutboundPort = new EolienneOutboundPort(this.eolienneOutboundPortURI, this) ;
		
		//publish des ports (voir avec le prof ce que ça fait)
		this.eolienneOutboundPort.publishPort() ;
		this.eolienneInboundPort.publishPort() ;
		
		this.tracer.setTitle(uri) ;
		if (uri.equals(CVM.EOLIENNE_COMPONENT_URI)) {
			this.tracer.setRelativePosition(1, 0) ;
		} else {
			this.tracer.setRelativePosition(1, 1) ;
		}
	}
	
	
	// ------------------------------------------------------------------------
	// Services
	// ------------------------------------------------------------------------

	@Override
	public void startEolienne() {
		this.logMessage("Eolienne "+this.uri+" : start.") ;
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							((Eolienne)this.getTaskOwner()).startEolienne();
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				},
				1000, TimeUnit.MILLISECONDS) ;
	}

	@Override
	public void stopEolienne() {
		this.logMessage("Eolienne "+this.uri+" : stop.") ;
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							((Eolienne)this.getTaskOwner()).stopEolienne();
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				},
				1000, TimeUnit.MILLISECONDS) ;
	}
}
