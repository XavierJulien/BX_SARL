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
import simulation.models.windturbine.WindTurbineModel;
import simulation.models.windturbine.WindTurbineModel.State;

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
	
	protected WindTurbineSimulatorPlugin		asp ;

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
		super(uri, 2, 2);

		assert uri != null;
		assert windTurbineOutboundPortURI != null;
		assert windTurbineInboundPortURI != null;
		assert windTurbineSensorInboundPortURI != null;
		
		this.uri = uri;
		this.windTurbineInboundPortURI = windTurbineInboundPortURI;
		this.windTurbineOutboundPortURI = windTurbineOutboundPortURI;
		this.windTurbineSensorInboundPortURI = windTurbineSensorInboundPortURI;

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

		//----------------Variables----------------

		this.prod = 0;
		this.windSpeed =0;
		
		//----------------Modelisation-------------

		this.initialise();
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
		prod = (Double)this.asp.getModelStateValue(WindTurbineModel.URI, "production");
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
	
	
//------------------------------------------------------------------------
//----------------------INITIALISE & EXECUTE------------------------------
//------------------------------------------------------------------------
	
	protected void initialise() throws Exception {
		Architecture localArchitecture = this.createLocalArchitecture(null) ;
		this.asp = new WindTurbineSimulatorPlugin() ;
		this.asp.setPluginURI(localArchitecture.getRootModelURI()) ;
		this.asp.setSimulationArchitecture(localArchitecture) ;
		this.installPlugin(this.asp) ;
		this.toggleLogging() ;
	}
	
	
	@Override
	public void execute() throws Exception {
		super.execute();
		
		//---------------SIMULATION---------------
		SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 1000L ;
		HashMap<String,Object> simParams = new HashMap<String,Object>() ;
		simParams.put("windTurbineRef", this) ;
		this.asp.setSimulationRunParameters(simParams) ;
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
		Thread.sleep(2000L) ;
		
		//---------------BCM---------------
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							
							while(true) {
								State s = (State) ((WindTurbine)this.getTaskOwner()).asp.getModelStateValue(WindTurbineModel.URI, "state");
								if(s == State.ON) {
									((WindTurbine)this.getTaskOwner()).startWindTurbine();
								}else {
									((WindTurbine)this.getTaskOwner()).stopWindTurbine();
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
//-------------------------SIMULATION METHODS-----------------------------
//------------------------------------------------------------------------
	@Override
	protected Architecture createLocalArchitecture(String architectureURI) throws Exception{
		return WindTurbineCoupledModel.build() ;
	}

	@Override
	public Object getEmbeddingComponentStateValue(String name) throws Exception {
		if(name.equals("windSpeed")) {
			return new Double(windSpeed);
		}else {
			if(name.equals("state")) {
				return new Boolean(isOn);
			}else{
				return null;
			}
			
		}
		
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
