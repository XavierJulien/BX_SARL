package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.battery.BatteryChargerI;
import interfaces.battery.BatteryI;
import ports.battery.BatteryInboundPort;
import ports.battery.BatteryOutboundPort;

@RequiredInterfaces(required = {BatteryI.class, BatteryChargerI.class})
@OfferedInterfaces(offered = {BatteryI.class, BatteryChargerI.class})
public class Battery extends AbstractComponent {

	protected final String				uri ;
	protected final String				batteryInboundPortURI ;	
	protected final String				batteryOutboundPortURI ;
	protected BatteryOutboundPort		batteryOutboundPort ;
	protected BatteryInboundPort		batteryInboundPort ;
	protected double 					prod;
	protected double 					maxCharge;
	protected double					chargePercentage;
	protected boolean 					isOn=false;

	
//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected Battery(String uri,
					   String batteryOutboundPortURI,
					   String batteryInboundPortURI) throws Exception{
		super(uri, 1, 1);

		assert uri != null;
		assert batteryOutboundPortURI != null;
		assert batteryInboundPortURI != null;

		this.uri = uri;
		this.batteryInboundPortURI = batteryInboundPortURI;
		this.batteryOutboundPortURI = batteryOutboundPortURI;
		this.prod = 0;

		//-------------------PUBLISH-------------------
		batteryInboundPort = new BatteryInboundPort(batteryInboundPortURI, this) ;
		batteryInboundPort.publishPort() ;
		batteryOutboundPort = new BatteryOutboundPort(batteryOutboundPortURI, this) ;
		batteryOutboundPort.localPublishPort() ;

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
		this.chargePercentage = 100;
		this.prod = 10;
		
		
		
	}


//------------------------------------------------------------------------
//----------------------------SERVICES------------------------------------
//------------------------------------------------------------------------

	public void startBattery() throws Exception{
		this.logMessage("The battery is starting his job....") ;
		isOn = true;
	}

	public void stopBattery() throws Exception{
		this.logMessage("The battery is stopping his job....") ;
		isOn =false;
	}

	public void sendChargePercentage() throws Exception {
		this.logMessage("Sending charge percentage....") ;
		this.batteryOutboundPort.sendChargePercentage(chargePercentage);
	}
	
	public void sendEnergy() throws Exception {
		this.logMessage("Sending energy....") ;
		this.batteryOutboundPort.sendEnergy(prod) ;
	}

	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Battery component.") ;
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
									((Battery)this.getTaskOwner()).sendEnergy();
									((Battery)this.getTaskOwner()).sendChargePercentage();
								}
								
								Thread.sleep(1000);
							}
							

						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				},
				1000, TimeUnit.MILLISECONDS);
	}


//------------------------------------------------------------------------
//----------------------------FINALISE------------------------------------
//------------------------------------------------------------------------
	@Override
	public void finalise() throws Exception {
		batteryOutboundPort.doDisconnection();
		super.finalise();
	}

//------------------------------------------------------------------------
//----------------------------SHUTDOWN------------------------------------
//------------------------------------------------------------------------
	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			batteryInboundPort.unpublishPort();
			batteryOutboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
