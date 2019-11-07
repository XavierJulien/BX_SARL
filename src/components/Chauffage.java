package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.ChauffageI;
import ports.ChauffageCapteurInboundPort;
import ports.ChauffageInboundPort;
import ports.ChauffageOutboundPort;

@RequiredInterfaces(required = {ChauffageI.class})
@OfferedInterfaces(offered = {ChauffageI.class})
public class Chauffage extends AbstractComponent {
	
	protected final String				uri ;
	protected final String				chauffageInboundPortURI ;
	protected final String				chauffageToCapteurInboundPortURI ;
	protected final String				chauffageOutboundPortURI ;
	
	protected ChauffageOutboundPort		chauffageOutboundPort ;
	protected ChauffageInboundPort		chauffageInboundPort ;
	protected ChauffageCapteurInboundPort		chauffageToCapteurInboundPort ;
	protected boolean 					isOn=false;

	protected Chauffage(String uri,
						String chauffageOutboundPortURI,
						String chauffageInboundPortURI,
						String chauffageToCapteurInboundPortURI) throws Exception{
		super(uri, 1, 1);

		assert uri != null;
		assert chauffageOutboundPortURI != null;
		assert chauffageInboundPortURI != null;
		assert chauffageToCapteurInboundPortURI != null;

		this.uri = uri;
		this.chauffageInboundPortURI = chauffageInboundPortURI;
		this.chauffageOutboundPortURI = chauffageOutboundPortURI;
		this.chauffageToCapteurInboundPortURI = chauffageToCapteurInboundPortURI;

		//-------------------PUBLISH-------------------
		chauffageToCapteurInboundPort = new ChauffageCapteurInboundPort(chauffageToCapteurInboundPortURI, this) ;
		chauffageToCapteurInboundPort.publishPort() ;
		chauffageInboundPort = new ChauffageInboundPort(chauffageInboundPortURI, this) ;
		chauffageInboundPort.publishPort() ;
		this.chauffageOutboundPort = new ChauffageOutboundPort(chauffageOutboundPortURI, this) ;
		this.chauffageOutboundPort.localPublishPort() ;

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		//-------------------GUI-------------------
		this.tracer.setTitle(uri) ;
		this.tracer.setRelativePosition(3, 2) ;
	}


//------------------------------------------------------------------------
//----------------------------SERVICES------------------------------------
//------------------------------------------------------------------------
	
	public void startChauffage() throws Exception{
		this.logMessage("The chauffage is starting his job....") ;
		isOn = true;
	}

	public void stopChauffage() throws Exception{
		this.logMessage("The chauffage is stopping his job....") ;
		isOn =false;
	}

	public double sendConso() throws Exception {
		this.logMessage("Sending consommation....") ;
		return Math.random()*10;
	}
	
	
	public double sendHeating() throws Exception {
		this.logMessage("Sending Heat....") ;
		return Math.random()*10;
	}

	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Chauffage component.") ;
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
		chauffageOutboundPort.doDisconnection();
		super.finalise();
	}

//------------------------------------------------------------------------
//----------------------------SHUTDOWN------------------------------------
//------------------------------------------------------------------------
	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			chauffageInboundPort.unpublishPort();
			chauffageToCapteurInboundPort.unpublishPort();
			chauffageOutboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
