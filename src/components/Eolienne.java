package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.PortI;
import launcher.CVM;
import ports.EolienneInboundPort;
import ports.EolienneOutboundPort;

public class Eolienne extends AbstractComponent /*implements EolienneI*/{

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
	
	
	/** utility vars */
	protected double prod;
	protected boolean isOn=false;

	protected Eolienne(String uri,String eolienneOutboundPortURI,String eolienneInboundPortURI) throws Exception{
		super(uri, 1, 1);

		//check arguments 
		assert uri != null;
		assert eolienneOutboundPortURI != null;
		assert eolienneOutboundPortURI != null;

		// init variables 
		this.uri = uri;
		
		PortI p = new EolienneInboundPort(eolienneInboundPortURI, this) ;
		
		// publish the port
		p.publishPort() ;
		
		
		this.eolienneInboundPortURI = eolienneInboundPortURI;
		this.eolienneOutboundPortURI = eolienneOutboundPortURI;
		
		this.eolienneOutboundPort =
				new EolienneOutboundPort(eolienneOutboundPortURI, this) ;
			// publish the port (an outbound port is always local)
			this.eolienneOutboundPort.localPublishPort() ;
	
//		
//		// The  interfaces and ports.
//		//ici on n'est producteur du coup on garde que les OfferedI
//		this.addOfferedInterface(EolienneI.class) ;
//		this.addRequiredInterface(EolienneI.class) ;
//		
//		
//		// init des ports avec les uri et de la donnée qui circule
//		this.eolienneInboundPortURI = new eolienneInboundPortURI(this.eolienneInboundPortURIURI, this) ;
//		this.eolienneOutboundPort = new EolienneOutboundPort(this.eolienneOutboundPortURI, this) ;
//		
//		//publish des ports (voir avec le prof ce que ça fait)
//		this.eolienneOutboundPort.publishPort() ;
//		this.eolienneInboundPortURI.publishPort() ;
//		
			if (AbstractCVM.isDistributed) {
				this.executionLog.setDirectory(System.getProperty("user.dir")) ;
			} else {
				this.executionLog.setDirectory(System.getProperty("user.home")) ;
			}	
			
		this.tracer.setTitle(uri) ;
		if (uri.equals(CVM.EOLIENNE_COMPONENT_URI)) {
			this.tracer.setRelativePosition(1, 2) ;
		} else {
			this.tracer.setRelativePosition(1, 0) ;
		}
		
		
		this.prod = 0;
		
		
	}
	
	
	// ------------------------------------------------------------------------
	// Services
	// ------------------------------------------------------------------------

	public void startEolienne() throws Exception{
		this.logMessage("The eolienne is starting his job....") ;
		isOn = true;
		/*this.logMessage("Eolienne "+this.uri+" : start.") ;
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							((Eolienne)this.getTaskOwner()).sendProduction();
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


	public void stopEolienne() throws Exception{
		this.logMessage("The eolienne is stopping his job....") ;
		isOn =false;
		/*this.logMessage("Eolienne "+this.uri+" : stop.") ;
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
				1000, TimeUnit.MILLISECONDS) ;*/
	}
	
	
	public void			start() throws ComponentStartException
	{
		super.start() ;
		this.logMessage("starting Eolienne component.") ;
		/*// Schedule the first service method invocation in one second.
		this.scheduleTask(
			new AbstractComponent.AbstractTask() {
				@Override
				public void run() {
					try {
						System.out.println(this.getTaskOwner());
						((Eolienne)this.getTaskOwner()).startEolienne(); ;
					} catch (Exception e) {
						throw new RuntimeException(e) ;
					}
				}
			},
			1000, TimeUnit.MILLISECONDS);*/
	}
}
