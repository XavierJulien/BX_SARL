package components;

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
	protected CompteurOutboundPort		compteurOutboundPort ;
	protected CompteurInboundPort		compteurInboundPort ;
	protected boolean 					isOn=false;

	
//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected Compteur(String uri,
					   String compteurOutboundPortURI,
					   String compteurInboundPortURI) throws Exception{
		super(uri, 1, 1);

		assert uri != null;
		assert compteurOutboundPortURI != null;
		assert compteurInboundPortURI != null;

		this.uri = uri;
		this.compteurInboundPortURI = compteurInboundPortURI;
		this.compteurOutboundPortURI = compteurOutboundPortURI;

		//-------------------PUBLISH-------------------
		compteurInboundPort = new CompteurInboundPort(compteurInboundPortURI, this) ;
		compteurInboundPort.publishPort() ;
		this.compteurOutboundPort = new CompteurOutboundPort(compteurOutboundPortURI, this) ;
		this.compteurOutboundPort.localPublishPort() ;

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
	
	public double sendConsommation() throws Exception {
		this.logMessage("Sending consommation....") ;

		return Math.random()*10;

	}

	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Compteur component.") ;
	}
	
	@Override
	public void execute() throws Exception{
		super.execute();
	}

	
//------------------------------------------------------------------------
//----------------------------FINALISE------------------------------------
//------------------------------------------------------------------------
	@Override
	public void finalise() throws Exception {
		compteurOutboundPort.doDisconnection();
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
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
