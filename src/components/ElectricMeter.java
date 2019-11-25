package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.ElectricMeterControllerI;
import interfaces.ElectricMeterI;
import ports.ElectricMeterInboundPort;
import ports.ElectricMeterOutboundPort;

@RequiredInterfaces(required = {ElectricMeterI.class, ElectricMeterControllerI.class})
@OfferedInterfaces(offered = {ElectricMeterI.class, ElectricMeterControllerI.class})
public class ElectricMeter extends AbstractComponent {
	
	protected final String				uri ;
	protected final String				electricMeterInboundPortURI ;	
	protected final String				electricMeterOutboundPortURI ;
	protected final String				electricMeterHeatingInboundPortURI ;	
	protected final String				electricMeterHeatingOutboundPortURI ;
	protected final String				electricMeterKettleInboundPortURI ;	
	protected final String				electricMeterKettleOutboundPortURI ;
	protected final String				electricMeterChargerInboundPortURI ;	
	protected final String				electricMeterChargerOutboundPortURI ;

	
	protected ElectricMeterOutboundPort		electricMeterOutboundPort ;
	protected ElectricMeterInboundPort		electricMeterInboundPort ;
	protected ElectricMeterOutboundPort		electricMeterHeatingOutboundPort ;
	protected ElectricMeterInboundPort		electricMeterHeatingInboundPort ;
	protected ElectricMeterOutboundPort		electricMeterKettleOutboundPort ;
	protected ElectricMeterInboundPort		electricMeterKettleInboundPort ;
	protected ElectricMeterOutboundPort		electricMeterChargerOutboundPort ;
	protected ElectricMeterInboundPort		electricMeterChargerInboundPort ;
	
	protected boolean 					isOn=false;
	protected double					consumptionHeating = 0;
	protected double					consumptionCharger = 0;
	protected double					consumptionKettle = 0;


	
//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected ElectricMeter(String uri,
					   String electricMeterOutboundPortURI,
					   String electricMeterInboundPortURI,
					   String electricMeterHeatingOutboundPortURI,
					   String electricMeterHeatingInboundPortURI,
					   String electricMeterKettleOutboundPortURI,
					   String electricMeterKettleInboundPortURI,
					   String electricMeterChargerOutboundPortURI,
					   String electricMeterChargerInboundPortURI) throws Exception{
		super(uri, 1, 1);

		assert uri != null;
		assert electricMeterOutboundPortURI != null;
		assert electricMeterInboundPortURI != null;

		this.uri = uri;
		this.electricMeterInboundPortURI = electricMeterInboundPortURI;
		this.electricMeterOutboundPortURI = electricMeterOutboundPortURI;
		this.electricMeterHeatingOutboundPortURI = electricMeterHeatingOutboundPortURI;
		this.electricMeterHeatingInboundPortURI = electricMeterHeatingInboundPortURI;
		this.electricMeterKettleOutboundPortURI = electricMeterKettleOutboundPortURI;
		this.electricMeterKettleInboundPortURI = electricMeterKettleInboundPortURI;
		this.electricMeterChargerOutboundPortURI = electricMeterChargerOutboundPortURI;
		this.electricMeterChargerInboundPortURI = electricMeterChargerInboundPortURI;

		//-------------------PUBLISH-------------------
		electricMeterInboundPort = new ElectricMeterInboundPort(electricMeterInboundPortURI, this) ;
		electricMeterInboundPort.publishPort() ;
		this.electricMeterOutboundPort = new ElectricMeterOutboundPort(electricMeterOutboundPortURI, this) ;
		this.electricMeterOutboundPort.localPublishPort() ;
		
		electricMeterHeatingInboundPort = new ElectricMeterInboundPort(electricMeterHeatingInboundPortURI, this) ;
		electricMeterHeatingInboundPort.publishPort() ;
		this.electricMeterHeatingOutboundPort = new ElectricMeterOutboundPort(electricMeterHeatingOutboundPortURI, this) ;
		this.electricMeterHeatingOutboundPort.localPublishPort() ;
		
		electricMeterKettleInboundPort = new ElectricMeterInboundPort(electricMeterKettleInboundPortURI, this) ;
		electricMeterKettleInboundPort.publishPort() ;
		this.electricMeterKettleOutboundPort = new ElectricMeterOutboundPort(electricMeterKettleOutboundPortURI, this) ;
		this.electricMeterKettleOutboundPort.localPublishPort() ;
		
		electricMeterChargerInboundPort = new ElectricMeterInboundPort(electricMeterChargerInboundPortURI, this) ;
		electricMeterChargerInboundPort.publishPort() ;
		this.electricMeterChargerOutboundPort = new ElectricMeterOutboundPort(electricMeterChargerOutboundPortURI, this) ;
		this.electricMeterChargerOutboundPort.localPublishPort() ;

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		//-------------------GUI-------------------
		this.tracer.setTitle(uri) ;
		this.tracer.setRelativePosition(3, 3) ;
	}


//------------------------------------------------------------------------
//----------------------------SERVICES------------------------------------
//------------------------------------------------------------------------
	
	public void startElectricMeter() throws Exception{
		this.logMessage("The electric Meter is starting his job....") ;
		isOn = true;
	}

	public void stopElectricMeter() throws Exception{
		this.logMessage("The electric Meter is stopping his job....") ;
		isOn =false;
	}
	
	public double sendAllConsumption() throws Exception {
		this.logMessage("Sending all comsumption....") ;
		return consumptionKettle+consumptionCharger+consumptionHeating;
	}
	
	public void getHeatingConsumption() throws Exception {
		double consumption = this.electricMeterHeatingOutboundPort.getHeatingConsumption();
		this.consumptionHeating = consumption;
		this.logMessage("The electric Meter is informed that the heating consumes "+consumption+" units of energy.") ;
	}
	
	public void getKettleConsumption() throws Exception {
		double consumption = this.electricMeterKettleOutboundPort.getKettleConsumption();
		this.consumptionKettle = consumption;
		this.logMessage("The electric Meter is informed that the kettle consumes "+consumption+" units of energy.") ;
	}
	
	public void getChargerConsumption() throws Exception {
		double consumption = this.electricMeterChargerOutboundPort.getChargerConsumption();
		this.consumptionCharger = consumption;
		this.logMessage("The electric Meter is informed that the charger consumes "+consumption+" units of energy.") ;
	}
	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Electric Meter component.") ;
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
								((ElectricMeter)this.getTaskOwner()).getHeatingConsumption() ;
								((ElectricMeter)this.getTaskOwner()).getKettleConsumption() ;
								((ElectricMeter)this.getTaskOwner()).getChargerConsumption() ;
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
		electricMeterOutboundPort.doDisconnection();
		electricMeterHeatingOutboundPort.doDisconnection();
		electricMeterKettleOutboundPort.doDisconnection();
		electricMeterChargerOutboundPort.doDisconnection();
		super.finalise();
	}

//------------------------------------------------------------------------
//----------------------------SHUTDOWN------------------------------------
//------------------------------------------------------------------------
	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			electricMeterInboundPort.unpublishPort();
			electricMeterOutboundPort.unpublishPort();
			electricMeterHeatingInboundPort.unpublishPort();
			electricMeterHeatingOutboundPort.unpublishPort();
			electricMeterKettleInboundPort.unpublishPort();
			electricMeterKettleOutboundPort.unpublishPort();
			electricMeterChargerInboundPort.unpublishPort();
			electricMeterChargerOutboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
