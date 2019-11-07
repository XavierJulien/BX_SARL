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

	//--------------------------------------------------------------
	//-------------------------URI COMPONENTS-----------------------
	//--------------------------------------------------------------
	protected final String				uri ;
	protected final String				controleurEolienneInboundPortURI ;	
	protected final String				controleurEolienneOutboundPortURI ;
	protected final String				controleurBouilloireOutboundPortURI ;
	protected final String				controleurBouilloireInboundPortURI ;
	protected final String				controleurChauffageOutboundPortURI ;
	protected final String				controleurChauffageInboundPortURI ;
	protected final String				controleurCompteurOutboundPortURI ;
	protected final String				controleurCompteurInboundPortURI ;
	protected final String				controleurChargeurOutboundPortURI ;
	protected final String				controleurChargeurInboundPortURI ;
	protected final String				controleurBatterieOutboundPortURI ;
	protected final String				controleurBatterieInboundPortURI ;
	protected final String				controleurCapteurVentOutboundPortURI ;
	protected final String				controleurCapteurChaleurOutboundPortURI ;

	//--------------------------------------------------------------
	//-------------------------INBOUND PORT-------------------------
	//--------------------------------------------------------------
	protected ControleurInboundPort		controleurEolienneInboundPort ;
	protected ControleurInboundPort		controleurBouilloireInboundPort ;
	protected ControleurInboundPort		controleurChauffageInboundPort ;
	protected ControleurInboundPort		controleurCompteurInboundPort ;
	protected ControleurInboundPort		controleurChargeurInboundPort ;
	protected ControleurInboundPort		controleurBatterieInboundPort ;
	//--------------------------------------------------------------
	//-------------------------OUTBOUND PORT------------------------
	//--------------------------------------------------------------
	protected ControleurOutboundPort	controleurEolienneOutboundPort ;
	protected ControleurOutboundPort 	controleurBouilloireOutboundPort;
	protected ControleurOutboundPort 	controleurChauffageOutboundPort;
	protected ControleurOutboundPort 	controleurCompteurOutboundPort;
	protected ControleurOutboundPort 	controleurChargeurOutboundPort;
	protected ControleurOutboundPort 	controleurBatterieOutboundPort;
	protected ControleurOutboundPort 	controleurCapteurVentOutboundPort;
	protected ControleurOutboundPort 	controleurCapteurChaleurOutboundPort;

	
	protected double windSpeed = 0;
	protected double temperature = 0;
	public boolean isEolienneOn = false;
	protected double prod = 0;

