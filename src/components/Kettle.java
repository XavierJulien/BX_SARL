package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.KettleI;
import ports.KettleInboundPort;
import ports.KettleOutboundPort;

@RequiredInterfaces(required = {KettleI.class})
@OfferedInterfaces(offered = {KettleI.class})
public class Kettle extends AbstractComponent{
	
	protected final String				uri ;
	protected final String				kettleInboundPortURI ;	
	protected final String				kettleOutboundPortURI ;
	protected final String				kettleElectricMeterOutboundPortURI ;
	protected final String				kettleElectricMeterInboundPortURI ;
	protected KettleOutboundPort		kettleOutboundPort ;
	protected KettleInboundPort			kettleInboundPort ;
	protected KettleOutboundPort		kettleElectricMeterOutboundPort ;
	protected KettleInboundPort			kettleElectricMeterInboundPort ;
	protected boolean 					isOn=false;
	protected final double 				conso = 10;

	
	//------------------------------------------------------------------------
	//----------------------------CONSTRUCTOR---------------------------------
	//------------------------------------------------------------------------
	protected Kettle(String uri,
						 String kettleOutboundPortURI,
						 String kettleInboundPortURI,
						 String kettleElectricMeterOutboundPortURI,
						 String kettleElectricMeterInboundPortURI) throws Exception{
		super(uri, 1, 1);

		assert uri != null;
		assert kettleOutboundPortURI != null;
		assert kettleInboundPortURI != null;

		this.uri = uri;
		this.kettleInboundPortURI = kettleInboundPortURI;
		this.kettleOutboundPortURI = kettleOutboundPortURI;
		this.kettleElectricMeterInboundPortURI = kettleElectricMeterInboundPortURI;
		this.kettleElectricMeterOutboundPortURI = kettleElectricMeterOutboundPortURI;

		//-------------------PUBLISH-------------------
		kettleInboundPort = new KettleInboundPort(kettleInboundPortURI, this) ;
		kettleInboundPort.publishPort() ;
		this.kettleOutboundPort = new KettleOutboundPort(kettleOutboundPortURI, this) ;
		this.kettleOutboundPort.localPublishPort() ;
		
		kettleElectricMeterInboundPort = new KettleInboundPort(kettleElectricMeterInboundPortURI, this) ;
		kettleElectricMeterInboundPort.publishPort() ;
		this.kettleElectricMeterOutboundPort = new KettleOutboundPort(kettleElectricMeterOutboundPortURI, this) ;
		this.kettleElectricMeterOutboundPort.localPublishPort() ;

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		//-------------------GUI-------------------
		this.tracer.setTitle(uri) ;
		this.tracer.setRelativePosition(2, 2) ;
	}


//------------------------------------------------------------------------
//----------------------------SERVICES------------------------------------
//------------------------------------------------------------------------
	
	public void startKettle() throws Exception{
		this.logMessage("The kettle is starting his job....") ;
		isOn = true;
	}

	public void stopKettle() throws Exception{
		this.logMessage("The kettle is stopping his job....") ;
		isOn =false;
	}
	
	public double sendConsumption() throws Exception {
		this.logMessage("Sending consumption....") ;
		return conso;
	}

	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Kettle component.") ;
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
								((Kettle)this.getTaskOwner()).sendConsumption() ;
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
		kettleOutboundPort.doDisconnection();
		kettleElectricMeterOutboundPort.doDisconnection();
		super.finalise();
	}

//------------------------------------------------------------------------
//----------------------------SHUTDOWN------------------------------------
//------------------------------------------------------------------------
	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			kettleInboundPort.unpublishPort();
			kettleOutboundPort.unpublishPort();
			kettleElectricMeterInboundPort.unpublishPort();
			kettleElectricMeterOutboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
