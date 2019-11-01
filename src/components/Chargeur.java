package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.ChargeurI;
import ports.ChargeurInboundPort;
import ports.ChargeurOutboundPort;

@RequiredInterfaces(required = {ChargeurI.class})
@OfferedInterfaces(offered = {ChargeurI.class})
public class Chargeur extends AbstractComponent{
	/** URI of the component (chargeur).*/
	protected final String				uri ;
	/** The inbound port URI for the chargeur.*/
	protected final String				chargeurInboundPortURI ;	
	/** The outbound port URI for the chargeur.*/
	protected final String				chargeurOutboundPortURI ;
	/** outbound port for the component (chargeur).*/
	protected ChargeurOutboundPort	chargeurOutboundPort ;
	/** inbound port for the component (chargeur).*/
	protected ChargeurInboundPort		chargeurInboundPort ;


	/** utility vars */
	protected boolean isOn=false;

	protected Chargeur(String uri,String chargeurOutboundPortURI,String chargeurInboundPortURI) throws Exception{
		super(uri, 1, 1);

		//check arguments 
		assert uri != null;
		assert chargeurOutboundPortURI != null;
		assert chargeurInboundPortURI != null;

		// init variables 
		this.uri = uri;

		chargeurInboundPort = new ChargeurInboundPort(chargeurInboundPortURI, this) ;

		// publish the port
		chargeurInboundPort.publishPort() ;

		this.chargeurInboundPortURI = chargeurInboundPortURI;
		this.chargeurOutboundPortURI = chargeurOutboundPortURI;

		this.chargeurOutboundPort =
				new ChargeurOutboundPort(chargeurOutboundPortURI, this) ;
		// publish the port (an outbound port is always local)
		this.chargeurOutboundPort.localPublishPort() ;

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		this.tracer.setTitle(uri) ;
		this.tracer.setRelativePosition(2, 2) ;




	}


	// ------------------------------------------------------------------------
	// Services
	// ------------------------------------------------------------------------

	public void startChargeur() throws Exception{
		this.logMessage("The chargeur is starting his job....") ;
		isOn = true;
	}

	public void stopChargeur() throws Exception{
		this.logMessage("The chargeur is stopping his job....") ;
		isOn =false;
	}


	public void			start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Chargeur component.") ;
	}

	// ------------------------------------------------------------------------
	// FINALISE / SHUTDOWN
	// ------------------------------------------------------------------------


	@Override
	public void finalise() throws Exception {
		chargeurOutboundPort.doDisconnection();
		super.finalise();
	}

	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			chargeurInboundPort.unpublishPort();
			chargeurOutboundPort.unpublishPort();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.shutdown();
	}

}
