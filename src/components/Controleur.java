package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import interfaces.ControleurI;
import launcher.CVM;
import ports.ControleurInboundPort;
import ports.ControleurOutboundPort;

public class Controleur extends AbstractComponent implements ControleurI {

	
	
	/** URI of the component (controleur).*/
	protected final String				uri ;
	/** The  inbound port URI for the controleur.*/
	protected final String				controleurInboundPortURI ;	
	/** The  outbound port URI for the controleur.*/
	protected final String				controleurOutboundPortURI ;
	/**  outbound port for the component (controleur).*/
	protected ControleurOutboundPort	controleurOutboundPort ;
	/**  inbound port for the component (controleur).*/
	protected ControleurInboundPort	controleurInboundPort ;

	
	protected Controleur(String uri,String controleurOutboundPortURI,String controleurInboundPortURI) throws Exception{
		super(uri, 1, 1);

		//check arguments 
		assert uri != null;
		assert controleurOutboundPortURI != null;
		assert controleurOutboundPortURI != null;

		// init variables 
		this.uri = uri;
		this.controleurInboundPortURI = controleurInboundPortURI;
		this.controleurOutboundPortURI = controleurOutboundPortURI;
		
		// The  interfaces and ports.
		//ici on n'est producteur du coup on garde que les OfferedI
		this.addOfferedInterface(ControleurI.class) ;
		this.addRequiredInterface(ControleurI.class) ;
		
		// init des ports avec les uri et de la donnée qui circule
		this.controleurInboundPort = new ControleurInboundPort(this.controleurInboundPortURI, this) ;
		this.controleurOutboundPort = new ControleurOutboundPort(this.controleurOutboundPortURI, this) ;
		
		//publish des ports (voir avec le prof ce que ça fait)
		this.controleurOutboundPort.publishPort() ;
		this.controleurInboundPort.publishPort() ;
		
		this.tracer.setTitle(uri) ;
		if (uri.equals(CVM.CONTROLEUR_COMPONENT_URI)) {
			this.tracer.setRelativePosition(1, 0) ;
		} else {
			this.tracer.setRelativePosition(1, 1) ;
		}
	}

	@Override
	public void startEolienne() {
		this.logMessage("Controleur "+this.uri+" : tell eolienne to start.") ;
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							((Controleur)this.getTaskOwner()).startEolienne();
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				},
				1000, TimeUnit.MILLISECONDS) ;
	}

	@Override
	public void stopEolienne() {
		this.logMessage("Controleur "+this.uri+" : tell eolienne to stop.") ;
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							((Controleur)this.getTaskOwner()).stopEolienne();
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				},
				1000, TimeUnit.MILLISECONDS) ;
	}
}
