package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.examples.basic_cs.components.URIConsumer;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.PortI;
import interfaces.ControleurI;
import launcher.CVM;
import ports.ControleurInboundPort;
import ports.ControleurOutboundPort;
import ports.EolienneOutboundPort;

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
		
		PortI p = new ControleurInboundPort(controleurInboundPortURI, this) ;
		
		// publish the port
		p.publishPort() ;
		
		
		
		this.controleurInboundPortURI = controleurInboundPortURI;
		this.controleurOutboundPortURI = controleurOutboundPortURI;
		
		
		// The  interfaces and ports.
		//ici on n'est producteur du coup on garde que les OfferedI
//		this.addOfferedInterface(ControleurI.class) ;
//		this.addRequiredInterface(ControleurI.class) ;
		
		this.controleurOutboundPort =
				new ControleurOutboundPort(controleurOutboundPortURI, this) ;
			// publish the port (an outbound port is always local)
			this.controleurOutboundPort.localPublishPort() ;
		
		// init des ports avec les uri et de la donnée qui circule
//		this.controleurInboundPort = new ControleurInboundPort(this.controleurInboundPortURI, this) ;
//		this.controleurOutboundPort = new ControleurOutboundPort(this.controleurOutboundPortURI, this) ;
		
		//publish des ports (voir avec le prof ce que ça fait)
//		this.controleurOutboundPort.publishPort() ;
//		this.controleurInboundPort.publishPort() ;
//		
			
			if (AbstractCVM.isDistributed) {
				this.executionLog.setDirectory(System.getProperty("user.dir")) ;
			} else {
				this.executionLog.setDirectory(System.getProperty("user.home")) ;
			}
			
		this.tracer.setTitle("Controleur") ;
		this.tracer.setRelativePosition(1, 1) ;
	}

	@Override
	public void startEolienne() throws Exception {
		this.logMessage("Controleur "+this.uri+" : tell eolienne to start.") ;
		/*this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							System.out.println((Controleur)this.getTaskOwner());
							((Controleur)this.getTaskOwner()).startEolienne();
							
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				},
				1000, TimeUnit.MILLISECONDS) ;*/
		this.controleurOutboundPort.startEolienne();
	}

	@Override
	public void stopEolienne() {
		this.logMessage("Controleur "+this.uri+" : tell eolienne to stop.") ;
		/*this.scheduleTask(
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
				1000, TimeUnit.MILLISECONDS) ;*/
	}
	
	@Override
	public void			start() throws ComponentStartException
	{
		
		super.start() ;
		this.logMessage("starting Controleur component.") ;
		// Schedule the first service method invocation in one second.
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
			1000, TimeUnit.MILLISECONDS);
	}

	@Override
	public void getProd(double prod) throws Exception {
		this.logMessage("The controleur is getting "+prod+" units of energy from the eolienne") ;
//		this.scheduleTask(
//				new AbstractComponent.AbstractTask() {
//					@Override
//					public void run() {
//						try {
//							((Controleur)this.getTaskOwner()).getProd(prod);
//						} catch (Exception e) {
//							throw new RuntimeException(e) ;
//						}
//					}
//				},
//				1000, TimeUnit.MILLISECONDS) ;
		
	}
	
	
	
	
	
	
	
}
