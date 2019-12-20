package components;

import java.util.concurrent.TimeUnit;

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
	protected final String				windTurbineSensorInboundPortURI;
	protected WindTurbineOutboundPort	windTurbineOutboundPort;
	protected WindTurbineInboundPort	windTurbineSensorInboundPort;
	protected WindTurbineInboundPort	windTurbineInboundPort;
	protected double 					prod;
	protected boolean 					isOn=false;
	protected double 					windSpeed;

	
//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected WindTurbine(String uri,
			String windTurbineOutboundPortURI,
			String windTurbineSensorInboundPortURI,
					   String windTurbineInboundPortURI) throws Exception{
		super(uri, 1, 1);

		assert uri != null;
		assert windTurbineOutboundPortURI != null;
		assert windTurbineInboundPortURI != null;
		assert windTurbineSensorInboundPortURI != null;
		
		this.uri = uri;
		this.windTurbineInboundPortURI = windTurbineInboundPortURI;
		this.windTurbineOutboundPortURI = windTurbineOutboundPortURI;
		this.windTurbineSensorInboundPortURI = windTurbineSensorInboundPortURI;
		this.prod = 0;
		this.windSpeed = 0;

		//-------------------PUBLISH-------------------
		windTurbineInboundPort = new WindTurbineInboundPort(windTurbineInboundPortURI, this);
		windTurbineInboundPort.publishPort() ;
		windTurbineOutboundPort = new WindTurbineOutboundPort(windTurbineOutboundPortURI, this);
		windTurbineOutboundPort.localPublishPort() ;
		windTurbineSensorInboundPort = new WindTurbineInboundPort(windTurbineSensorInboundPortURI, this);
		windTurbineSensorInboundPort.publishPort() ;
		
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
	
	public void sendProduction() throws Exception {
		this.logMessage("Sending energy....") ;
		this.windTurbineOutboundPort.sendProduction(prod) ;
	}
	
	public void getWindSpeed(double speed) throws Exception{
		this.windSpeed = speed;
		this.logMessage("The wind power is "+ windSpeed) ;
	}

	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Wind Turbine component.") ;
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
								
								if(isOn) {
									((WindTurbine)this.getTaskOwner()).sendProduction();
								}
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
			windTurbineSensorInboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
