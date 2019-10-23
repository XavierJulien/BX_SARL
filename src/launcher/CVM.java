package launcher;


import components.Bouilloire;
import components.CapteurVent;
import components.Chauffage;
import components.Controleur;
import components.Eolienne;
import connectors.BouilloireControleurConnector;
import connectors.ChauffageControleurConnector;
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

import fr.sorbonne_u.components.cvm.AbstractCVM;

//-----------------------------------------------------------------------------
/**
 * The class <code>CVM</code> implements the single JVM assembly for the basic
 * client/server example.
 *
 * <p><strong>Description</strong></p>
 * 
 * An URI provider component defined by the class <code>URIProvider</code>
 * offers an URI creation service, which is used by an URI consumer component
 * defined by the class <code>URIConsumer</code>. Both are deployed within a
 * single JVM.
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
public class CVM extends AbstractCVM {
	/** URI of the eolienne component (convenience).						*/
	public static final String	EOLIENNE_COMPONENT_URI = "my-URI-eolienne" ;
	/** URI of the bouilloire component (convenience).						*/
	public static final String	BOUILLOIRE_COMPONENT_URI = "my-URI-bouilloire" ;
	/** URI of the chauffage component (convenience).						*/
	public static final String	CHAUFFAGE_COMPONENT_URI = "my-URI-chauffage" ;
	/** URI of the controleur component (convenience).						*/
	public static final String	CONTROLEUR_COMPONENT_URI = "my-URI-controleur" ;
	/** URI of the capteur component (convenience).						*/
	public static final String	CAPTEUR_COMPONENT_URI = "my-URI-capteur" ;

	/** URI of the eolienne outbound port (simplifies the connection).	*/
	protected static final String	URIEolienneOutboundPortURI = "eolienneOPort" ;
	/** URI of the eolienne inbound port (simplifies the connection).		*/
	protected static final String	URIEolienneInboundPortURI = "eolienneIPort" ;	

	/** URI of the bouilloire outbound port (simplifies the connection).	*/
	protected static final String	URIBouilloireOutboundPortURI = "bouilloireOPort" ;
	/** URI of the bouilloire inbound port (simplifies the connection).		*/
	protected static final String	URIBouilloireInboundPortURI = "bouilloireIPort" ;	

	/** URI of the bouilloire outbound port (simplifies the connection).	*/
	protected static final String	URIChauffageOutboundPortURI = "chauffageOPort" ;
	/** URI of the bouilloire inbound port (simplifies the connection).		*/
	protected static final String	URIChauffageInboundPortURI = "chauffageIPort" ;	

	/** URI of the controleur outbound port (simplifies the connection).		*/
	protected static final String	URIControleurOutboundPortURI = "controleurOPort" ;
	/** URI of the controleur inbound port (simplifies the connection).		*/
	protected static final String	URIControleurInboundPortURI = "controleurIPort" ;
	/** URI of the controleur outbound port (simplifies the connection).		*/
	protected static final String	URIControleurBouilloireOutboundPortURI = "controleurBouilloireOPort" ;
	/** URI of the controleur inbound port (simplifies the connection).		*/
	protected static final String	URIControleurBouilloireInboundPortURI = "controleurBouilloireIPort" ;

	/** URI of the controleur outbound port (simplifies the connection).		*/
	protected static final String	URIControleurChauffageOutboundPortURI = "controleurChauffageOPort" ;
	/** URI of the controleur inbound port (simplifies the connection).		*/
	protected static final String	URIControleurChauffageInboundPortURI = "controleurChauffageIPort" ;





	/** URI of the capteurVent outbound port (simplifies the connection).	*/
	protected static final String	URICapteurVentOutboundPortURI = "capteurVentOPort" ;
	/** URI of the capteurVent inbound port (simplifies the connection).		*/
	protected static final String	URICapteurVentInboundPortURI = "capteurVentIPort" ;



	protected CVM() throws Exception{
		super() ;
	}

	/** Reference to the eolienne component to share between deploy
	 *  and shutdown.													*/
	protected String uriEolienneURI ;
	/** Reference to the bouilloire component to share between deploy
	 *  and shutdown.													*/
	protected String uriBouilloireURI ;
	/** Reference to the bouilloire component to share between deploy
	 *  and shutdown.													*/
	protected String uriChauffageURI ;
	/** Reference to the controleur component to share between deploy
	 *  and shutdown.													*/
	protected String uriControleurURI ;
	/** Reference to the Capteur component to share between deploy
	 *  and shutdown.													*/	
	protected String uriCapteurURI ;


	/**
	 * instantiate the components, publish their port and interconnect them.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	!this.deploymentDone()
	 * post	this.deploymentDone()
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy()
	 */
	@Override
	public void	deploy() throws Exception
	{
		assert	!this.deploymentDone() ;

		// --------------------------------------------------------------------
		// Configuration phase
		// --------------------------------------------------------------------

		// debugging mode configuration; comment and uncomment the line to see
		// the difference
		// AbstractCVM.DEBUG_MODE.add(CVMDebugModes.PUBLIHSING) ;
		// AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CONNECTING) ;
		// AbstractCVM.DEBUG_MODE.add(CVMDebugModes.COMPONENT_DEPLOYMENT) ;

		// --------------------------------------------------------------------
		// Creation phase
		// --------------------------------------------------------------------

		// create the eolienne component
		this.uriEolienneURI =
				AbstractComponent.createComponent(
						Eolienne.class.getCanonicalName(),
						new Object[]{EOLIENNE_COMPONENT_URI,
								URIEolienneOutboundPortURI,
								URIEolienneInboundPortURI}) ;

		assert	this.isDeployedComponent(this.uriEolienneURI) ;
		// make it trace its operations; comment and uncomment the line to see
		// the difference
		this.toggleTracing(this.uriEolienneURI) ;
		this.toggleLogging(this.uriEolienneURI) ;

		// create the bouilloire component
		this.uriBouilloireURI =
				AbstractComponent.createComponent(
						Bouilloire.class.getCanonicalName(),
						new Object[]{BOUILLOIRE_COMPONENT_URI,
								URIBouilloireOutboundPortURI,
								URIBouilloireInboundPortURI}) ;

		assert	this.isDeployedComponent(this.uriBouilloireURI) ;
		this.toggleTracing(this.uriBouilloireURI) ;
		this.toggleLogging(this.uriBouilloireURI) ;

		// create the chauffage component
		this.uriChauffageURI =
				AbstractComponent.createComponent(
						Chauffage.class.getCanonicalName(),
						new Object[]{CHAUFFAGE_COMPONENT_URI,
								URIChauffageOutboundPortURI,
								URIChauffageInboundPortURI}) ;

		assert	this.isDeployedComponent(this.uriChauffageURI) ;
		this.toggleTracing(this.uriChauffageURI) ;
		this.toggleLogging(this.uriChauffageURI) ;


		// create the controleur component
		this.uriControleurURI =
				AbstractComponent.createComponent(
						Controleur.class.getCanonicalName(),
						new Object[]{CONTROLEUR_COMPONENT_URI,
								URIControleurOutboundPortURI,
								URIControleurInboundPortURI,
								URICapteurVentOutboundPortURI,
								URIControleurBouilloireOutboundPortURI,
								URIControleurBouilloireInboundPortURI,
								URIControleurChauffageOutboundPortURI,
								URIControleurChauffageInboundPortURI}) ;
		assert	this.isDeployedComponent(this.uriControleurURI) ;
		this.toggleTracing(this.uriControleurURI) ;
		this.toggleLogging(this.uriControleurURI) ;

		// create the capteurVent component
		this.uriCapteurURI =
				AbstractComponent.createComponent(
						CapteurVent.class.getCanonicalName(),
						new Object[]{CAPTEUR_COMPONENT_URI,
								URICapteurVentInboundPortURI}) ;
		assert	this.isDeployedComponent(this.uriCapteurURI) ;
		this.toggleTracing(this.uriCapteurURI) ;
		this.toggleLogging(this.uriCapteurURI) ;

		// --------------------------------------------------------------------
		// Connection phase
		// --------------------------------------------------------------------

		// do the connection

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
		//EOLIENNE <=> CONTROLEUR
		this.doPortConnection(
				this.uriEolienneURI,
				URIEolienneOutboundPortURI,
				URIControleurInboundPortURI,
				EolienneControleurConnector.class.getCanonicalName()) ;

		this.doPortConnection(
				this.uriControleurURI,
				URIControleurOutboundPortURI,
				URIEolienneInboundPortURI,
				ControleurConnector.class.getCanonicalName()) ;	

		//CAPTEUR <=> CAPTEUR
		this.doPortConnection(
				this.uriControleurURI,
				URICapteurVentOutboundPortURI,
				URICapteurVentInboundPortURI,
				ControleurConnector.class.getCanonicalName()) ;



		// Nota: the above use of the reference to the object representing
		// the URI consumer component is allowed only in the deployment
		// phase of the component virtual machine (to perform the static
		// interconnection of components in a static architecture) and
		// inside the concerned component (i.e., where the method
		// doPortConnection can be called with the this destination
		// (this.doPortConenction(...)). It must never be used in another
		// component as the references to objects used to implement component
		// features must not be shared among components.

		// --------------------------------------------------------------------
		// Deployment done
		// --------------------------------------------------------------------

		super.deploy();
		assert	this.deploymentDone() ;
	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#finalise()
	 */
	@Override
	public void	finalise() throws Exception
	{
		// Port disconnections can be done here for static architectures
		// otherwise, they can be done in the finalise methods of components.
		super.finalise();

	}

	/**
	 * disconnect the components and then call the base shutdown method.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true				// no more preconditions.
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#shutdown()
	 */
	@Override
	public void	shutdown() throws Exception
	{
		assert	this.allFinalised() ;
		// any disconnection not done yet can be performed here

		super.shutdown();
	}

	public static void main(String[] args) {
		try {
			// Create an instance of the defined component virtual machine.
			CVM a = new CVM() ;
			// Execute the application.
			a.startStandardLifeCycle(2000L) ;
			// Give some time to see the traces (convenience).
			Thread.sleep(5000L) ;
			// Simplifies the termination (termination has yet to be treated
			// properly in BCM).
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}





	@Override
	public void			doPortConnection(
			String componentURI,
			String outboundPortURI,
			String inboundPortURI,
			String connectorClassname
			) throws Exception
	{
		assert	componentURI != null && outboundPortURI != null &&
				inboundPortURI != null && connectorClassname != null ;
		assert	this.isDeployedComponent(componentURI) ;
		this.uri2component.get(componentURI).doPortConnection(
				outboundPortURI, inboundPortURI, connectorClassname);
	}





}
