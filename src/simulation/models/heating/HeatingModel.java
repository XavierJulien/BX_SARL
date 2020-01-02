package simulation.models.heating;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.cyphy.interfaces.EmbeddingComponentStateAccessI;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOAwithEquations;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.interfaces.SimulationReportI;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.devs_simulation.utils.AbstractSimulationReport;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;
import fr.sorbonne_u.utils.PlotterDescription;
import fr.sorbonne_u.utils.XYPlotter;
import simulation.events.AbstractEvent;
import simulation.events.heating.HeatingMode;
import simulation.events.heating.HeatingUpdater;
import simulation.events.heating.RestMode;
import simulation.events.heating.SwitchOff;
import simulation.events.heating.SwitchOn;

@ModelExternalEvents(imported = {SwitchOn.class,
								 SwitchOff.class,
								 RestMode.class,
								 HeatingMode.class,
								 HeatingUpdater.class})

public class HeatingModel extends AtomicHIOAwithEquations {
	
	// -------------------------------------------------------------------------
	// Inner classes and types
	// -------------------------------------------------------------------------

	public static enum 		State {
		OFF,
		ON
	}
	
	public static enum 		Mode {
		OFF,
		REST,
		HEATING
	}

	public static class		HeatingReport
	extends		AbstractSimulationReport
	{
		private static final long serialVersionUID = 1L;
		
		public			HeatingReport(String modelURI)
		{
			super(modelURI);
		}


		@Override
		public String	toString()
		{
			return "HeatingReport(" + this.getModelURI() + ")";
		}
	}

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long					serialVersionUID = 1L;
	protected static final double				CONSUMPTION = 1200.0; // Watts
	private static final String					SERIES = "temperature";
	private static final String					SERIESONOFF = "OnOff";
	public static final String					URI = "HeatingModel";
	protected XYPlotter							temperaturePlotter;
	protected XYPlotter							onOffPlotter; 
	
	//CURRENT
	protected State								currentState;
	protected Mode 								currentMode;
	protected final Value<Double>				currentTemperature = new Value<Double>(this, 0.0, 0);
	
	protected EmbeddingComponentStateAccessI 	componentRef;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public						HeatingModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception
	{
		super(uri, simulatedTimeUnit, simulationEngine);

		// creation of a plotter to show the evolution of the temperature over
		// time during the simulation.
		PlotterDescription pd =
				new PlotterDescription(
						"Heating temperature",
						"Time (sec)",
						"Temperature (C)",
						100,
						0,
						600,
						400);
		this.temperaturePlotter = new XYPlotter(pd);
		this.temperaturePlotter.createSeries(SERIES);

		
		PlotterDescription pdOnOff =
				new PlotterDescription(
						"Kettle State",
						"Time (sec)",
						"State (ON = 1/OFF = 0)",
						700,
						0,
						600,
						200);
		
		this.onOffPlotter = new XYPlotter(pdOnOff);
		this.onOffPlotter.createSeries(SERIESONOFF);
		// create a standard logger (logging on the terminal)
		this.setLogger(new StandardLogger());
	}

	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------


	@Override
	public void					setSimulationRunParameters(
		Map<String, Object> simParams
		) throws Exception
	{
		// The reference to the embedding component
		this.componentRef =
			(EmbeddingComponentStateAccessI) simParams.get("componentRef");
	}


