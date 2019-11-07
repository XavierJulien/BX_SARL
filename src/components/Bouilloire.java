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
	
	protected final String				uri ;
	protected final String				bouilloireInboundPortURI ;	
	protected final String				bouilloireOutboundPortURI ;
	protected BouilloireOutboundPort	bouilloireOutboundPort ;
	protected BouilloireInboundPort		bouilloireInboundPort ;
	protected boolean 					isOn=false;

	
	//------------------------------------------------------------------------
	//----------------------------CONSTRUCTOR---------------------------------
	//------------------------------------------------------------------------
	protected Bouilloire(String uri,
						 String bouilloireOutboundPortURI,
						 String bouilloireInboundPortURI) throws Exception{
		super(uri, 1, 1);

		assert uri != null;
		assert bouilloireOutboundPortURI != null;
		assert bouilloireInboundPortURI != null;

		this.uri = uri;
		this.bouilloireInboundPortURI = bouilloireInboundPortURI;
		this.bouilloireOutboundPortURI = bouilloireOutboundPortURI;

		//-------------------PUBLISH-------------------
		bouilloireInboundPort = new BouilloireInboundPort(bouilloireInboundPortURI, this) ;
		bouilloireInboundPort.publishPort() ;
		this.bouilloireOutboundPort = new BouilloireOutboundPort(bouilloireOutboundPortURI, this) ;
		this.bouilloireOutboundPort.localPublishPort() ;

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		//-------------------GUI-------------------
		this.tracer.setTitle(uri) ;
		this.tracer.setRelativePosition(2, 2) ;
	}


//------------------------------------------------------------------------
//----------------------------SERVICES------------------------------------
//------------------------------------------------------------------------
	
	public void startBouilloire() throws Exception{
		this.logMessage("The bouilloire is starting his job....") ;
		isOn = true;
	}

	public void stopBouilloire() throws Exception{
		this.logMessage("The bouilloire is stopping his job....") ;
		isOn =false;
	}
	
	public double sendConsommation() throws Exception {
		this.logMessage("Sending consommation....") ;
		return Math.random()*10;
	}

	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Bouilloire component.") ;
	}

	@Override
	public void execute() throws Exception {
		super.execute();
	}


//------------------------------------------------------------------------
//----------------------------FINALISE------------------------------------
//------------------------------------------------------------------------
	@Override
	public void finalise() throws Exception {
		bouilloireOutboundPort.doDisconnection();
		super.finalise();
	}

//------------------------------------------------------------------------
//----------------------------SHUTDOWN------------------------------------
//------------------------------------------------------------------------
	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			bouilloireInboundPort.unpublishPort();
			bouilloireOutboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
