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
import connectors.BouilloireCompteurConnector;
import connectors.BouilloireControleurConnector;
import connectors.CapteurChauffageConnector;
import connectors.ChargeurCompteurConnector;
import connectors.ChargeurControleurConnector;
import connectors.ChauffageCompteurConnector;
import connectors.ChauffageControleurConnector;
import connectors.CompteurConnector;
import connectors.CompteurControleurConnector;
import connectors.ControleurConnector;
import connectors.EolienneControleurConnector;
import fr.sorbonne_u.components.AbstractComponent;

//Copyright Jacques Malenfant, Sorbonne Universite.
//
//Jacques.Malenfant@lip6.fr
//
//This software is a computer program whose purpose is to provide a
//basic component programming model to program with components
//distributed applications in the Java programming language.
//
//This software is governed by the CeCILL-C license under French law and
//abiding by the rules of distribution of free software.  You can use,
//modify and/ or redistribute the software under the terms of the
//CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
//URL "http://www.cecill.info".
//
//As a counterpart to the access to the source code and  rights to copy,
//modify and redistribute granted by the license, users are provided only
//with a limited warranty  and the software's author,  the holder of the
//economic rights,  and the successive licensors  have only  limited
//liability. 
//
//In this respect, the user's attention is drawn to the risks associated
//with loading,  using,  modifying and/or developing or reproducing the
//software by the user in light of its specific status of free software,
//that may mean  that it is complicated to manipulate,  and  that  also
//therefore means  that it is reserved for developers  and  experienced
//professionals having in-depth computer knowledge. Users are therefore
//encouraged to load and test the software's suitability as regards their
//requirements in conditions enabling the security of their systems and/or 
//data to be ensured and,  more generally, to use and operate it in the 
//same conditions as regards security. 
//
//The fact that you are presently reading this means that you have had
//knowledge of the CeCILL-C license and that you accept its terms.

import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;
import fr.sorbonne_u.components.examples.basic_cs.components.URIConsumer;
import fr.sorbonne_u.components.examples.basic_cs.components.URIProvider;
import fr.sorbonne_u.components.examples.basic_cs.connectors.URIServiceConnector;

