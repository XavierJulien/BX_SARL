package launcher;


import components.Batterie;
import components.Bouilloire;
import components.CapteurChaleur;
import components.CapteurVent;
import components.Chargeur;
import components.Chauffage;
import components.Compteur;
import components.Controleur;
import components.Eolienne;
import connectors.BatterieControleurConnector;
import connectors.BouilloireControleurConnector;
import connectors.CapteurChauffageConnector;
import connectors.ChargeurControleurConnector;
import connectors.ChauffageControleurConnector;
import connectors.CompteurControleurConnector;
import connectors.ControleurConnector;
import connectors.EolienneControleurConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

public class CVM extends AbstractCVM {
	
//--------------------------------------------------------------
//-------------------------VARIABLES----------------------------
//--------------------------------------------------------------
	
	//--------------------------------------------------------------
	//-------------------------CONTROLEUR---------------------------
	//--------------------------------------------------------------
	public static final String	CONTROLEUR_COMPONENT_URI = "my-URI-controleur" ;
	
	//--------------------------------------------------------------
	//-------------------------EOLIENNE-----------------------------
	//--------------------------------------------------------------
	public static final String	EOLIENNE_COMPONENT_URI = "my-URI-eolienne" ;
	protected static final String	URIEolienneOutboundPortURI = "eolienneOPort" ;
	protected static final String	URIEolienneInboundPortURI = "eolienneIPort" ;	
	protected static final String	URIControleurEolienneOutboundPortURI = "controleurEolienneOPort" ;
	protected static final String	URIControleurEolienneInboundPortURI = "controleurEolienneIPort" ;
	
	//--------------------------------------------------------------
	//-------------------------BOUILLOIRE---------------------------
	//--------------------------------------------------------------	
	public static final String	BOUILLOIRE_COMPONENT_URI = "my-URI-bouilloire" ;
	protected static final String	URIBouilloireOutboundPortURI = "bouilloireOPort" ;
	protected static final String	URIBouilloireInboundPortURI = "bouilloireIPort" ;
	protected static final String	URIControleurBouilloireOutboundPortURI = "controleurBouilloireOPort" ;
	protected static final String	URIControleurBouilloireInboundPortURI = "controleurBouilloireIPort" ;
	
	//--------------------------------------------------------------
	//-------------------------CHAUFFAGE----------------------------
	//--------------------------------------------------------------
	public static final String	CHAUFFAGE_COMPONENT_URI = "my-URI-chauffage" ;
	protected static final String	URIChauffageOutboundPortURI = "chauffageOPort" ;
	protected static final String	URIChauffageInboundPortURI = "chauffageIPort" ;
	protected static final String	URIChauffageToCapteurInboundPortURI = "chauffageToCapteurIPort" ;
	protected static final String	URIControleurChauffageOutboundPortURI = "controleurChauffageOPort" ;
	protected static final String	URIControleurChauffageInboundPortURI = "controleurChauffageIPort" ;
	
	//--------------------------------------------------------------
	//-------------------------COMPTEUR-----------------------------
	//--------------------------------------------------------------
	/** URI of the compteur component (convenience).						*/
	public static final String	COMPTEUR_COMPONENT_URI = "my-URI-compteur" ;
	protected static final String	URICompteurOutboundPortURI = "compteurOPort" ;
	protected static final String	URICompteurInboundPortURI = "compteurIPort" ;	
	protected static final String	URIControleurCompteurOutboundPortURI = "controleurCompteurOPort" ;
	protected static final String	URIControleurCompteurInboundPortURI = "controleurCompteurIPort" ;	
	
	//--------------------------------------------------------------
	//-------------------------CHARGEUR-----------------------------
	//--------------------------------------------------------------
	public static final String	CHARGEUR_COMPONENT_URI = "my-URI-chargeur" ;
	protected static final String	URIChargeurOutboundPortURI = "chargeurOPort" ;
	protected static final String	URIChargeurInboundPortURI = "chargeurIPort" ;
	protected static final String	URIControleurChargeurOutboundPortURI = "controleurChargeurOPort" ;
	protected static final String	URIControleurChargeurInboundPortURI = "controleurChargeurIPort" ;
	
	//--------------------------------------------------------------
	//-------------------------BATTERIE-----------------------------
	//--------------------------------------------------------------
	public static final String	BATTERIE_COMPONENT_URI = "my-URI-batterie" ;
	protected static final String	URIBatterieOutboundPortURI = "batterieOPort" ;
	protected static final String	URIBatterieInboundPortURI = "batterieIPort" ;	
	protected static final String	URIControleurBatterieOutboundPortURI = "controleurBatterieOPort" ;
	protected static final String	URIControleurBatterieInboundPortURI = "controleurBatterieIPort" ;
	
