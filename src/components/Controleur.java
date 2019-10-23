package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.ControleurI;
import ports.ControleurInboundPort;
import ports.ControleurOutboundPort;

@RequiredInterfaces(required = {ControleurI.class})
@OfferedInterfaces(offered = {ControleurI.class})
public class Controleur extends AbstractComponent {

	//---------------------------------------------------
	//------------------------URI------------------------
	//---------------------------------------------------
	/** URI of the component (controleur).*/
	protected final String				uri ;
	/** The  inbound port URI for the controleur.*/
	protected final String				controleurEolienneInboundPortURI ;	
	/** The  outbound port URI for the controleur.*/
	protected final String				controleurEolienneOutboundPortURI ;
	/***/
	protected final String				controleurCapteurOutboundPortURI ;
	/***/
	protected final String				controleurBouilloireOutboundPortURI ;

	protected final String				controleurBouilloireInboundPortURI ;
	
	protected final String				controleurChauffageOutboundPortURI ;

	protected final String				controleurChauffageInboundPortURI ;

	//--------------------------------------------------------------
	//-------------------------INBOUND PORT-------------------------
	//--------------------------------------------------------------
	/**  inbound port for the component (controleur).*/
	protected ControleurInboundPort		controleurEolienneInboundPort ;
	/**  inbound port for the component (controleur).*/
	protected ControleurInboundPort		controleurBouilloireInboundPort ;
	/**  inbound port for the component (controleur).*/
	protected ControleurInboundPort		controleurChauffageInboundPort ;
	//--------------------------------------------------------------
	//-------------------------OUTBOUND PORT------------------------
	//--------------------------------------------------------------
	/**  outbound port for the component (controleur).*/
	protected ControleurOutboundPort	controleurEolienneOutboundPort ;
	/***/
	protected ControleurOutboundPort 	controleurCapteurOutboundPort;
	/***/
	protected ControleurOutboundPort 	controleurBouilloireOutboundPort;
	/***/
	protected ControleurOutboundPort 	controleurChauffageOutboundPort;



	protected double windSpeed = 0;

	public boolean isEolienneOn = false;

	protected double prod = 0;



	protected Controleur(String uri,
						 String controleurEolienneOutboundPortURI,
						 String controleurEolienneInboundPortURI, 
						 String controleurCapteurOutboundPortURI, 
						 String controleurBouilloireOutboundPortURI, 
						 String controleurBouilloireInboundPortURI, 
						 String controleurChauffageOutboundPortURI, 
						 String controleurChauffageInboundPortURI) throws Exception{
		super(uri, 2, 2);

		//check arguments 
		assert uri != null;
		assert controleurEolienneInboundPortURI != null;
		assert controleurBouilloireInboundPortURI != null;
		assert controleurChauffageInboundPortURI != null;

		assert controleurEolienneOutboundPortURI != null;
		assert controleurChauffageOutboundPortURI != null;
		assert controleurCapteurOutboundPortURI != null;
		assert controleurBouilloireOutboundPortURI != null;

		// init variables 
		this.uri = uri;

		controleurEolienneInboundPort = new ControleurInboundPort(controleurEolienneInboundPortURI, this) ;

		// publish the port
		controleurEolienneInboundPort.publishPort() ;

		controleurBouilloireInboundPort = new ControleurInboundPort(controleurBouilloireInboundPortURI, this) ;

		// publish the port
		controleurBouilloireInboundPort.publishPort() ;
		
		controleurChauffageInboundPort = new ControleurInboundPort(controleurChauffageInboundPortURI, this) ;

		// publish the port
		controleurChauffageInboundPort.publishPort() ;

		//AJOUTER DANS SHUTDOWN
		//pB.destroyPort();

		this.controleurEolienneInboundPortURI = controleurEolienneInboundPortURI;
		this.controleurEolienneOutboundPortURI = controleurEolienneOutboundPortURI;
		this.controleurCapteurOutboundPortURI = controleurCapteurOutboundPortURI;
		this.controleurBouilloireOutboundPortURI = controleurBouilloireOutboundPortURI;
		this.controleurBouilloireInboundPortURI = controleurBouilloireInboundPortURI;
		this.controleurChauffageOutboundPortURI = controleurChauffageOutboundPortURI;
		this.controleurChauffageInboundPortURI = controleurChauffageInboundPortURI;

		this.controleurEolienneOutboundPort =
				new ControleurOutboundPort(controleurEolienneOutboundPortURI, this) ;
		// publish the port (an outbound port is always local)
		this.controleurEolienneOutboundPort.localPublishPort() ;

		this.controleurCapteurOutboundPort =
				new ControleurOutboundPort(controleurCapteurOutboundPortURI, this) ;
		// publish the port (an outbound port is always local)
		this.controleurCapteurOutboundPort.localPublishPort() ;

		this.controleurBouilloireOutboundPort =
				new ControleurOutboundPort(controleurBouilloireOutboundPortURI, this) ;
		// publish the port (an outbound port is always local)
		this.controleurBouilloireOutboundPort.localPublishPort() ;

		this.controleurChauffageOutboundPort =
				new ControleurOutboundPort(controleurChauffageOutboundPortURI, this) ;
		// publish the port (an outbound port is always local)
		this.controleurChauffageOutboundPort.localPublishPort() ;

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
		this.controleurEolienneOutboundPort.startEolienne();

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
		this.controleurEolienneOutboundPort.stopEolienne();
	}

	public void startBouilloire() throws Exception{	
		this.logMessage("Controleur "+this.uri+" : tell bouilloire to start.") ;
		this.controleurBouilloireOutboundPort.startBouilloire();
	}

	public void stopBouilloire() throws Exception{
		this.logMessage("Controleur "+this.uri+" : tell bouilloire to stop.") ;
		this.controleurBouilloireOutboundPort.stopBouilloire();
	}

	public void startChauffage() throws Exception{	
		this.logMessage("Controleur "+this.uri+" : tell chauffage to start.") ;
		this.controleurChauffageOutboundPort.startChauffage();
	}

	public void stopChauffage() throws Exception{
		this.logMessage("Controleur "+this.uri+" : tell chauffage to stop.") ;
		this.controleurChauffageOutboundPort.stopChauffage();
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
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				},
				1000, TimeUnit.MILLISECONDS);
	}

	public void getProd() throws Exception {
		double prod = this.controleurCapteurOutboundPort.getProd() ;

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

	// ------------------------------------------------------------------------
	// FINALISE / SHUTDOWN
	// ------------------------------------------------------------------------

	@Override
	public void finalise() throws Exception {
		controleurEolienneOutboundPort.doDisconnection();
		controleurCapteurOutboundPort.doDisconnection();
		controleurBouilloireOutboundPort.doDisconnection();
		controleurChauffageOutboundPort.doDisconnection();
		super.finalise();
	}

	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			controleurEolienneInboundPort.unpublishPort();
			controleurEolienneOutboundPort.unpublishPort();
			controleurCapteurOutboundPort.unpublishPort();
			controleurBouilloireInboundPort.unpublishPort();
			controleurBouilloireOutboundPort.unpublishPort();
			controleurChauffageInboundPort.unpublishPort();
			controleurChauffageOutboundPort.unpublishPort();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.shutdown();
	}
}
