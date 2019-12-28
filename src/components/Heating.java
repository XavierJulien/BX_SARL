package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.heating.HeatingI;
import interfaces.heating.HeatingTemperatureSensorI;
import ports.heating.HeatingInboundPort;
import ports.heating.HeatingOutboundPort;
import ports.heating.HeatingTemperatureSensorInboundPort;

@RequiredInterfaces(required = {HeatingI.class, HeatingTemperatureSensorI.class})
@OfferedInterfaces(offered = {HeatingI.class, HeatingTemperatureSensorI.class})
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
	protected HeatingTemperatureSensorInboundPort		heatingToHeatSensorInboundPort ;
	protected boolean 							isOn = false;
	protected int 								maxPower = 10;
	protected int 								powerPercentage;
	protected int								maxConsumption = 10;
	protected int 								consumption;

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
		heatingToHeatSensorInboundPort = new HeatingTemperatureSensorInboundPort(heatingToHeatSensorInboundPortURI, this) ;
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
		powerPercentage = 10;
		consumption = (int) ((powerPercentage/100.0)*maxConsumption);
		isOn = true;
	}

	public void stopHeating() throws Exception{
		this.logMessage("The heating is stopping his job....") ;
		powerPercentage = 0;
		consumption = 0;
		isOn =false;
	}

	public void sendConsumption() throws Exception {
		this.logMessage("Sending comsumption....") ;
		this.heatingElectricMeterOutboundPort.sendConsumption((1.0/powerPercentage)*maxConsumption) ;
	}
	
	public void putExtraPowerInHeating(double power) throws Exception {
		powerPercentage = (int) Math.min(100, powerPercentage+power);
		this.logMessage("The heating is now running at "+powerPercentage+"% of his maximum power") ;
	}
	
	
	public void slowHeating(double power) throws Exception {
		powerPercentage = (int) Math.max(0, powerPercentage-power);
		this.logMessage("The heating is now running at "+powerPercentage+"% of his maximum power") ;
	}
	
	
	public double sendHeating() throws Exception {
		if(isOn) {
			this.logMessage("Sending Heat....") ;
			return maxPower*(powerPercentage/100.0);
		}else {
			return 0;
		}
		
		
		
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
