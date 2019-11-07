package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.EolienneI;
import ports.EolienneInboundPort;
import ports.EolienneOutboundPort;

@RequiredInterfaces(required = {EolienneI.class})
@OfferedInterfaces(offered = {EolienneI.class})
public class Eolienne extends AbstractComponent {

	protected final String				uri;
	protected final String				eolienneInboundPortURI;	
	protected final String				eolienneOutboundPortURI;
	protected EolienneOutboundPort		eolienneOutboundPort;
	protected EolienneInboundPort		eolienneInboundPort;
	protected double 					prod;
	protected boolean 					isOn=false;

	
//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected Eolienne(String uri,
					   String eolienneOutboundPortURI,
					   String eolienneInboundPortURI) throws Exception{
		super(uri, 1, 1);

		assert uri != null;
		assert eolienneOutboundPortURI != null;
		assert eolienneInboundPortURI != null;

		this.uri = uri;
		this.eolienneInboundPortURI = eolienneInboundPortURI;
		this.eolienneOutboundPortURI = eolienneOutboundPortURI;
		this.prod = 0;

		//-------------------PUBLISH-------------------
		eolienneInboundPort = new EolienneInboundPort(eolienneInboundPortURI, this);
		eolienneInboundPort.publishPort() ;
		eolienneOutboundPort = new EolienneOutboundPort(eolienneOutboundPortURI, this);
		eolienneOutboundPort.localPublishPort() ;
		
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir"));
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home"));
		}	

		//-------------------GUI-------------------
		this.tracer.setTitle(uri);
		this.tracer.setRelativePosition(0, 2);
	}


//------------------------------------------------------------------------
//----------------------------SERVICES------------------------------------
//------------------------------------------------------------------------

	public void startEolienne() throws Exception{
		this.logMessage("The eolienne is starting his job....") ;
		isOn = true;
	}

	public void stopEolienne() throws Exception{
		this.logMessage("The eolienne is stopping his job....") ;
		isOn =false;
	}
	
	public double sendProduction() throws Exception {
		this.logMessage("Sending energy....") ;
		return Math.random()*10;
	}

	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Eolienne component.") ;
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
		eolienneOutboundPort.doDisconnection();
		super.finalise();
	}

//------------------------------------------------------------------------
//----------------------------SHUTDOWN------------------------------------
//------------------------------------------------------------------------
	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			eolienneInboundPort.unpublishPort();
			eolienneOutboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