//-----------------------------------------------------------------------------
/**
 * The class <code>DistributedCVM</code> implements the multi-JVM assembly for
 * the basic client/server example.
 *
 * <p><strong>Description</strong></p>
 * 
 * An URI provider component defined by the class <code>URIProvider</code>
 * offers an URI creation service, which is used by an URI consumer component
 * defined by the class <code>URIConsumer</code>.
 * 
 * The URI provider is deployed within a JVM running an instance of the CVM
 * called <code>provider</code> in the <code>config.xml</code> file. The URI
 * consumer is deployed in the instance called <code>consumer</code>.
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2014-01-22</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class				DistributedCVM
extends		AbstractDistributedCVM
{
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
		protected static final String	URIBouilloireCompteurInboundPortURI = "bouilloireCompteurIPort" ;
		
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
		protected static final String	URIChauffageCompteurInboundPortURI = "chauffageCompteurIPort" ;
		
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
		protected static final String	URIControleurCapteurVentOutboundPortURI = "capteurVentOPort" ;
		protected static final String	URICapteurVentInboundPortURI = "capteurVentIPort" ;
		
		//--------------------------------------------------------------
		//-------------------------CAPTEUR CHALEUR----------------------
		//--------------------------------------------------------------
		public static final String	CAPTEUR_CHALEUR_COMPONENT_URI = "my-URI-capteur-chaleur" ;
		protected static final String	URICapteurChaleurToChauffageOutboundPortURI = "capteurChaleurToChauffageOPort" ;
		protected static final String	URIControleurCapteurChaleurOutboundPortURI = "capteurChaleurOPort" ;
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


	// URI of the CVM instances as defined in the config.xml file
	protected static String			Controleur_JVM_URI = "controleur" ;
	protected static String			Appareils_JVM_URI = "appareils" ;

	protected static String			URIGetterOutboundPortURI = "oport" ;
	protected static String			URIProviderInboundPortURI = "iport" ;

	
	/** Reference to the provider component to share between deploy
	 *  and shutdown.													*/
	protected String	uriProviderURI ;
	/** Reference to the consumer component to share between deploy
	 *  and shutdown.													*/
	protected String	uriConsumerURI ;

	public				DistributedCVM(String[] args, int xLayout, int yLayout)
	throws Exception
	{
		super(args, xLayout, yLayout);
	}

	/**
	 * do some initialisation before anything can go on.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true				// no more preconditions.
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.components.cvm.AbstractDistributedCVM#initialise()
	 */
	@Override
	public void			initialise() throws Exception
	{
		// debugging mode configuration; comment and uncomment the line to see
		// the difference
//		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.PUBLIHSING) ;
//		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CONNECTING) ;
//		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.COMPONENT_DEPLOYMENT) ;

		super.initialise() ;
		// any other application-specific initialisation must be put here

	}

	/**
	 * instantiate components and publish their ports.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true				// no more preconditions.
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.components.cvm.AbstractDistributedCVM#instantiateAndPublish()
	 */
	@Override
	public void			instantiateAndPublish() throws Exception
	{
		if (thisJVMURI.equals(Controleur_JVM_URI)) {

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
									URIControleurCapteurVentOutboundPortURI,
									URIControleurCapteurChaleurOutboundPortURI}) ;
			assert	this.isDeployedComponent(this.uriControleurURI) ;
			this.toggleTracing(this.uriControleurURI) ;
			this.toggleLogging(this.uriControleurURI) ;
			
			
			this.uriCompteurURI =
					AbstractComponent.createComponent(
							Compteur.class.getCanonicalName(),
							new Object[]{COMPTEUR_COMPONENT_URI,
									URICompteurOutboundPortURI,
									URICompteurInboundPortURI,
									URIChauffageCompteurOutboundPortURI,
									URIChauffageCompteurInboundPortURI,
									URIBouilloireCompteurOutboundPortURI,
									URIBouilloireCompteurInboundPortURI,
									URIChargeurCompteurOutboundPortURI,
									URIChargeurCompteurInboundPortURI}) ;

			assert	this.isDeployedComponent(this.uriCompteurURI) ;
			this.toggleTracing(this.uriCompteurURI) ;
			this.toggleLogging(this.uriCompteurURI) ;


		} else if (thisJVMURI.equals(Appareils_JVM_URI)) {
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
			//-------------------------CHAUFFAGE----------------------------
			//--------------------------------------------------------------
			this.uriChauffageURI =
					AbstractComponent.createComponent(
							Chauffage.class.getCanonicalName(),
							new Object[]{CHAUFFAGE_COMPONENT_URI,
									URIChauffageOutboundPortURI,
									URIChauffageInboundPortURI,
									URIChauffageToCapteurInboundPortURI,
									URICompteurChauffageOutboundPortURI,
									URICompteurChauffageInboundPortURI}) ;

			assert	this.isDeployedComponent(this.uriChauffageURI) ;
			this.toggleTracing(this.uriChauffageURI) ;
			this.toggleLogging(this.uriChauffageURI) ;
			
			
			//--------------------------------------------------------------
			//-------------------------BOUILLOIRE---------------------------
			//--------------------------------------------------------------
			this.uriBouilloireURI =
					AbstractComponent.createComponent(
							Bouilloire.class.getCanonicalName(),
							new Object[]{BOUILLOIRE_COMPONENT_URI,
									URIBouilloireOutboundPortURI,
									URIBouilloireInboundPortURI,
									URICompteurBouilloireOutboundPortURI,
									URICompteurBouilloireInboundPortURI}) ;

			assert	this.isDeployedComponent(this.uriBouilloireURI) ;
			this.toggleTracing(this.uriBouilloireURI) ;
			this.toggleLogging(this.uriBouilloireURI) ;


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
									URIChargeurInboundPortURI,
									URICompteurChargeurOutboundPortURI,
									URICompteurChargeurInboundPortURI});
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

			
			
			
			
		} else {

			System.out.println("Unknown JVM URI... " + thisJVMURI) ;

		}

		super.instantiateAndPublish();
	}

	/**
	 * interconnect the components.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true				// no more preconditions.
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.components.cvm.AbstractDistributedCVM#interconnect()
	 */
	@Override
	public void			interconnect() throws Exception
	{
		assert	this.isIntantiatedAndPublished() ;

		if (thisJVMURI.equals(Controleur_JVM_URI)) {

			assert	this.uriControleurURI != null
					&& this.uriCompteurURI != null;
			
			//BOUILLOIRE <=> CONTROLEUR
			this.doPortConnection(
					this.uriControleurURI,
					URIControleurBouilloireOutboundPortURI,
					URIBouilloireInboundPortURI,
					ControleurConnector.class.getCanonicalName()) ;	

			//BOUILLOIRE <=> COMPTEUR
			this.doPortConnection(
					this.uriCompteurURI,
					URIBouilloireCompteurOutboundPortURI,
					URICompteurBouilloireInboundPortURI,
					CompteurConnector.class.getCanonicalName()) ;
			
			//CHAUFFAGE <=> CONTROLEUR
			this.doPortConnection(
					this.uriControleurURI,
					URIControleurChauffageOutboundPortURI,
					URIChauffageInboundPortURI,
					ControleurConnector.class.getCanonicalName()) ;	
			
			//CHAUFFAGE <=> COMPTEUR
			this.doPortConnection(
					this.uriCompteurURI,
					URIChauffageCompteurOutboundPortURI,
					URICompteurChauffageInboundPortURI,
					CompteurConnector.class.getCanonicalName()) ;
	
			//CHARGEUR <=> COMPTEUR
			this.doPortConnection(
					this.uriCompteurURI,
					URIChargeurCompteurOutboundPortURI,
					URICompteurChargeurInboundPortURI,
					CompteurConnector.class.getCanonicalName()) ;	

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
					this.uriControleurURI,
					URIControleurEolienneOutboundPortURI,
					URIEolienneInboundPortURI,
					ControleurConnector.class.getCanonicalName()) ;	
			
			//CAPTEUR <=> CONTROLEUR
			this.doPortConnection(
					this.uriControleurURI,
					URIControleurCapteurVentOutboundPortURI,
					URICapteurVentInboundPortURI,
					ControleurConnector.class.getCanonicalName()) ;
			this.doPortConnection(
					this.uriControleurURI,
					URIControleurCapteurChaleurOutboundPortURI,
					URICapteurChaleurInboundPortURI,
					ControleurConnector.class.getCanonicalName()) ;
			
			//CHARGEUR <=> CONTROLEUR
			this.doPortConnection(
					this.uriControleurURI,
					URIControleurChargeurOutboundPortURI,
					URIChargeurInboundPortURI,
					ControleurConnector.class.getCanonicalName()) ;	
			
			//BATTERIE <=> CONTROLEUR
			this.doPortConnection(
					this.uriControleurURI,
					URIControleurBatterieOutboundPortURI,
					URIBatterieInboundPortURI,
					ControleurConnector.class.getCanonicalName()) ;	
			
		} else if (thisJVMURI.equals(Appareils_JVM_URI)) {

			assert	this.uriEolienneURI != null 
					 && this.uriCapteurChaleurURI != null && this.uriChauffageURI != null
					 &&	this.uriCapteurVentURI != null && this.uriBouilloireURI != null
					 &&	this.uriBatterieURI != null && this.uriChargeurURI != null;
			
			//BOUILLOIRE <=> CONTROLEUR
			this.doPortConnection(
					this.uriBouilloireURI,
					URIBouilloireOutboundPortURI,
					URIControleurBouilloireInboundPortURI,
					BouilloireControleurConnector.class.getCanonicalName()) ;

			//CHAUFFAGE <=> CONTROLEUR
			this.doPortConnection(
					this.uriChauffageURI,
					URIChauffageOutboundPortURI,
					URIControleurChauffageInboundPortURI,
					ChauffageControleurConnector.class.getCanonicalName()) ;

			//CHAUFFAGE <=> COMPTEUR
			this.doPortConnection(
					this.uriChauffageURI,
					URICompteurChauffageOutboundPortURI,
					URIChauffageCompteurInboundPortURI,
					ChauffageCompteurConnector.class.getCanonicalName()) ;

			//BOUILLOIRE <=> COMPTEUR
			this.doPortConnection(
					this.uriBouilloireURI,
					URICompteurBouilloireOutboundPortURI,
					URIBouilloireCompteurInboundPortURI,
					BouilloireCompteurConnector.class.getCanonicalName()) ;
			
			//CHARGEUR <=> COMPTEUR
			this.doPortConnection(
					this.uriChargeurURI,
					URICompteurChargeurOutboundPortURI,
					URIChargeurCompteurInboundPortURI,
					ChargeurCompteurConnector.class.getCanonicalName()) ;
			
			//EOLIENNE <=> CONTROLEUR
			this.doPortConnection(
					this.uriEolienneURI,
					URIEolienneOutboundPortURI,
					URIControleurEolienneInboundPortURI,
					EolienneControleurConnector.class.getCanonicalName()) ;
			
			//CHARGEUR <=> CONTROLEUR
			this.doPortConnection(
					this.uriChargeurURI,
					URIChargeurOutboundPortURI,
					URIControleurChargeurInboundPortURI,
					ChargeurControleurConnector.class.getCanonicalName()) ;

			//BATTERIE <=> CONTROLEUR
			this.doPortConnection(
					this.uriBatterieURI,
					URIBatterieOutboundPortURI,
					URIControleurBatterieInboundPortURI,
					BatterieControleurConnector.class.getCanonicalName()) ;

			//CAPTEUR => CHAUFFAGE
			this.doPortConnection(
					this.uriCapteurChaleurURI,
					URICapteurChaleurToChauffageOutboundPortURI,
					URIChauffageToCapteurInboundPortURI,
					CapteurChauffageConnector.class.getCanonicalName()) ;
			
		} else {

			System.out.println("Unknown JVM URI... " + thisJVMURI) ;

		}

		super.interconnect();
	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractDistributedCVM#finalise()
	 */
	@Override
	public void			finalise() throws Exception
	{
		if (thisJVMURI.equals(Controleur_JVM_URI)) {

			assert	this.uriControleurURI != null
					&& this.uriCompteurURI != null;

		} else if (thisJVMURI.equals(Appareils_JVM_URI)) {

			assert	this.uriEolienneURI != null 
					&& this.uriCapteurChaleurURI != null && this.uriChauffageURI != null
					&& this.uriCapteurVentURI != null && this.uriBouilloireURI != null
					&& this.uriBatterieURI != null && this.uriChargeurURI != null;
			
		} else {

			System.out.println("Unknown JVM URI... " + thisJVMURI) ;

		}

		super.finalise() ;
	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractDistributedCVM#shutdown()
	 */
	@Override
	public void			shutdown() throws Exception
	{
		if (thisJVMURI.equals(Controleur_JVM_URI)) {
			assert	this.uriControleurURI != null
					&& this.uriCompteurURI != null;
		} else if (thisJVMURI.equals(Appareils_JVM_URI)) {

			assert	this.uriEolienneURI != null 
					&& this.uriCapteurChaleurURI != null && this.uriChauffageURI != null
					&& this.uriCapteurVentURI != null && this.uriBouilloireURI != null
					&& this.uriBatterieURI != null && this.uriChargeurURI != null;

		} else {

			System.out.println("Unknown JVM URI... " + thisJVMURI) ;

		}

		super.shutdown();
	}

	public static void	main(String[] args)
	{
		try {
			DistributedCVM da  = new DistributedCVM(args, 2, 5) ;
			da.startStandardLifeCycle(15000L) ;
			Thread.sleep(10000L) ;
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
}
//-----------------------------------------------------------------------------
