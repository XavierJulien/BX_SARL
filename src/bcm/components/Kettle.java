package bcm.components;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import bcm.interfaces.kettle.KettleI;
import bcm.ports.kettle.KettleInboundPort;
import bcm.ports.kettle.KettleOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.cyphy.interfaces.EmbeddingComponentStateAccessI;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.simulators.SimulationEngine;
import simulation.models.kettle.KettleCoupledModel;
import simulation.models.kettle.KettleModel;
import simulation.simulatorplugins.KettleSimulatorPlugin;

@RequiredInterfaces(required = {KettleI.class})
@OfferedInterfaces(offered = {KettleI.class})
public class Kettle 
extends AbstractCyPhyComponent 
implements EmbeddingComponentStateAccessI{
	
	protected final String				uri ;
	protected final String				kettleInboundPortURI ;	
	protected final String				kettleOutboundPortURI ;
	protected final String				kettleElectricMeterOutboundPortURI ;
	protected final String				kettleElectricMeterInboundPortURI ;
	protected KettleOutboundPort		kettleOutboundPort ;
	protected KettleInboundPort			kettleInboundPort ;
	protected KettleOutboundPort		kettleElectricMeterOutboundPort ;
	protected KettleInboundPort			kettleElectricMeterInboundPort ;
	
	
	protected KettleSimulatorPlugin		asp ;
	
	protected boolean 					isOn=false;
	protected double 					consumption = 10;
	protected double 					currentConsumption;
	
	

	
	//------------------------------------------------------------------------
	//----------------------------CONSTRUCTOR---------------------------------
	//------------------------------------------------------------------------
	protected Kettle(String uri,
						 String kettleOutboundPortURI,
						 String kettleInboundPortURI,
						 String kettleElectricMeterOutboundPortURI,
						 String kettleElectricMeterInboundPortURI,
						 double consumption) throws Exception{
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
		
		//----------------Variables----------------
		isOn=false;
		this.consumption = consumption;
		currentConsumption = 0;
		
		//----------------Modelisation-------------
		
		this.initialise();
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
	
	public void sendConsumption() throws Exception {
			this.logMessage("Sending consumption.... "+currentConsumption ) ;
			this.kettleElectricMeterOutboundPort.sendConsumption(currentConsumption) ;
	}

	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Kettle component.") ;
	}
	
	
	
//------------------------------------------------------------------------
//-------------------------TREATMENT METHODS------------------------------
//------------------------------------------------------------------------
	
	protected void updateConsumption(KettleModel.Content content) {
		if(!isOn) {
			currentConsumption = 0;
		}else {
			if(content == KettleModel.Content.HALF) {
				currentConsumption = consumption*0.5;
			}else {
				if(content == KettleModel.Content.FULL) {
					currentConsumption = consumption;
				}
			}
		}
		
	}
	
	protected void updateState(KettleModel.State state) {
		if(state == KettleModel.State.ON) {
			isOn = true;
		}else {
			isOn = false;
		}
	}
	
	
	
	
//------------------------------------------------------------------------
//------------------------------EXECUTION---------------------------------
//------------------------------------------------------------------------
	
	protected void initialise() throws Exception {
		Architecture localArchitecture = this.createLocalArchitecture(null) ;
		this.asp = new KettleSimulatorPlugin() ;
		this.asp.setPluginURI(localArchitecture.getRootModelURI()) ;
		this.asp.setSimulationArchitecture(localArchitecture) ;
		this.installPlugin(this.asp) ;
		this.toggleLogging() ;
	}
	
	
	
	

	@Override
	public void execute() throws Exception {
		super.execute();
	
		//---------------SIMULATION---------------
		SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 500L ;
		HashMap<String,Object> simParams = new HashMap<String,Object>() ;
		simParams.put("kettleRef", this) ;
		this.asp.setSimulationRunParameters(simParams) ;
		this.runTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							Thread.sleep(1000);
							asp.doStandAloneSimulation(0.0, 5000.0) ;
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				}) ;
		Thread.sleep(10L) ;
		
		
		//---------------BCM---------------
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							while(true) {
								((Kettle)this.getTaskOwner()).updateState((KettleModel.State)asp.getModelStateValue(KettleModel.URI, "state"));
								((Kettle)this.getTaskOwner()).updateConsumption((KettleModel.Content)asp.getModelStateValue(KettleModel.URI, "content"));
								((Kettle)this.getTaskOwner()).sendConsumption() ;
								
								Thread.sleep(1000);
							}
						} catch (Exception e) {throw new RuntimeException(e) ;}
					}
				},
				1000, TimeUnit.MILLISECONDS);
	}
//------------------------------------------------------------------------
//-------------------------SIMULATION METHODS-----------------------------
//------------------------------------------------------------------------

	@Override
	protected Architecture createLocalArchitecture(String architectureURI) throws Exception{
		return KettleCoupledModel.build() ;
	}


	@Override
	public Object getEmbeddingComponentStateValue(String name) throws Exception {
		return this.asp.getModelStateValue(KettleModel.URI, "state") + " " + 
				   this.asp.getModelStateValue(KettleModel.URI, "content") + " " + 
				   this.asp.getModelStateValue(KettleModel.URI, "temperature");
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