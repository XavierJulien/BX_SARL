package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.BatterieI;
import interfaces.BatterieToChargeurI;
import ports.BatterieInboundPort;
import ports.BatterieOutboundPort;

@RequiredInterfaces(required = {BatterieI.class, BatterieToChargeurI.class})
@OfferedInterfaces(offered = {BatterieI.class, BatterieToChargeurI.class})
public class Batterie extends AbstractComponent {

	protected final String				uri ;
	protected final String				batterieInboundPortURI ;	
	protected final String				batterieOutboundPortURI ;
	protected BatterieOutboundPort		batterieOutboundPort ;
	protected BatterieInboundPort		batterieInboundPort ;
	protected double 					prod;
	protected double 					maxCharge;
	protected double					chargePercentage;
	protected boolean 					isOn=false;

	
//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected Batterie(String uri,
					   String batterieOutboundPortURI,
					   String batterieInboundPortURI) throws Exception{
		super(uri, 1, 1);

		assert uri != null;
		assert batterieOutboundPortURI != null;
		assert batterieInboundPortURI != null;

		this.uri = uri;
		this.batterieInboundPortURI = batterieInboundPortURI;
		this.batterieOutboundPortURI = batterieOutboundPortURI;
		this.prod = 0;

		//-------------------PUBLISH-------------------
		batterieInboundPort = new BatterieInboundPort(batterieInboundPortURI, this) ;
		batterieInboundPort.publishPort() ;
		batterieOutboundPort = new BatterieOutboundPort(batterieOutboundPortURI, this) ;
		batterieOutboundPort.localPublishPort() ;

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		//-------------------GUI-------------------
		this.tracer.setTitle(uri) ;
		this.tracer.setRelativePosition(0, 3) ;
		
		//----------------Variables----------------
		
		this.maxCharge = 500;
		this.chargePercentage = 0;
		this.prod = 10;
		
		
		
	}


//------------------------------------------------------------------------
//----------------------------SERVICES------------------------------------
//------------------------------------------------------------------------

	public void startBatterie() throws Exception{
		this.logMessage("The batterie is starting his job....") ;
		isOn = true;
	}

	public void stopBatterie() throws Exception{
		this.logMessage("The batterie is stopping his job....") ;
		isOn =false;
	}

	public double sendChargePercentage() throws Exception {
		this.logMessage("Sending charge percentage....") ;
		return chargePercentage;
	}
	
	public double sendEnergy() throws Exception {
		this.logMessage("Sending energy....") ;
		return Math.random()*10;
	}

	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Batterie component.") ;
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
		batterieOutboundPort.doDisconnection();
		super.finalise();
	}

//------------------------------------------------------------------------
//----------------------------SHUTDOWN------------------------------------
//------------------------------------------------------------------------
	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			batterieInboundPort.unpublishPort();
			batterieOutboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
