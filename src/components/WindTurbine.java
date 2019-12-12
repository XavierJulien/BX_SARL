package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.windturbine.WindTurbineI;
import ports.windturbine.WindTurbineInboundPort;
import ports.windturbine.WindTurbineOutboundPort;

@RequiredInterfaces(required = {WindTurbineI.class})
@OfferedInterfaces(offered = {WindTurbineI.class})
public class WindTurbine extends AbstractComponent {

	protected final String				uri;
	protected final String				windTurbineInboundPortURI;	
	protected final String				windTurbineOutboundPortURI;
	protected WindTurbineOutboundPort	windTurbineOutboundPort;
	protected WindTurbineInboundPort	windTurbineInboundPort;
	protected double 					prod;
	protected boolean 					isOn=false;

	
//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected WindTurbine(String uri,
					   String windTurbineOutboundPortURI,
					   String windTurbineInboundPortURI) throws Exception{
		super(uri, 1, 1);

		assert uri != null;
		assert windTurbineOutboundPortURI != null;
		assert windTurbineInboundPortURI != null;

		this.uri = uri;
		this.windTurbineInboundPortURI = windTurbineInboundPortURI;
		this.windTurbineOutboundPortURI = windTurbineOutboundPortURI;
		this.prod = 0;

		//-------------------PUBLISH-------------------
		windTurbineInboundPort = new WindTurbineInboundPort(windTurbineInboundPortURI, this);
		windTurbineInboundPort.publishPort() ;
		windTurbineOutboundPort = new WindTurbineOutboundPort(windTurbineOutboundPortURI, this);
		windTurbineOutboundPort.localPublishPort() ;
		
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

	public void startWindTurbine() throws Exception{
		this.logMessage("The wind Turbine is starting his job....") ;
		isOn = true;
	}

	public void stopWindTurbine() throws Exception{
		this.logMessage("The wind Turbine is stopping his job....") ;
		isOn =false;
	}
	
	public double sendProduction() throws Exception {
		this.logMessage("Sending energy....") ;
		return Math.random()*10;
	}

	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Wind Turbine component.") ;
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
		windTurbineOutboundPort.doDisconnection();
		super.finalise();
	}

//------------------------------------------------------------------------
//----------------------------SHUTDOWN------------------------------------
//------------------------------------------------------------------------
	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			windTurbineInboundPort.unpublishPort();
			windTurbineOutboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