//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected Controleur(String uri,
						 String controleurEolienneOutboundPortURI,
						 String controleurEolienneInboundPortURI, 
						 String controleurBouilloireOutboundPortURI, 
						 String controleurBouilloireInboundPortURI, 
						 String controleurChauffageOutboundPortURI, 
						 String controleurChauffageInboundPortURI,
						 String controleurCompteurOutboundPortURI, 
						 String controleurCompteurInboundPortURI,
						 String controleurChargeurOutboundPortURI, 
						 String controleurChargeurInboundPortURI,
						 String controleurBatterieOutboundPortURI, 
						 String controleurBatterieInboundPortURI,
						 String controleurCapteurVentOutboundPortURI, 
						 String controleurCapteurChaleurOutboundPortURI) throws Exception{
		super(uri, 2, 2);

		assert uri != null;
		//--------------------------------------------------------------
		//-------------------------INBOUND PORT-------------------------
		//--------------------------------------------------------------
		assert controleurEolienneInboundPortURI != null;
		assert controleurBouilloireInboundPortURI != null;
		assert controleurChauffageInboundPortURI != null;
		assert controleurCompteurInboundPortURI != null;
		assert controleurChargeurInboundPortURI != null;
		assert controleurBatterieInboundPortURI != null;

		//--------------------------------------------------------------
		//-------------------------OUTBOUND PORT------------------------
		//--------------------------------------------------------------
		assert controleurEolienneOutboundPortURI != null;
		assert controleurChauffageOutboundPortURI != null;
		assert controleurCompteurOutboundPortURI != null;
		assert controleurCapteurVentOutboundPortURI != null;
		assert controleurCapteurChaleurOutboundPortURI != null;
		assert controleurBouilloireOutboundPortURI != null;
		assert controleurChargeurOutboundPortURI != null;
		assert controleurBatterieOutboundPortURI != null;

		this.uri = uri;
		this.controleurEolienneInboundPortURI = controleurEolienneInboundPortURI;
		this.controleurEolienneOutboundPortURI = controleurEolienneOutboundPortURI;
		this.controleurCapteurVentOutboundPortURI = controleurCapteurVentOutboundPortURI;
		this.controleurCapteurChaleurOutboundPortURI = controleurCapteurChaleurOutboundPortURI;
		this.controleurBouilloireOutboundPortURI = controleurBouilloireOutboundPortURI;
		this.controleurBouilloireInboundPortURI = controleurBouilloireInboundPortURI;
		this.controleurChauffageOutboundPortURI = controleurChauffageOutboundPortURI;
		this.controleurChauffageInboundPortURI = controleurChauffageInboundPortURI;
		this.controleurCompteurOutboundPortURI = controleurCompteurOutboundPortURI;
		this.controleurCompteurInboundPortURI = controleurCompteurInboundPortURI;
		this.controleurChargeurOutboundPortURI = controleurChargeurOutboundPortURI;
		this.controleurChargeurInboundPortURI = controleurChargeurInboundPortURI;
		this.controleurBatterieOutboundPortURI = controleurChargeurOutboundPortURI;
		this.controleurBatterieInboundPortURI = controleurChargeurInboundPortURI;

		//-------------------PUBLISH INBOUND PORT-------------------
		controleurEolienneInboundPort = new ControleurInboundPort(controleurEolienneInboundPortURI, this) ;
		controleurEolienneInboundPort.publishPort() ;
		controleurBouilloireInboundPort = new ControleurInboundPort(controleurBouilloireInboundPortURI, this) ;
		controleurBouilloireInboundPort.publishPort() ;
		controleurChauffageInboundPort = new ControleurInboundPort(controleurChauffageInboundPortURI, this) ;
		controleurChauffageInboundPort.publishPort() ;
		controleurCompteurInboundPort = new ControleurInboundPort(controleurCompteurInboundPortURI, this) ;
		controleurCompteurInboundPort.publishPort() ;
		controleurChargeurInboundPort = new ControleurInboundPort(controleurChargeurInboundPortURI, this) ;
		controleurChargeurInboundPort.publishPort() ;
		controleurBatterieInboundPort = new ControleurInboundPort(controleurBatterieInboundPortURI, this) ;
		controleurBatterieInboundPort.publishPort() ;
		
		//-------------------PUBLISH INBOUND PORT-------------------
		this.controleurEolienneOutboundPort = new ControleurOutboundPort(controleurEolienneOutboundPortURI, this) ;
		this.controleurEolienneOutboundPort.localPublishPort() ;
		this.controleurCapteurVentOutboundPort = new ControleurOutboundPort(controleurCapteurVentOutboundPortURI, this) ;
		this.controleurCapteurVentOutboundPort.localPublishPort() ;
		this.controleurCapteurChaleurOutboundPort = new ControleurOutboundPort(controleurCapteurChaleurOutboundPortURI, this) ;
		this.controleurCapteurChaleurOutboundPort.localPublishPort() ;
		this.controleurBouilloireOutboundPort = new ControleurOutboundPort(controleurBouilloireOutboundPortURI, this) ;
		this.controleurBouilloireOutboundPort.localPublishPort() ;
		this.controleurChauffageOutboundPort = new ControleurOutboundPort(controleurChauffageOutboundPortURI, this) ;
		this.controleurChauffageOutboundPort.localPublishPort() ;
		this.controleurCompteurOutboundPort = new ControleurOutboundPort(controleurCompteurOutboundPortURI, this) ;
		this.controleurCompteurOutboundPort.localPublishPort() ;
		this.controleurChargeurOutboundPort = new ControleurOutboundPort(controleurChargeurOutboundPortURI, this) ;
		this.controleurChargeurOutboundPort.localPublishPort() ;		
		this.controleurBatterieOutboundPort = new ControleurOutboundPort(controleurBatterieOutboundPortURI, this) ;
		this.controleurBatterieOutboundPort.localPublishPort() ;
		
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}
		
		//-------------------GUI-------------------
		this.tracer.setTitle("Controleur") ;
		this.tracer.setRelativePosition(0, 0) ;
	}


