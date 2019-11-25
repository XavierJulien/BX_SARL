package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.HeatingI;
import interfaces.HeatingHeatSensorI;
import ports.HeatingHeatSensorInboundPort;
import ports.HeatingInboundPort;
import ports.HeatingOutboundPort;

@RequiredInterfaces(required = {HeatingI.class, HeatingHeatSensorI.class})
@OfferedInterfaces(offered = {HeatingI.class, HeatingHeatSensorI.class})
public class Heating extends AbstractComponent {
	
	protected final String						uri ;
	protected final String						heatingInboundPortURI ;
	protected final String						heatingOutboundPortURI ;
	protected final String						heatingElectricMeterOutboundPortURI ;
	protected final String						heatingElectricMeterInboundPortURI ;
	protected final String						heatingToHeatSensorInboundPortURI ;
	
	
	protected HeatingOutboundPort				heatingOutboundPort ;
	protected HeatingInboundPort				heatingInboundPort ;
	protected HeatingOutboundPort				heatingElectricMeterOutboundPort ;
	protected HeatingInboundPort				heatingElectricMeterInboundPort ;
	protected HeatingHeatSensorInboundPort		heatingToHeatSensorInboundPort ;
	protected boolean 							isOn=false;

	protected Heating(String uri,
						String heatingOutboundPortURI,
						String heatingInboundPortURI,
						String heatingToHeatSensorInboundPortURI,
						String heatingElectricMeterOutboundPortURI,
						String heatingElectricMeterInboundPortURI) throws Exception{
		super(uri, 1, 1);

		assert uri != null;
		assert heatingOutboundPortURI != null;
		assert heatingInboundPortURI != null;
		assert heatingToHeatSensorInboundPortURI != null;

		this.uri = uri;
		this.heatingInboundPortURI = heatingInboundPortURI;
		this.heatingOutboundPortURI = heatingOutboundPortURI;
		this.heatingElectricMeterOutboundPortURI = heatingElectricMeterOutboundPortURI;
		this.heatingElectricMeterInboundPortURI = heatingElectricMeterInboundPortURI;
		this.heatingToHeatSensorInboundPortURI = heatingToHeatSensorInboundPortURI;

		//-------------------PUBLISH-------------------
		heatingToHeatSensorInboundPort = new HeatingHeatSensorInboundPort(heatingToHeatSensorInboundPortURI, this) ;
		heatingToHeatSensorInboundPort.publishPort() ;
		heatingInboundPort = new HeatingInboundPort(heatingInboundPortURI, this) ;
		heatingInboundPort.publishPort() ;
		this.heatingOutboundPort = new HeatingOutboundPort(heatingOutboundPortURI, this) ;
		this.heatingOutboundPort.localPublishPort() ;
		
		heatingElectricMeterInboundPort = new HeatingInboundPort(heatingElectricMeterInboundPortURI, this) ;
		heatingElectricMeterInboundPort.publishPort() ;
		this.heatingElectricMeterOutboundPort = new HeatingOutboundPort(heatingElectricMeterOutboundPortURI, this) ;
		this.heatingElectricMeterOutboundPort.localPublishPort() ;

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
	
	public void startHeating() throws Exception{
		this.logMessage("The heating is starting his job....") ;
		isOn = true;
	}

	public void stopHeating() throws Exception{
		this.logMessage("The heating is stopping his job....") ;
		isOn =false;
	}

	public double sendConsumption() throws Exception {
		this.logMessage("Sending comsumption....") ;
		return Math.random()*10;
	}
	
	
	public double sendHeating() throws Exception {
		this.logMessage("Sending Heat....") ;
		return Math.random()*10;
	}

	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Heating component.") ;
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
								((Heating)this.getTaskOwner()).sendConsumption() ;
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
		heatingOutboundPort.doDisconnection();
		heatingElectricMeterOutboundPort.doDisconnection();
		super.finalise();
	}

//------------------------------------------------------------------------
//----------------------------SHUTDOWN------------------------------------
//------------------------------------------------------------------------
	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			heatingInboundPort.unpublishPort();
			heatingOutboundPort.unpublishPort();
			heatingElectricMeterInboundPort.unpublishPort();
			heatingElectricMeterOutboundPort.unpublishPort();
			heatingToHeatSensorInboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
