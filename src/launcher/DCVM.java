package launcher;

import java.util.Vector;

import components.Controleur;
import components.Eolienne;
import connectors.ControleurConnector;
import connectors.EolienneControleurConnector;
import components.Batterie;
import components.Compteur;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;

public class DCVM extends AbstractDistributedCVM {

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
	protected static final String	URIBouilloireCompteurOutboundPortURI = "bouilloireCompteurOPort" ;
	protected static final String	URIBouilloireCompteurInboundPortURI = "bouilloireCompteurOPort" ;
	
	//--------------------------------------------------------------
	//-------------------------CHAUFFAGE----------------------------
	//--------------------------------------------------------------
	public static final String	CHAUFFAGE_COMPONENT_URI = "my-URI-chauffage" ;
	protected static final String	URIChauffageOutboundPortURI = "chauffageOPort" ;
	protected static final String	URIChauffageInboundPortURI = "chauffageIPort" ;
	protected static final String	URIChauffageToCapteurInboundPortURI = "chauffageToCapteurIPort" ;
	protected static final String	URIControleurChauffageOutboundPortURI = "controleurChauffageOPort" ;
	protected static final String	URIControleurChauffageInboundPortURI = "controleurChauffageIPort" ;
	protected static final String	URIChauffageCompteurOutboundPortURI = "chauffageCompteurOPort" ;
	protected static final String	URIChauffageCompteurInboundPortURI = "chauffageCompteurOPort" ;
	
	//--------------------------------------------------------------
	//-------------------------COMPTEUR-----------------------------
	//--------------------------------------------------------------
	/** URI of the compteur component (convenience).						*/
	public static final String	COMPTEUR_COMPONENT_URI = "my-URI-compteur" ;
	protected static final String	URICompteurOutboundPortURI = "compteurOPort" ;
	protected static final String	URICompteurInboundPortURI = "compteurIPort" ;	
	protected static final String	URIControleurCompteurOutboundPortURI = "controleurCompteurOPort" ;
	protected static final String	URIControleurCompteurInboundPortURI = "controleurCompteurIPort" ;
	protected static final String	URICompteurChauffageOutboundPortURI = "compteurChauffageOPort" ;
	protected static final String	URICompteurChauffageInboundPortURI = "compteurChauffageIPort" ;
	protected static final String	URICompteurBouilloireOutboundPortURI = "compteurBouilloireOPort" ;
	protected static final String	URICompteurBouilloireInboundPortURI = "compteurBouilloireIPort" ;
	protected static final String	URICompteurChargeurOutboundPortURI = "compteurChargeurOPort" ;
	protected static final String	URICompteurChargeurInboundPortURI = "compteurChargeurIPort" ;
	//--------------------------------------------------------------
	//-------------------------CHARGEUR-----------------------------
	//--------------------------------------------------------------
	public static final String	CHARGEUR_COMPONENT_URI = "my-URI-chargeur" ;
	protected static final String	URIChargeurOutboundPortURI = "chargeurOPort" ;
	protected static final String	URIChargeurInboundPortURI = "chargeurIPort" ;
	protected static final String	URIControleurChargeurOutboundPortURI = "controleurChargeurOPort" ;
	protected static final String	URIControleurChargeurInboundPortURI = "controleurChargeurIPort" ;
	protected static final String	URIChargeurCompteurOutboundPortURI = "chargeurCompteurOPort" ;
	protected static final String	URIChargeurCompteurInboundPortURI = "chargeurCompteurIPort" ;
	
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

	
	protected Vector<String> uris = new Vector<>();

	Controleur cont;
	Compteur cpt;
	Eolienne eolienne;
	Batterie batterie;


	public DCVM(String[] args, int xLayout, int yLayout) throws Exception {
		super(args, xLayout, yLayout);
		System.out.println("Constructeur");
		uris.add(CONTROLEUR_COMPONENT_URI);
		uris.add(EOLIENNE_COMPONENT_URI);
	}

	@Override
	public void instantiateAndPublish() throws Exception {

		System.out.println("instantiateAndPublish");

		if (thisJVMURI.equals(CONTROLEUR_COMPONENT_URI)) {
			
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
			
		} else if (thisJVMURI.equals(EOLIENNE_COMPONENT_URI)) {
			
			this.uriEolienneURI =
					AbstractComponent.createComponent(
							Eolienne.class.getCanonicalName(),
							new Object[]{EOLIENNE_COMPONENT_URI,
									URIEolienneOutboundPortURI,
									URIEolienneInboundPortURI}) ;
			assert	this.isDeployedComponent(this.uriEolienneURI) ;
			this.toggleTracing(this.uriEolienneURI) ;
			this.toggleLogging(this.uriEolienneURI) ;
			
		} else {
			throw new RuntimeException("Unknown JVM URI: " + thisJVMURI);
		}
		super.instantiateAndPublish();
	}

	@Override
	public void interconnect() throws Exception {
		System.out.println("Interconnexion");
		assert this.isIntantiatedAndPublished();

		if (thisJVMURI.equals(CONTROLEUR_COMPONENT_URI)) {
			assert	this.uriEolienneURI == null && this.uriControleurURI != null;
			//EOLIENNE <=> CONTROLEUR
			this.doPortConnection(
					this.uriControleurURI,
					URIControleurEolienneOutboundPortURI,
					URIEolienneInboundPortURI,
					ControleurConnector.class.getCanonicalName()) ;	
			
		} else if (thisJVMURI.equals(EOLIENNE_COMPONENT_URI)) {
			this.doPortConnection(
					this.uriEolienneURI,
					URIEolienneOutboundPortURI,
					URIControleurEolienneInboundPortURI,
					EolienneControleurConnector.class.getCanonicalName()) ;
		} else {
			System.out.println("Unknown JVM URI... " + thisJVMURI);
		}

		super.interconnect();
	}

	@Override
	public void			finalise() throws Exception
	{
		// Port disconnections can be done here for static architectures
		// otherwise, they can be done in the finalise methods of components.

		/*if (thisJVMURI.equals(CONTROLEUR_COMPONENT_URI)) {

			assert	this.uriEolienneURI == null && this.uriControleurURI != null;
			// nothing to be done on the provider side

		} else if (thisJVMURI.equals(EOLIENNE_COMPONENT_URI)) {

			assert	this.uriEolienneURI == null && this.uriControleurURI != null ;
			this.doPortDisconnection(this.uriConsumerURI, URIGetterOutboundPortURI) ;

		} else {

			System.out.println("Unknown JVM URI... " + thisJVMURI) ;

		}*/

		super.finalise() ;
	}
	
	@Override
	public void			shutdown() throws Exception
	{
		/*if (thisJVMURI.equals(Controleur_JVM_URI)) {

			assert	this.uriConsumerURI == null && this.uriProviderURI != null ;
			// any disconnection not done yet can be performed here

		} else if (thisJVMURI.equals(Appareils_JVM_URI)) {

			assert	this.uriConsumerURI != null && this.uriProviderURI == null ;
			// any disconnection not done yet can be performed here

		} else {

			System.out.println("Unknown JVM URI... " + thisJVMURI) ;

		}*/

		super.shutdown();
	}
	
	public static void main(String[] args) {
		try {
			DistributedCVM da = new DistributedCVM(args, 2, 5);
			da.startStandardLifeCycle(50000L);
			Thread.sleep(10000L);
			System.exit(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}