//------------------------------------------------------------------------
//----------------------------SERVICES------------------------------------
//------------------------------------------------------------------------
	
	//--------------------------------------------------------------
	//-------------------------EOLIENNE-----------------------------
	//--------------------------------------------------------------
	public void startEolienne() throws Exception {
		this.logMessage("Controleur "+this.uri+" : tell eolienne to start.") ;
		this.controleurEolienneOutboundPort.startEolienne();
	}
	public void stopEolienne() throws Exception{
		this.logMessage("Controleur "+this.uri+" : tell eolienne to stop.") ;
		this.controleurEolienneOutboundPort.stopEolienne();
	}
	public void getProd() throws Exception {
		double prod = this.controleurCapteurVentOutboundPort.getProd();
		this.logMessage("The controleur is getting "+prod+" units of energy from the eolienne") ;
	}

	//--------------------------------------------------------------
	//-------------------------BOUILLOIRE---------------------------
	//--------------------------------------------------------------
	public void startBouilloire() throws Exception{	
		this.logMessage("Controleur "+this.uri+" : tell bouilloire to start.") ;
		this.controleurBouilloireOutboundPort.startBouilloire();
	}
	public void stopBouilloire() throws Exception{
		this.logMessage("Controleur "+this.uri+" : tell bouilloire to stop.") ;
		this.controleurBouilloireOutboundPort.stopBouilloire();
	}

	//--------------------------------------------------------------
	//-------------------------CHARGEUR-----------------------------
	//--------------------------------------------------------------
	public void startChargeur() throws Exception{	
		this.logMessage("Controleur "+this.uri+" : tell chargeur to start.") ;
		this.controleurChargeurOutboundPort.startChargeur();
	}
	public void stopChargeur() throws Exception{
		this.logMessage("Controleur "+this.uri+" : tell chargeur to stop.") ;
		this.controleurChargeurOutboundPort.stopChargeur();
	}	
	
	//--------------------------------------------------------------
	//-------------------------CHAUFFAGE----------------------------
	//--------------------------------------------------------------
	public void startChauffage() throws Exception{	
		this.logMessage("Controleur "+this.uri+" : tell chauffage to start.") ;
		this.controleurChauffageOutboundPort.startChauffage();
	}
	public void stopChauffage() throws Exception{
		this.logMessage("Controleur "+this.uri+" : tell chauffage to stop.") ;
		this.controleurChauffageOutboundPort.stopChauffage();
	}
	
	//--------------------------------------------------------------
	//-------------------------COMPTEUR-----------------------------
	//--------------------------------------------------------------
	public void startCompteur() throws Exception{	
		this.logMessage("Controleur "+this.uri+" : tell compteur to start.") ;
		this.controleurCompteurOutboundPort.startChauffage();
	}
	public void stopCompteur() throws Exception{
		this.logMessage("Controleur "+this.uri+" : tell compteur to stop.") ;
		this.controleurCompteurOutboundPort.stopChauffage();
	}
	
	//--------------------------------------------------------------
	//-------------------------BATTERIE-----------------------------
	//--------------------------------------------------------------
	public void startBatterie() throws Exception{	
		this.logMessage("Controleur "+this.uri+" : tell batterie to start.") ;
		this.controleurBatterieOutboundPort.startBatterie();
	}
	public void stopBatterie() throws Exception{
		this.logMessage("Controleur "+this.uri+" : tell batterie to stop.") ;
		this.controleurBatterieOutboundPort.stopBatterie();
	}

	//--------------------------------------------------------------
	//-------------------------CAPTEURS-----------------------------
	//--------------------------------------------------------------
	public void getVent() throws Exception{
		double prod = this.controleurCapteurVentOutboundPort.getVent() ;
		windSpeed = prod;
		this.logMessage("The controleur is informed that the wind power is"+prod) ;
	}	
	
	public void getChaleur() throws Exception{
		double prod = this.controleurCapteurChaleurOutboundPort.getChaleur() ;
		temperature = prod;
		this.logMessage("The controleur is informed that the wind power is"+prod) ;
	}
	
	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Controleur component.") ;	
	}
	
	public void execute() throws Exception {
		super.execute();
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
						} catch (Exception e) {throw new RuntimeException(e) ;}
					}
				},
				1000, TimeUnit.MILLISECONDS);
	}


//------------------------------------------------------------------------
//----------------------------FINALISE------------------------------------
//------------------------------------------------------------------------
	@Override
	public void finalise() throws Exception {
		controleurEolienneOutboundPort.doDisconnection();
		controleurCapteurVentOutboundPort.doDisconnection();
		controleurCapteurChaleurOutboundPort.doDisconnection();
		controleurBouilloireOutboundPort.doDisconnection();
		controleurChauffageOutboundPort.doDisconnection();
		controleurCompteurOutboundPort.doDisconnection();
		controleurChargeurOutboundPort.doDisconnection();
		controleurBatterieOutboundPort.doDisconnection();
		super.finalise();
	}

//------------------------------------------------------------------------
//----------------------------SHUTDOWN------------------------------------
//------------------------------------------------------------------------
	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			controleurEolienneInboundPort.unpublishPort();
			controleurEolienneOutboundPort.unpublishPort();
			controleurCapteurVentOutboundPort.unpublishPort();
			controleurCapteurChaleurOutboundPort.unpublishPort();
			controleurBouilloireInboundPort.unpublishPort();
			controleurBouilloireOutboundPort.unpublishPort();
			controleurChauffageInboundPort.unpublishPort();
			controleurChauffageOutboundPort.unpublishPort();
			controleurCompteurInboundPort.unpublishPort();
			controleurCompteurOutboundPort.unpublishPort();
			controleurChargeurInboundPort.unpublishPort();
			controleurChargeurOutboundPort.unpublishPort();
			controleurBatterieInboundPort.unpublishPort();
			controleurBatterieOutboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