	//--------------------------------------------------------------
	//-------------------------CAPTEUR VENT------------------------
	//--------------------------------------------------------------
	public static final String	CAPTEUR_VENT_COMPONENT_URI = "my-URI-capteur-vent" ;
	protected static final String	URICapteurVentOutboundPortURI = "capteurVentOPort" ;
	protected static final String	URICapteurVentInboundPortURI = "capteurVentIPort" ;
	
	//--------------------------------------------------------------
	//-------------------------CAPTEUR CHALEUR----------------------
	//--------------------------------------------------------------
	public static final String	CAPTEUR_CHALEUR_COMPONENT_URI = "my-URI-capteur-chaleur" ;
	protected static final String	URICapteurChaleurToChauffageOutboundPortURI = "capteurChaleurToChauffageOPort" ;
	protected static final String	URICapteurChaleurOutboundPortURI = "capteurChaleurOPort" ;
	protected static final String	URICapteurChaleurInboundPortURI = "capteurChaleurIPort" ;
	
	
	
	//--------------------------------------------------------------
	//-------------------------URI COMPONENTS-----------------------
	//--------------------------------------------------------------
	protected String uriEolienneURI ;
	protected String uriBouilloireURI ;
	protected String uriChauffageURI ;
	protected String uriCompteurURI ;
	protected String uriControleurURI ;
	protected String uriCapteurVentURI ;
	protected String uriCapteurChaleurURI ;
	protected String uriChargeurURI ;
	protected String uriBatterieURI ;

//--------------------------------------------------------------
//-------------------------CONSTRUCTOR--------------------------
//--------------------------------------------------------------
	protected CVM() throws Exception{
		super();
	}


//--------------------------------------------------------------
//-------------------------DEPLOY-------------------------------
//--------------------------------------------------------------	
	@Override
	public void	deploy() throws Exception{
		assert	!this.deploymentDone() ;

		//--------------------------------------------------------------
		//-------------------------EOLIENNE-----------------------------
		//--------------------------------------------------------------
		this.uriEolienneURI =
				AbstractComponent.createComponent(
						Eolienne.class.getCanonicalName(),
						new Object[]{EOLIENNE_COMPONENT_URI,
								URIEolienneOutboundPortURI,
								URIEolienneInboundPortURI}) ;
		assert	this.isDeployedComponent(this.uriEolienneURI) ;
		this.toggleTracing(this.uriEolienneURI) ;
		this.toggleLogging(this.uriEolienneURI) ;

		//--------------------------------------------------------------
		//-------------------------BOUILLOIRE---------------------------
		//--------------------------------------------------------------
		this.uriBouilloireURI =
				AbstractComponent.createComponent(
						Bouilloire.class.getCanonicalName(),
						new Object[]{BOUILLOIRE_COMPONENT_URI,
								URIBouilloireOutboundPortURI,
								URIBouilloireInboundPortURI}) ;

		assert	this.isDeployedComponent(this.uriBouilloireURI) ;
		this.toggleTracing(this.uriBouilloireURI) ;
		this.toggleLogging(this.uriBouilloireURI) ;

		//--------------------------------------------------------------
		//-------------------------CHAUFFAGE----------------------------
		//--------------------------------------------------------------
		this.uriChauffageURI =
				AbstractComponent.createComponent(
						Chauffage.class.getCanonicalName(),
						new Object[]{CHAUFFAGE_COMPONENT_URI,
								URIChauffageOutboundPortURI,
								URIChauffageInboundPortURI,
								URIChauffageToCapteurInboundPortURI}) ;

		assert	this.isDeployedComponent(this.uriChauffageURI) ;
		this.toggleTracing(this.uriChauffageURI) ;
		this.toggleLogging(this.uriChauffageURI) ;
		
		//--------------------------------------------------------------
		//-------------------------COMPTEUR-----------------------------
		//--------------------------------------------------------------
		this.uriCompteurURI =
				AbstractComponent.createComponent(
						Compteur.class.getCanonicalName(),
						new Object[]{COMPTEUR_COMPONENT_URI,
								URICompteurOutboundPortURI,
								URICompteurInboundPortURI}) ;

		assert	this.isDeployedComponent(this.uriCompteurURI) ;
		this.toggleTracing(this.uriCompteurURI) ;
		this.toggleLogging(this.uriCompteurURI) ;

		//--------------------------------------------------------------
		//-------------------------CAPTEUR VENT-------------------------
		//--------------------------------------------------------------
		this.uriCapteurVentURI =
				AbstractComponent.createComponent(
						CapteurVent.class.getCanonicalName(),
						new Object[]{CAPTEUR_VENT_COMPONENT_URI,
								URICapteurVentInboundPortURI}) ;
		assert	this.isDeployedComponent(this.uriCapteurVentURI) ;
		this.toggleTracing(this.uriCapteurVentURI) ;
		this.toggleLogging(this.uriCapteurVentURI) ;
		
		//--------------------------------------------------------------
		//-------------------------CAPTEUR CHALEUR----------------------
		//--------------------------------------------------------------
		this.uriCapteurChaleurURI =
				AbstractComponent.createComponent(
						CapteurChaleur.class.getCanonicalName(),
						new Object[]{CAPTEUR_CHALEUR_COMPONENT_URI,
								URICapteurChaleurInboundPortURI,
								URICapteurChaleurToChauffageOutboundPortURI}) ;
		assert	this.isDeployedComponent(this.uriCapteurChaleurURI) ;
		this.toggleTracing(this.uriCapteurChaleurURI) ;
		this.toggleLogging(this.uriCapteurChaleurURI) ;
		
		//--------------------------------------------------------------
		//-------------------------CHARGEUR-----------------------------
		//--------------------------------------------------------------
		this.uriChargeurURI =
				AbstractComponent.createComponent(
						Chargeur.class.getCanonicalName(),
						new Object[]{CHARGEUR_COMPONENT_URI,
								URIChargeurOutboundPortURI,
								URIChargeurInboundPortURI}) ;
		assert	this.isDeployedComponent(this.uriChargeurURI) ;
		this.toggleTracing(this.uriChargeurURI) ;
		this.toggleLogging(this.uriChargeurURI) ;
				
				
		//--------------------------------------------------------------
		//-------------------------BATTERIE-----------------------------
		//--------------------------------------------------------------
		this.uriBatterieURI =
				AbstractComponent.createComponent(
						Batterie.class.getCanonicalName(),
						new Object[]{BATTERIE_COMPONENT_URI,
								URIBatterieOutboundPortURI,
								URIBatterieInboundPortURI}) ;
		assert	this.isDeployedComponent(this.uriBatterieURI) ;
		this.toggleTracing(this.uriBatterieURI) ;
		this.toggleLogging(this.uriBatterieURI) ;

		//--------------------------------------------------------------
		//-------------------------CONTROLEUR---------------------------
		//--------------------------------------------------------------
		this.uriControleurURI =
				AbstractComponent.createComponent(
						Controleur.class.getCanonicalName(),
						new Object[]{CONTROLEUR_COMPONENT_URI,
								URIControleurEolienneOutboundPortURI,
								URIControleurEolienneInboundPortURI,
								URIControleurBouilloireOutboundPortURI,
								URIControleurBouilloireInboundPortURI,
								URIControleurChauffageOutboundPortURI,
								URIControleurChauffageInboundPortURI,
								URIControleurCompteurOutboundPortURI,
								URIControleurCompteurInboundPortURI,
								URIControleurChargeurOutboundPortURI,
								URIControleurChargeurInboundPortURI,
								URIControleurBatterieOutboundPortURI,
								URIControleurBatterieInboundPortURI,
								URICapteurVentOutboundPortURI,
								URICapteurChaleurOutboundPortURI}) ;
		assert	this.isDeployedComponent(this.uriControleurURI) ;
		this.toggleTracing(this.uriControleurURI) ;
		this.toggleLogging(this.uriControleurURI) ;


//--------------------------------------------------------------
//-------------------------CONNECTION PHASE---------------------
//--------------------------------------------------------------

		//BOUILLOIRE <=> CONTROLEUR
		this.doPortConnection(
				this.uriBouilloireURI,
				URIBouilloireOutboundPortURI,
				URIControleurBouilloireInboundPortURI,
				BouilloireControleurConnector.class.getCanonicalName()) ;
		this.doPortConnection(
				this.uriControleurURI,
				URIControleurBouilloireOutboundPortURI,
				URIBouilloireInboundPortURI,
				ControleurConnector.class.getCanonicalName()) ;	

		
		//CHAUFFAGE <=> CONTROLEUR
		this.doPortConnection(
				this.uriChauffageURI,
				URIChauffageOutboundPortURI,
				URIControleurChauffageInboundPortURI,
				ChauffageControleurConnector.class.getCanonicalName()) ;
		this.doPortConnection(
				this.uriControleurURI,
				URIControleurChauffageOutboundPortURI,
				URIChauffageInboundPortURI,
				ControleurConnector.class.getCanonicalName()) ;	
		
		
		//COMPTEUR <=> CONTROLEUR
		this.doPortConnection(
				this.uriCompteurURI,
				URICompteurOutboundPortURI,
				URIControleurCompteurInboundPortURI,
				CompteurControleurConnector.class.getCanonicalName()) ;
		this.doPortConnection(
				this.uriControleurURI,
				URIControleurCompteurOutboundPortURI,
				URICompteurInboundPortURI,
				ControleurConnector.class.getCanonicalName()) ;	
				
		
		//EOLIENNE <=> CONTROLEUR
		this.doPortConnection(
				this.uriEolienneURI,
				URIEolienneOutboundPortURI,
				URIControleurEolienneInboundPortURI,
				EolienneControleurConnector.class.getCanonicalName()) ;
		this.doPortConnection(
				this.uriControleurURI,
				URIControleurEolienneOutboundPortURI,
				URIEolienneInboundPortURI,
				ControleurConnector.class.getCanonicalName()) ;	

		
		//CAPTEUR <=> CONTROLEUR
		this.doPortConnection(
				this.uriControleurURI,
				URICapteurVentOutboundPortURI,
				URICapteurVentInboundPortURI,
				ControleurConnector.class.getCanonicalName()) ;
		this.doPortConnection(
				this.uriControleurURI,
				URICapteurChaleurOutboundPortURI,
				URICapteurChaleurInboundPortURI,
				ControleurConnector.class.getCanonicalName()) ;
		
		
		//CHARGEUR <=> CONTROLEUR
		this.doPortConnection(
				this.uriChargeurURI,
				URIChargeurOutboundPortURI,
				URIControleurChargeurInboundPortURI,
				ChargeurControleurConnector.class.getCanonicalName()) ;
		this.doPortConnection(
				this.uriControleurURI,
				URIControleurChargeurOutboundPortURI,
				URIChargeurInboundPortURI,
				ControleurConnector.class.getCanonicalName()) ;	
				
		
		//BATTERIE <=> CONTROLEUR
		this.doPortConnection(
				this.uriBatterieURI,
				URIBatterieOutboundPortURI,
				URIControleurBatterieInboundPortURI,
				BatterieControleurConnector.class.getCanonicalName()) ;
		this.doPortConnection(
				this.uriControleurURI,
				URIControleurBatterieOutboundPortURI,
				URIBatterieInboundPortURI,
				ControleurConnector.class.getCanonicalName()) ;	
		
		//CAPTEUR => CHAUFFAGE
		this.doPortConnection(
				this.uriCapteurChaleurURI,
				URICapteurChaleurToChauffageOutboundPortURI,
				URIChauffageToCapteurInboundPortURI,
				CapteurChauffageConnector.class.getCanonicalName()) ;
 		

//--------------------------------------------------------------
//-------------------------DEPLOYMENT PHASE---------------------
//--------------------------------------------------------------
		super.deploy();
		assert this.deploymentDone() ;
	}
	
//--------------------------------------------------------------
//-------------------------FINALISE-----------------------------
//--------------------------------------------------------------
	@Override
	public void	finalise() throws Exception{
		super.finalise();
	}

//--------------------------------------------------------------
//-------------------------SHUTDOWN-----------------------------
//--------------------------------------------------------------
	@Override
	public void	shutdown() throws Exception{
		assert this.allFinalised() ;
		super.shutdown();
	}

//--------------------------------------------------------------
//-------------------------MAIN---------------------------------
//--------------------------------------------------------------	
	public static void main(String[] args) {
		try {
			CVM a = new CVM();
			a.startStandardLifeCycle(20000L);
			Thread.sleep(5000L);
			System.exit(0);
		} catch (Exception e) {throw new RuntimeException(e);}
	}

//--------------------------------------------------------------
//-------------------------DO_PORT_CONNECTION-------------------
//--------------------------------------------------------------
	@Override
	public void	doPortConnection(
			String componentURI,
			String outboundPortURI,
			String inboundPortURI,
			String connectorClassname) throws Exception{
		assert componentURI != null && outboundPortURI != null &&
				inboundPortURI != null && connectorClassname != null ;
		assert this.isDeployedComponent(componentURI) ;
		this.uri2component.get(componentURI)
						  .doPortConnection(outboundPortURI, inboundPortURI, connectorClassname);
	}
}
