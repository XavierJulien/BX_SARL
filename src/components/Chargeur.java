package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.ChargeurCompteurI;
import interfaces.ChargeurI;
import interfaces.ChargeurToBatterieI;
import ports.ChargeurInboundPort;
import ports.ChargeurOutboundPort;

@RequiredInterfaces(required = {ChargeurI.class, ChargeurCompteurI.class, ChargeurToBatterieI.class})
@OfferedInterfaces(offered = {ChargeurI.class, ChargeurCompteurI.class, ChargeurToBatterieI.class})
public class Chargeur extends AbstractComponent{
	
	protected final String				uri ;
	protected final String				chargeurInboundPortURI ;	
	protected final String				chargeurOutboundPortURI ;
	protected final String				chargeurCompteurOutboundPortURI ;
	protected final String				chargeurCompteurInboundPortURI ;
	protected ChargeurOutboundPort		chargeurOutboundPort ;
	protected ChargeurInboundPort		chargeurInboundPort ;
	protected ChargeurOutboundPort		chargeurCompteurOutboundPort ;
	protected ChargeurInboundPort		chargeurCompteurInboundPort ;
	protected boolean 					isOn=false;
	protected final double 				conso = 10;
	
	
//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected Chargeur(String uri,
					   String chargeurOutboundPortURI,
					   String chargeurInboundPortURI,
					   String chargeurCompteurOutboundPortURI,
					   String chargeurCompteurInboundPortURI) throws Exception{
		super(uri, 1, 1);

		assert uri != null;
		assert chargeurOutboundPortURI != null;
		assert chargeurInboundPortURI != null;

		this.uri = uri;
		this.chargeurInboundPortURI = chargeurInboundPortURI;
		this.chargeurOutboundPortURI = chargeurOutboundPortURI;
		this.chargeurCompteurOutboundPortURI = chargeurCompteurOutboundPortURI;
		this.chargeurCompteurInboundPortURI = chargeurCompteurInboundPortURI;

		//-------------------PUBLISH-------------------
		chargeurInboundPort = new ChargeurInboundPort(chargeurInboundPortURI, this);
		chargeurInboundPort.publishPort();
		this.chargeurOutboundPort =	new ChargeurOutboundPort(chargeurOutboundPortURI, this);
		this.chargeurOutboundPort.localPublishPort();
		chargeurCompteurInboundPort = new ChargeurInboundPort(chargeurCompteurInboundPortURI, this);
		chargeurCompteurInboundPort.publishPort();
		this.chargeurCompteurOutboundPort =	new ChargeurOutboundPort(chargeurCompteurOutboundPortURI, this);
		this.chargeurCompteurOutboundPort.localPublishPort();

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		//-------------------GUI-------------------
		this.tracer.setTitle(uri) ;
		this.tracer.setRelativePosition(2, 3) ;
		
		
		
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
	
	public double sendConso() throws Exception {
		this.logMessage("Sending consommation....") ;
		return conso;
	}
	
	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Chargeur component.") ;
	}
	@Override
	public void execute() throws Exception{
		super.execute();
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							while(true) {
								((Chargeur)this.getTaskOwner()).sendConso() ;
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
		chargeurOutboundPort.doDisconnection();
		chargeurCompteurOutboundPort.doDisconnection();
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
			chargeurCompteurInboundPort.unpublishPort();
			chargeurCompteurOutboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
