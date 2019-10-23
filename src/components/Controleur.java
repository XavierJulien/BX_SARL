package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.PortI;
import interfaces.ControleurI;
import ports.ControleurInboundPort;
import ports.ControleurOutboundPort;

@RequiredInterfaces(required = {ControleurI.class})
@OfferedInterfaces(offered = {ControleurI.class})
public class Controleur extends AbstractComponent {

	
	
	/** URI of the component (controleur).*/
	protected final String				uri ;
	/** The  inbound port URI for the controleur.*/
	protected final String				controleurInboundPortURI ;	
	/** The  outbound port URI for the controleur.*/
	protected final String				controleurOutboundPortURI ;
	/***/
	protected final String				controleurCapteurOutboundPortURI ;
	/**  outbound port for the component (controleur).*/
	protected ControleurOutboundPort	controleurOutboundPort ;
	/**  inbound port for the component (controleur).*/
	protected ControleurInboundPort	controleurInboundPort ;
	/***/
	protected ControleurOutboundPort controleurCapteurOutboundPort;
	
	
	
	protected double windSpeed = 0;
	
	public boolean isEolienneOn = false;
	
	protected double prod = 0;

	
	protected Controleur(String uri,String controleurOutboundPortURI,String controleurInboundPortURI, String controleurCapteurOutboundPortURI) throws Exception{
		super(uri, 2, 2);

		//check arguments 
		assert uri != null;
		assert controleurOutboundPortURI != null;
		assert controleurOutboundPortURI != null;
		assert controleurCapteurOutboundPortURI != null;

		// init variables 
		this.uri = uri;
		
		PortI p = new ControleurInboundPort(controleurInboundPortURI, this) ;
		
		// publish the port
		p.publishPort() ;
		
		
		
		this.controleurInboundPortURI = controleurInboundPortURI;
		this.controleurOutboundPortURI = controleurOutboundPortURI;
		this.controleurCapteurOutboundPortURI = controleurCapteurOutboundPortURI;
		
		
		// The  interfaces and ports.
		//ici on n'est producteur du coup on garde que les OfferedI
//		this.addOfferedInterface(ControleurI.class) ;
//		this.addRequiredInterface(ControleurI.class) ;
		
		this.controleurOutboundPort =
				new ControleurOutboundPort(controleurOutboundPortURI, this) ;
			// publish the port (an outbound port is always local)
			this.controleurOutboundPort.localPublishPort() ;
			
		this.controleurCapteurOutboundPort =
				new ControleurOutboundPort(controleurCapteurOutboundPortURI, this) ;
			// publish the port (an outbound port is always local)
			this.controleurCapteurOutboundPort.localPublishPort() ;
	
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

	public void stopEolienne() throws Exception{
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
		this.controleurOutboundPort.stopEolienne();
	}
	
	public void startBouilloire() throws Exception{	
		this.logMessage("Controleur "+this.uri+" : tell bouilloire to start.") ;
		this.controleurOutboundPort.startBouilloire();
	}

	public void stopBouilloire() throws Exception{
		this.logMessage("Controleur "+this.uri+" : tell bouilloire to stop.") ;
		this.controleurOutboundPort.stopBouilloire();
	}
	
	public void	start() throws ComponentStartException
	{
		
		super.start() ;
		this.logMessage("starting Controleur component.") ;
		// Schedule the first service method invocation in one second.
		this.scheduleTask(
			new AbstractComponent.AbstractTask() {
				@Override
				public void run() {
					try {
						
						while(true) {
							System.out.println("WIndspeed = "+windSpeed);
							synchronized(this){
								((Controleur)this.getTaskOwner()).getVent() ;
								if(isEolienneOn) {
									if(windSpeed < 0.5) {
										((Controleur)this.getTaskOwner()).getProd() ;
									}else {
										((Controleur)this.getTaskOwner()).stopEolienne();
										isEolienneOn = false;
									}
								}else {
									if(windSpeed < 0.5) {
										((Controleur)this.getTaskOwner()).startEolienne();
										isEolienneOn = true;
									}
								}
								Thread.sleep(1000);
							}
						}
						
						//((Controleur)this.getTaskOwner()).getVent() ;
						
					} catch (Exception e) {
						throw new RuntimeException(e) ;
					}
				}
			},
			1000, TimeUnit.MILLISECONDS);
	}

	public void getProd() throws Exception {
		double prod = this.controleurOutboundPort.getProd() ;
		
		this.logMessage("The controleur is getting "+prod+" units of energy from the eolienne") ;
		/*this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							((Controleur)this.getTaskOwner()).getProd();
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				},
				1000, TimeUnit.MILLISECONDS) ;*/
	}
	
	public void getVent() throws Exception{
		double prod = this.controleurCapteurOutboundPort.getVent() ;
		windSpeed = prod;
		this.logMessage("The controleur is informed that the wind power is"+prod) ;
		/*this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							((Controleur)this.getTaskOwner()).getVent();
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				},
				1000, TimeUnit.MILLISECONDS) ;*/
		
	}	
}
