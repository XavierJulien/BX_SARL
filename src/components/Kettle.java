package components;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
import interfaces.kettle.KettleI;
import ports.kettle.KettleInboundPort;
import ports.kettle.KettleOutboundPort;
import simulation.components.kettle.KettleSimulatorPlugin;
import simulation.models.kettle.KettleCoupledModel;
import simulation.models.kettle.KettleModel;

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
	protected final double 				consumption = 10;
	
	

	
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
		this.logMessage("Sending consumption....") ;
		this.kettleElectricMeterOutboundPort.sendConsumption(consumption) ;
	}

	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Kettle component.") ;
	}
	
	
	
	protected void initialise() throws Exception {
		// The coupled model has been made able to create the simulation
		// architecture description.
		Architecture localArchitecture = this.createLocalArchitecture(null) ;
		// Create the appropriate DEVS simulation plug-in.
		this.asp = new KettleSimulatorPlugin() ;
		// Set the URI of the plug-in, using the URI of its associated
		// simulation model.
		this.asp.setPluginURI(localArchitecture.getRootModelURI()) ;
		// Set the simulation architecture.
		this.asp.setSimulationArchitecture(localArchitecture) ;
		// Install the plug-in on the component, starting its own life-cycle.
		this.installPlugin(this.asp) ;

		// Toggle logging on to get a log on the screen.
		this.toggleLogging() ;
	}
	
	
	
	

	@Override
	public void execute() throws Exception {
		super.execute();
		SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 100L ;
		HashMap<String,Object> simParams = new HashMap<String,Object>() ;
		simParams.put("componentRef", this) ;
		this.asp.setSimulationRunParameters(simParams) ;
		// Start the simulation.
		this.runTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							asp.doStandAloneSimulation(0.0, 500.0) ;
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				}) ;
		Thread.sleep(10L) ;
		// During the simulation, the following lines provide an example how
		// to use the simulation model access facility by the component.
		/*for (int i = 0 ; i < 100 ; i++) {
			this.logMessage("Kettle " +
				this.asp.getModelStateValue(KettleModel.URI, "state") + " "+
				this.asp.getModelStateValue(KettleModel.URI, "content") + " " +
				this.asp.getModelStateValue(KettleModel.URI, "temperature")) ;
			Thread.sleep(5L) ;
		}*/
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							while(true) {
								this.taskOwner.logMessage("Kettle " +
										asp.getModelStateValue(KettleModel.URI, "state") + " "+
										asp.getModelStateValue(KettleModel.URI, "content") + " " +
										asp.getModelStateValue(KettleModel.URI, "temperature")) ;
								if(!isOn) {
									((Kettle)this.getTaskOwner()).sendConsumption() ;
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
}
