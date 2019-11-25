package components;

import java.util.concurrent.TimeUnit;

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
	protected final String				bouilloireCompteurOutboundPortURI ;
	protected final String				bouilloireCompteurInboundPortURI ;
	protected BouilloireOutboundPort	bouilloireOutboundPort ;
	protected BouilloireInboundPort		bouilloireInboundPort ;
	protected BouilloireOutboundPort	bouilloireCompteurOutboundPort ;
	protected BouilloireInboundPort		bouilloireCompteurInboundPort ;
	protected boolean 					isOn=false;
	protected final double 				conso = 10;

	
	//------------------------------------------------------------------------
	//----------------------------CONSTRUCTOR---------------------------------
	//------------------------------------------------------------------------
	protected Bouilloire(String uri,
						 String bouilloireOutboundPortURI,
						 String bouilloireInboundPortURI,
						 String bouilloireCompteurOutboundPortURI,
						 String bouilloireCompteurInboundPortURI) throws Exception{
		super(uri, 1, 1);

		assert uri != null;
		assert bouilloireOutboundPortURI != null;
		assert bouilloireInboundPortURI != null;

		this.uri = uri;
		this.bouilloireInboundPortURI = bouilloireInboundPortURI;
		this.bouilloireOutboundPortURI = bouilloireOutboundPortURI;
		this.bouilloireCompteurInboundPortURI = bouilloireCompteurInboundPortURI;
		this.bouilloireCompteurOutboundPortURI = bouilloireCompteurOutboundPortURI;

		//-------------------PUBLISH-------------------
		bouilloireInboundPort = new BouilloireInboundPort(bouilloireInboundPortURI, this) ;
		bouilloireInboundPort.publishPort() ;
		this.bouilloireOutboundPort = new BouilloireOutboundPort(bouilloireOutboundPortURI, this) ;
		this.bouilloireOutboundPort.localPublishPort() ;
		
		bouilloireCompteurInboundPort = new BouilloireInboundPort(bouilloireCompteurInboundPortURI, this) ;
		bouilloireCompteurInboundPort.publishPort() ;
		this.bouilloireCompteurOutboundPort = new BouilloireOutboundPort(bouilloireCompteurOutboundPortURI, this) ;
		this.bouilloireCompteurOutboundPort.localPublishPort() ;

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
	
	public double sendConso() throws Exception {
		this.logMessage("Sending consommation....") ;
		return conso;
	}

	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Bouilloire component.") ;
	}

	@Override
	public void execute() throws Exception {
		super.execute();
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							while(true) {
								((Bouilloire)this.getTaskOwner()).sendConso() ;
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
		bouilloireOutboundPort.doDisconnection();
		bouilloireCompteurOutboundPort.doDisconnection();
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
			bouilloireCompteurInboundPort.unpublishPort();
			bouilloireCompteurOutboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
