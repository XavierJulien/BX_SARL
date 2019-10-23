package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.PortI;
import interfaces.BouilloireI;
import launcher.CVM;
import ports.BouilloireInboundPort;
import ports.BouilloireOutboundPort;

@RequiredInterfaces(required = {BouilloireI.class})
@OfferedInterfaces(offered = {BouilloireI.class})
public class Bouilloire extends AbstractComponent{
	/** URI of the component (bouilloire).*/
	protected final String				uri ;
	/** The inbound port URI for the bouilloire.*/
	protected final String				bouilloireInboundPortURI ;	
	/** The outbound port URI for the bouilloire.*/
	protected final String				bouilloireOutboundPortURI ;
	/** outbound port for the component (bouilloire).*/
	protected BouilloireOutboundPort	bouilloireOutboundPort ;
	/** inbound port for the component (bouilloire).*/
	protected BouilloireInboundPort	bouilloireInboundPort ;
	
	
	/** utility vars */
	protected double prod;
	protected boolean isOn=false;

	protected Bouilloire(String uri,String bouilloireOutboundPortURI,String bouilloireInboundPortURI) throws Exception{
		super(uri, 1, 1);

		//check arguments 
		assert uri != null;
		assert bouilloireOutboundPortURI != null;
		assert bouilloireOutboundPortURI != null;

		// init variables 
		this.uri = uri;
		
		PortI p = new BouilloireInboundPort(bouilloireInboundPortURI, this) ;
		
		// publish the port
		p.publishPort() ;
		
		
		this.bouilloireInboundPortURI = bouilloireInboundPortURI;
		this.bouilloireOutboundPortURI = bouilloireOutboundPortURI;
		
		this.bouilloireOutboundPort =
				new BouilloireOutboundPort(bouilloireOutboundPortURI, this) ;
			// publish the port (an outbound port is always local)
			this.bouilloireOutboundPort.localPublishPort() ;
	
//		
//		// The  interfaces and ports.
//		//ici on n'est producteur du coup on garde que les OfferedI
//		this.addOfferedInterface(BouilloireI.class) ;
//		this.addRequiredInterface(BouilloireI.class) ;
//		
//		
//		// init des ports avec les uri et de la donnée qui circule
//		this.bouilloireInboundPortURI = new bouilloireInboundPortURI(this.bouilloireInboundPortURIURI, this) ;
//		this.bouilloireOutboundPort = new BouilloireOutboundPort(this.bouilloireOutboundPortURI, this) ;
//		
//		//publish des ports (voir avec le prof ce que ça fait)
//		this.bouilloireOutboundPort.publishPort() ;
//		this.bouilloireInboundPortURI.publishPort() ;
//		
			if (AbstractCVM.isDistributed) {
				this.executionLog.setDirectory(System.getProperty("user.dir")) ;
			} else {
				this.executionLog.setDirectory(System.getProperty("user.home")) ;
			}	
			
		this.tracer.setTitle(uri) ;
		if (uri.equals(CVM.BOUILLOIRE_COMPONENT_URI)) {
			this.tracer.setRelativePosition(1, 2) ;
		} else {
			this.tracer.setRelativePosition(1, 0) ;
		}
		
		
		this.prod = 0;
		
		
	}
	
	
	// ------------------------------------------------------------------------
	// Services
	// ------------------------------------------------------------------------

	public void startBouilloire() throws Exception{
		this.logMessage("The bouilloire is starting his job....") ;
		isOn = true;
		/*this.logMessage("Bouilloire "+this.uri+" : start.") ;
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							((Bouilloire)this.getTaskOwner()).sendProduction();
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				},
				1000, TimeUnit.MILLISECONDS) ;*/
	}
	
	

	public double sendProduction() throws Exception {
		this.logMessage("Sending energy....") ;
		
		return Math.random()*10;
		
	}


	public void stopBouilloire() throws Exception{
		this.logMessage("The bouilloire is stopping his job....") ;
		isOn =false;
		/*this.logMessage("Bouilloire "+this.uri+" : stop.") ;
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							((Bouilloire)this.getTaskOwner()).stopBouilloire();
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				},
				1000, TimeUnit.MILLISECONDS) ;*/
	}
	
	
	public void			start() throws ComponentStartException
	{
		super.start() ;
		this.logMessage("starting Bouilloire component.") ;
		/*// Schedule the first service method invocation in one second.
		this.scheduleTask(
			new AbstractComponent.AbstractTask() {
				@Override
				public void run() {
					try {
						System.out.println(this.getTaskOwner());
						((Bouilloire)this.getTaskOwner()).startBouilloire(); ;
					} catch (Exception e) {
						throw new RuntimeException(e) ;
					}
				}
			},
			1000, TimeUnit.MILLISECONDS);*/
	}
}
