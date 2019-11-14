package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.CompteurI;
import ports.CompteurInboundPort;
import ports.CompteurOutboundPort;

@RequiredInterfaces(required = {CompteurI.class})
@OfferedInterfaces(offered = {CompteurI.class})
public class Compteur extends AbstractComponent {
	
	protected final String				uri ;
	protected final String				compteurInboundPortURI ;	
	protected final String				compteurOutboundPortURI ;
	protected final String				compteurChauffageInboundPortURI ;	
	protected final String				compteurChauffageOutboundPortURI ;
	protected final String				compteurBouilloireInboundPortURI ;	
	protected final String				compteurBouilloireOutboundPortURI ;
	protected final String				compteurChargeurInboundPortURI ;	
	protected final String				compteurChargeurOutboundPortURI ;

	
	protected CompteurOutboundPort		compteurOutboundPort ;
	protected CompteurInboundPort		compteurInboundPort ;
	protected CompteurOutboundPort		compteurChauffageOutboundPort ;
	protected CompteurInboundPort		compteurChauffageInboundPort ;
	protected CompteurOutboundPort		compteurBouilloireOutboundPort ;
	protected CompteurInboundPort		compteurBouilloireInboundPort ;
	protected CompteurOutboundPort		compteurChargeurOutboundPort ;
	protected CompteurInboundPort		compteurChargeurInboundPort ;
	
	protected boolean 					isOn=false;
	protected double					consoChauffage = 0;
	protected double					consoChargeur = 0;
	protected double					consoBouilloire = 0;


	
//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected Compteur(String uri,
					   String compteurOutboundPortURI,
					   String compteurInboundPortURI,
					   String compteurChauffageOutboundPortURI,
					   String compteurChauffageInboundPortURI,
					   String compteurBouilloireOutboundPortURI,
					   String compteurBouilloireInboundPortURI,
					   String compteurChargeurOutboundPortURI,
					   String compteurChargeurInboundPortURI) throws Exception{
		super(uri, 1, 1);

		assert uri != null;
		assert compteurOutboundPortURI != null;
		assert compteurInboundPortURI != null;

		this.uri = uri;
		this.compteurInboundPortURI = compteurInboundPortURI;
		this.compteurOutboundPortURI = compteurOutboundPortURI;
		this.compteurChauffageOutboundPortURI = compteurChauffageOutboundPortURI;
		this.compteurChauffageInboundPortURI = compteurChauffageInboundPortURI;
		this.compteurBouilloireOutboundPortURI = compteurBouilloireOutboundPortURI;
		this.compteurBouilloireInboundPortURI = compteurBouilloireInboundPortURI;
		this.compteurChargeurOutboundPortURI = compteurChargeurOutboundPortURI;
		this.compteurChargeurInboundPortURI = compteurChargeurInboundPortURI;

		//-------------------PUBLISH-------------------
		compteurInboundPort = new CompteurInboundPort(compteurInboundPortURI, this) ;
		compteurInboundPort.publishPort() ;
		this.compteurOutboundPort = new CompteurOutboundPort(compteurOutboundPortURI, this) ;
		this.compteurOutboundPort.localPublishPort() ;
		
		compteurChauffageInboundPort = new CompteurInboundPort(compteurChauffageInboundPortURI, this) ;
		compteurChauffageInboundPort.publishPort() ;
		this.compteurChauffageOutboundPort = new CompteurOutboundPort(compteurChauffageOutboundPortURI, this) ;
		this.compteurChauffageOutboundPort.localPublishPort() ;
		
		compteurBouilloireInboundPort = new CompteurInboundPort(compteurBouilloireInboundPortURI, this) ;
		compteurBouilloireInboundPort.publishPort() ;
		this.compteurBouilloireOutboundPort = new CompteurOutboundPort(compteurBouilloireOutboundPortURI, this) ;
		this.compteurBouilloireOutboundPort.localPublishPort() ;
		
		compteurChargeurInboundPort = new CompteurInboundPort(compteurChargeurInboundPortURI, this) ;
		compteurChargeurInboundPort.publishPort() ;
		this.compteurChargeurOutboundPort = new CompteurOutboundPort(compteurChargeurOutboundPortURI, this) ;
		this.compteurChargeurOutboundPort.localPublishPort() ;

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		//-------------------GUI-------------------
		this.tracer.setTitle(uri) ;
		this.tracer.setRelativePosition(3, 3) ;
	}


//------------------------------------------------------------------------
//----------------------------SERVICES------------------------------------
//------------------------------------------------------------------------
	
	public void startCompteur() throws Exception{
		this.logMessage("The compteur is starting his job....") ;
		isOn = true;
	}

	public void stopCompteur() throws Exception{
		this.logMessage("The compteur is stopping his job....") ;
		isOn =false;
	}
	
	public double sendAllConso() throws Exception {
		this.logMessage("Sending all consommation....") ;
		return consoBouilloire+consoChargeur+consoChauffage;
	}
	
	public void getChauffageConso() throws Exception {
		double conso = this.compteurChauffageOutboundPort.getChauffageConso();
		this.consoChauffage = conso;
		this.logMessage("The compteur is informed that the chauffage consumes "+conso+" units of energy.") ;
	}
	
	public void getBouilloireConso() throws Exception {
		double conso = this.compteurBouilloireOutboundPort.getBouilloireConso();
		this.consoBouilloire = conso;
		this.logMessage("The compteur is informed that the bouilloire consumes "+conso+" units of energy.") ;
	}
	
	public void getChargeurConso() throws Exception {
		double conso = this.compteurChargeurOutboundPort.getChargeurConso();
		this.consoChargeur = conso;
		this.logMessage("The compteur is informed that the chargeur consumes "+conso+" units of energy.") ;
	}
	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Compteur component.") ;
	}
	
	@Override
	public void execute() throws Exception{
		super.execute();
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							while(true) {
								((Compteur)this.getTaskOwner()).getChauffageConso() ;
								((Compteur)this.getTaskOwner()).getBouilloireConso() ;
								((Compteur)this.getTaskOwner()).getChargeurConso() ;
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
		compteurOutboundPort.doDisconnection();
		compteurChauffageOutboundPort.doDisconnection();
		compteurBouilloireOutboundPort.doDisconnection();
		compteurChargeurOutboundPort.doDisconnection();
		super.finalise();
	}

//------------------------------------------------------------------------
//----------------------------SHUTDOWN------------------------------------
//------------------------------------------------------------------------
	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			compteurInboundPort.unpublishPort();
			compteurOutboundPort.unpublishPort();
			compteurChauffageInboundPort.unpublishPort();
			compteurChauffageOutboundPort.unpublishPort();
			compteurBouilloireInboundPort.unpublishPort();
			compteurBouilloireOutboundPort.unpublishPort();
			compteurChargeurInboundPort.unpublishPort();
			compteurChargeurOutboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
