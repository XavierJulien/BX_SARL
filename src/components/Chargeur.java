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
	
	protected final String				uri ;
	protected final String				chargeurInboundPortURI ;	
	protected final String				chargeurOutboundPortURI ;
	protected ChargeurOutboundPort		chargeurOutboundPort ;
	protected ChargeurInboundPort		chargeurInboundPort ;
	protected boolean 					isOn=false;
	
	
	
	//Variables
	
	protected double conso;
	

//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected Chargeur(String uri,
					   String chargeurOutboundPortURI,
					   String chargeurInboundPortURI) throws Exception{
		super(uri, 1, 1);

		assert uri != null;
		assert chargeurOutboundPortURI != null;
		assert chargeurInboundPortURI != null;

		this.uri = uri;
		this.chargeurInboundPortURI = chargeurInboundPortURI;
		this.chargeurOutboundPortURI = chargeurOutboundPortURI;

		//-------------------PUBLISH-------------------
		chargeurInboundPort = new ChargeurInboundPort(chargeurInboundPortURI, this);
		chargeurInboundPort.publishPort();
		this.chargeurOutboundPort =	new ChargeurOutboundPort(chargeurOutboundPortURI, this);
		this.chargeurOutboundPort.localPublishPort();

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		//-------------------GUI-------------------
		this.tracer.setTitle(uri) ;
		this.tracer.setRelativePosition(2, 3) ;
		
		
		
		
		//---------------Variables----------------
		
		this.conso = 10;
	}

	
//------------------------------------------------------------------------
//----------------------------SERVICES------------------------------------
//------------------------------------------------------------------------
	public void startChargeur() throws Exception{
		this.logMessage("The chargeur is starting his job....") ;
		isOn = true;
	}

	public void stopChargeur() throws Exception{
		this.logMessage("The chargeur is stopping his job....") ;
		isOn =false;
	}

	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Chargeur component.") ;
	}
	
	public double sendConso() throws Exception{
		this.logMessage("Sending consommation");
		return conso;
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
		chargeurOutboundPort.doDisconnection();
		super.finalise();
	}

//------------------------------------------------------------------------
//----------------------------SHUTDOWN------------------------------------
//------------------------------------------------------------------------
	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			chargeurInboundPort.unpublishPort();
			chargeurOutboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
