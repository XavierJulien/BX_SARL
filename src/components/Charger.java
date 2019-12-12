package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.charger.ChargerBatteryI;
import interfaces.charger.ChargerElectricMeterI;
import interfaces.charger.ChargerI;
import ports.charger.ChargerInboundPort;
import ports.charger.ChargerOutboundPort;

@RequiredInterfaces(required = {ChargerI.class, ChargerElectricMeterI.class, ChargerBatteryI.class})
@OfferedInterfaces(offered = {ChargerI.class, ChargerElectricMeterI.class, ChargerBatteryI.class})
public class Charger extends AbstractComponent{
	
	protected final String				uri ;
	protected final String				chargerInboundPortURI ;	
	protected final String				chargerOutboundPortURI ;
	protected final String				chargerElectricMeterOutboundPortURI ;
	protected final String				chargerElectricMeterInboundPortURI ;
	protected ChargerOutboundPort		chargerOutboundPort ;
	protected ChargerInboundPort		chargerInboundPort ;
	protected ChargerOutboundPort		chargerElectricMeterOutboundPort ;
	protected ChargerInboundPort		chargerElectricMeterInboundPort ;
	protected boolean 					isOn=false;
	protected final double 				conso = 10;
	
	
//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected Charger(String uri,
					   String chargerOutboundPortURI,
					   String chargerInboundPortURI,
					   String chargerElectricMeterOutboundPortURI,
					   String chargerElectricMeterInboundPortURI) throws Exception{
		super(uri, 1, 1);

		assert uri != null;
		assert chargerOutboundPortURI != null;
		assert chargerInboundPortURI != null;

		this.uri = uri;
		this.chargerInboundPortURI = chargerInboundPortURI;
		this.chargerOutboundPortURI = chargerOutboundPortURI;
		this.chargerElectricMeterOutboundPortURI = chargerElectricMeterOutboundPortURI;
		this.chargerElectricMeterInboundPortURI = chargerElectricMeterInboundPortURI;

		//-------------------PUBLISH-------------------
		chargerInboundPort = new ChargerInboundPort(chargerInboundPortURI, this);
		chargerInboundPort.publishPort();
		this.chargerOutboundPort =	new ChargerOutboundPort(chargerOutboundPortURI, this);
		this.chargerOutboundPort.localPublishPort();
		chargerElectricMeterInboundPort = new ChargerInboundPort(chargerElectricMeterInboundPortURI, this);
		chargerElectricMeterInboundPort.publishPort();
		this.chargerElectricMeterOutboundPort =	new ChargerOutboundPort(chargerElectricMeterOutboundPortURI, this);
		this.chargerElectricMeterOutboundPort.localPublishPort();

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
	public void startCharger() throws Exception{
		this.logMessage("The charger is starting his job....") ;
		isOn = true;
	}

	public void stopCharger() throws Exception{
		this.logMessage("The charger is stopping his job....") ;
		isOn =false;
	}
	
	public void sendConsumption() throws Exception {
		this.logMessage("Sending consumption....") ;
		this.chargerElectricMeterOutboundPort.sendConsumption(conso) ;
	}
	
	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Charger component.") ;
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
								if(isOn) {
									((Charger)this.getTaskOwner()).sendConsumption() ;
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
		chargerOutboundPort.doDisconnection();
		chargerElectricMeterOutboundPort.doDisconnection();
		super.finalise();
	}

//------------------------------------------------------------------------
//----------------------------SHUTDOWN------------------------------------
//------------------------------------------------------------------------
	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			chargerInboundPort.unpublishPort();
			chargerOutboundPort.unpublishPort();
			chargerElectricMeterInboundPort.unpublishPort();
			chargerElectricMeterOutboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
