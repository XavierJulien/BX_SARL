package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.BouilloireI;
import ports.BouilloireInboundPort;
import ports.BouilloireOutboundPort;

@RequiredInterfaces(required = {BouilloireI.class})
@OfferedInterfaces(offered = {BouilloireI.class})
public class Bouilloire extends AbstractComponent{
	/** URI of the component (bouilloire).*/
	protected final String				uri ;
	/** The inbound port URI for the bouilloire.*/
	protected final String				bouilloireInboundPortURI ;	
	/** The outbound port URI for the bouilloire.*/
	protected final String				bouilloireOutboundPortURI ;
	/** outbound port for the component (bouilloire).*/
	protected BouilloireOutboundPort	bouilloireOutboundPort ;
	/** inbound port for the component (bouilloire).*/
	protected BouilloireInboundPort		bouilloireInboundPort ;


	/** utility vars */
	protected boolean isOn=false;

	protected Bouilloire(String uri,String bouilloireOutboundPortURI,String bouilloireInboundPortURI) throws Exception{
		super(uri, 1, 1);

		//check arguments 
		assert uri != null;
		assert bouilloireOutboundPortURI != null;
		assert bouilloireInboundPortURI != null;

		// init variables 
		this.uri = uri;

		bouilloireInboundPort = new BouilloireInboundPort(bouilloireInboundPortURI, this) ;

		// publish the port
		bouilloireInboundPort.publishPort() ;

		this.bouilloireInboundPortURI = bouilloireInboundPortURI;
		this.bouilloireOutboundPortURI = bouilloireOutboundPortURI;

		this.bouilloireOutboundPort =
				new BouilloireOutboundPort(bouilloireOutboundPortURI, this) ;
		// publish the port (an outbound port is always local)
		this.bouilloireOutboundPort.localPublishPort() ;

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

	public void startBouilloire() throws Exception{
		this.logMessage("The bouilloire is starting his job....") ;
		isOn = true;
	}



	public double sendConsommation() throws Exception {
		this.logMessage("Sending consommation....") ;

		return Math.random()*10;

	}


	public void stopBouilloire() throws Exception{
		this.logMessage("The bouilloire is stopping his job....") ;
		isOn =false;
	}


	public void			start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Bouilloire component.") ;
	}

	// ------------------------------------------------------------------------
	// FINALISE / SHUTDOWN
	// ------------------------------------------------------------------------


	@Override
	public void finalise() throws Exception {
		bouilloireOutboundPort.doDisconnection();
		super.finalise();
	}

	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			bouilloireInboundPort.unpublishPort();
			bouilloireOutboundPort.unpublishPort();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.shutdown();
	}

}
