package bcm.components;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import bcm.interfaces.windturbine.WindTurbineI;
import bcm.ports.windturbine.WindTurbineInboundPort;
import bcm.ports.windturbine.WindTurbineOutboundPort;
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
import simulation.models.windturbine.WindTurbineCoupledModel;
import simulation.models.windturbine.WindTurbineModel;
import simulation.models.windturbine.WindTurbineModel.State;
import simulation.simulatorplugins.WindTurbineSimulatorPlugin;

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
	protected final String				windTurbineRef;
	

	
//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected WindTurbine(String uri,
			String windTurbineOutboundPortURI,
			String windTurbineSensorInboundPortURI,
					   String windTurbineInboundPortURI,
					   String windTurbineRef) throws Exception{
		super(uri, 2, 2);

		assert uri != null;
		assert windTurbineOutboundPortURI != null;
		assert windTurbineInboundPortURI != null;
		assert windTurbineSensorInboundPortURI != null;
		assert windTurbineRef != null;
		
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
		this.windTurbineRef = windTurbineRef;
		
		//----------------Modelisation-------------

		this.initialise();
	}


//------------------------------------------------------------------------
//----------------------------SERVICES------------------------------------
//------------------------------------------------------------------------

	
	/**
	 * this method is used to start the windTurbine
	 * @throws Exception
	 */
	public void startWindTurbine() throws Exception{
		this.logMessage("The wind Turbine is starting his job....") ;
		isOn = true;
	}

	/**
	 * this method is used to stop the windTurbine
	 * @throws Exception
	 */
	public void stopWindTurbine() throws Exception{
		this.logMessage("The wind Turbine is stopping his job....") ;
		isOn =false;
	}
	
	/**
	 * This method is used to send the wind turbine production to the controller
	 * @throws Exception
	 */
	public void sendProduction() throws Exception {
		this.logMessage("Sending energy....") ;
		prod = (Double)this.asp.getModelStateValue(WindTurbineModel.URI, "production");
		this.windTurbineOutboundPort.sendProduction(prod) ;
	}
	
	/**
	 * this method is called by an inbound port to update the wind speed with the value sent from the wind sensor
	 * @param speed the current windspeed
	 * @throws Exception
	 */
	public void getWindSpeed(double speed) throws Exception{
		this.windSpeed = speed;
		this.logMessage("The wind speed is "+ windSpeed) ;
	}

	
	/**
	 * start the component
	 */
	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Wind Turbine component.") ;
	}
	
	
//------------------------------------------------------------------------
//----------------------INITIALISE & EXECUTE------------------------------
//------------------------------------------------------------------------
	
	/**
	 * initialize the simulation
	 * @throws Exception
	 */
	protected void initialise() throws Exception {
		Architecture localArchitecture = this.createLocalArchitecture(null) ;
		this.asp = new WindTurbineSimulatorPlugin() ;
		this.asp.setPluginURI(localArchitecture.getRootModelURI()) ;
		this.asp.setSimulationArchitecture(localArchitecture) ;
		this.installPlugin(this.asp) ;
		this.toggleLogging() ;
	}
	
	
	/**
	 * execute the component, first the simulation starts, then the component behaviour
	 */
	@Override
	public void execute() throws Exception {
		super.execute();
		
		//---------------SIMULATION---------------
		SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 1000L ;
		HashMap<String,Object> simParams = new HashMap<String,Object>() ;
		simParams.put(windTurbineRef, this) ;
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
