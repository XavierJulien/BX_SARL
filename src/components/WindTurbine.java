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
import interfaces.windturbine.WindTurbineI;
import ports.windturbine.WindTurbineInboundPort;
import ports.windturbine.WindTurbineOutboundPort;
import simulation.components.windturbine.WindTurbineSimulatorPlugin;
import simulation.models.windturbine.WindTurbineCoupledModel;

@RequiredInterfaces(required = {WindTurbineI.class})
@OfferedInterfaces(offered = {WindTurbineI.class})
public class WindTurbine extends AbstractCyPhyComponent implements EmbeddingComponentStateAccessI{

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
	
	protected WindTurbineSimulatorPlugin		asp ;

	
//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected WindTurbine(String uri,
			String windTurbineOutboundPortURI,
			String windTurbineSensorInboundPortURI,
					   String windTurbineInboundPortURI) throws Exception{
		super(uri, 2, 2);

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

		this.initialise();
		
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
		prod = 2*windSpeed;
		this.windTurbineOutboundPort.sendProduction(prod) ;
	}
	
	public void getWindSpeed(double speed) throws Exception{
		this.windSpeed = speed;
		this.logMessage("The wind speed is "+ windSpeed) ;
	}

	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Wind Turbine component.") ;
	}
	
	
	
	
	protected void initialise() throws Exception {
		// The coupled model has been made able to create the simulation
		// architecture description.
		Architecture localArchitecture = this.createLocalArchitecture(null) ;
		// Create the appropriate DEVS simulation plug-in.
		this.asp = new WindTurbineSimulatorPlugin() ;
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
		
		SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 500L ;
		HashMap<String,Object> simParams = new HashMap<String,Object>() ;
		simParams.put("windTurbineRef", this) ;
		this.asp.setSimulationRunParameters(simParams) ;
		// Start the simulation.
		this.runTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							Thread.sleep(1000);
							asp.doStandAloneSimulation(0.0, 50000.0) ;
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				}) ;
		Thread.sleep(10L) ;
		
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							
							while(true) {
								if(windSpeed > 7.5 && isOn) {
									stopWindTurbine();
								}else {
									if(!isOn && windSpeed <=7.5) {
										startWindTurbine();
									}
								}
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


	@Override
	protected Architecture createLocalArchitecture(String architectureURI) throws Exception{
		return WindTurbineCoupledModel.build() ;
	}

	
	
	@Override
	public Object getEmbeddingComponentStateValue(String name) throws Exception {
		if(name.equals("windSpeed")) {
			return new Double(windSpeed);
		}else {
			return null;
		}
		
	}
	
}