	@Override
	public void					initialiseState(Time initialTime)
	{
		// the heating starts in mode OFF
		this.currentState = State.OFF;
		this.currentMode = Mode.OFF;

		// initialisation of the temperature plotter 
		this.temperaturePlotter.initialise();
		// show the plotter on the screen
		this.temperaturePlotter.showPlotter();

		
		//initialise the plotter for the state
		this.onOffPlotter.initialise();
		this.onOffPlotter.showPlotter();
		
		
		try {
			// set the debug level triggering the production of log messages.
			this.setDebugLevel(1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		super.initialiseState(initialTime);
	}


	@Override
	protected void				initialiseVariables(Time startTime)
	{
		// as the heating starts in mode OFF, its power consumption is 0
		this.currentTemperature.v = 0.0;

		// first data in the plotter to start the plot.
		this.temperaturePlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getTemperature());

		this.onOffPlotter.addData(
				SERIESONOFF,
				this.getCurrentStateTime().getSimulatedTime(),
				0);
		
		super.initialiseVariables(startTime);
	}


	@Override
	public Vector<EventI>		output()
	{
		// the model does not export any event.
		return null;
	}


	@Override
	public Duration				timeAdvance()
	{
		if (this.componentRef == null) {
			// the model has no internal event, however, its state will evolve
			// upon reception of external events.
			return Duration.INFINITY;
		} else {
			// This is to test the embedding component access facility.
			return new Duration(10.0, TimeUnit.SECONDS);
		}
	}


	@Override
	public void					userDefinedInternalTransition(Duration elapsedTime)
	{
		if (this.componentRef != null) {
			// This is an example showing how to access the component state
			// from a simulation model; this must be done with care and here
			// we are not synchronising with other potential component threads
			// that may access the state of the component object at the same
			// time.
			try {
				this.logMessage("component state = " +
						componentRef.getEmbeddingComponentStateValue("state"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}


	@Override
	public void					userDefinedExternalTransition(Duration elapsedTime)
	{
		if (this.hasDebugLevel(2)) {
//			this.logMessage("HeatingModel::userDefinedExternalTransition 1");
		}

		// get the vector of current external events
		Vector<EventI> currentEvents = this.getStoredEventAndReset();
		// when this method is called, there is at least one external event,
		// and for the heating model, there will be exactly one by
		// construction.
		assert	currentEvents != null && currentEvents.size() == 1;

		Event ce = (Event) currentEvents.get(0);
		assert	ce instanceof AbstractEvent;
		if (this.hasDebugLevel(2)) {
//			this.logMessage("HeatingModel::userDefinedExternalTransition 2 "
//										+ ce.getClass().getCanonicalName());
		}

		// the plot is piecewise constant; this data will close the currently
		// open piece
		this.temperaturePlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getTemperature());
		
		this.onOffPlotter.addData(
				SERIESONOFF,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getStateDouble());

		if (this.hasDebugLevel(2)) {
			//this.logMessage("HeatingModel::userDefinedExternalTransition 3 "+ this.getState());
		}

		// execute the current external event on this model, changing its state
		// and temperature level
		ce.executeOn(this);

		if (this.hasDebugLevel(1)) {
			//this.logMessage("HeatingModel::userDefinedExternalTransition 4 " + this.getState());
		}

		// add a new data on the plotter; this data will open a new piece
		this.temperaturePlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getTemperature());
		
		this.onOffPlotter.addData(
				SERIESONOFF,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getStateDouble());

		super.userDefinedExternalTransition(elapsedTime);
		if (this.hasDebugLevel(2)) {
			//this.logMessage("HeatingModel::userDefinedExternalTransition 5");
		}
	}


	@Override
	public void					endSimulation(Time endTime) throws Exception
	{
		this.temperaturePlotter.addData(
				SERIES,
				endTime.getSimulatedTime(),
				this.getTemperature());
		Thread.sleep(10000L);
		this.temperaturePlotter.dispose();
		
		this.onOffPlotter.addData(
				SERIESONOFF,
				endTime.getSimulatedTime(),
				this.getStateDouble());
		Thread.sleep(10000L);
		this.onOffPlotter.dispose();

		super.endSimulation(endTime);
	}


	@Override
	public SimulationReportI	getFinalReport() throws Exception
	{
		return new HeatingReport(this.getURI());
	}

	// ------------------------------------------------------------------------
	// Model-specific methods
	// ------------------------------------------------------------------------
	
	public State				getState()
	{
		return this.currentState;
	}
	public Mode					getMode() {
		return this.currentMode;
	}
	public double				getTemperature()
	{
		return this.currentTemperature.v;
	}
	
	public double getStateDouble() {
		if(this.getState() == State.ON) {
			return 1;
		}else {
			return 0;
		}
	}
	
	// ------------------------------------------------------------------------
	// Utils
	// ------------------------------------------------------------------------

	public void					updateTemperature() {
		if(currentState == State.ON) {
			if(currentMode == Mode.REST) {
				currentTemperature.v -= 1;
				//System.out.println("ON/FULL Temperature : "+currentTemperature.v);
			}
			if(currentMode == Mode.HEATING) {
				currentTemperature.v += 4;
				//System.out.println("ON/HALF Temperature : "+currentTemperature.v);
			}
		}
		if(currentState == State.OFF) {
			if(currentTemperature.v > 0.2) {
				currentTemperature.v -= 0.2;
			}else {
				currentTemperature.v = 0.;
			}
					//System.out.println("OFF/FULL or HALF  Temperature : "+currentTemperature);
		}
	}
	
	public void					updateState(State s){
		this.currentState = s;
	}
	
	public void 				updateMode() {
		System.out.println(currentMode);
		if(currentState == State.ON) {
			if(this.currentMode == Mode.REST){
				if(currentTemperature.v < 15){
					updateMode(Mode.HEATING);
				}
			}
			if(this.currentMode == Mode.HEATING){
				if(currentTemperature.v > 20){
					updateMode(Mode.REST);
				}
			}
		}else {
			updateMode(Mode.OFF);
		}
	}
	
	public void					updateMode(Mode c)
	{
		this.currentMode = c;
	}
}
//------------------------------------------------------------------------------
