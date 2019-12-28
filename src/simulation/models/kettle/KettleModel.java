package simulation.models.kettle;

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
import simulation.events.kettle.EmptyKettle;
import simulation.events.kettle.FillKettle;
import simulation.events.kettle.KettleUpdater;
import simulation.events.kettle.SwitchOff;
import simulation.events.kettle.SwitchOn;

@ModelExternalEvents(imported = {SwitchOn.class,
								 SwitchOff.class,
								 FillKettle.class,
								 EmptyKettle.class,
								 KettleUpdater.class})

public class KettleModel extends AtomicHIOAwithEquations {
	
	// -------------------------------------------------------------------------
	// Inner classes and types
	// -------------------------------------------------------------------------

	public static enum State {
		OFF,
		ON
	}
	
	public static enum Content {
		FULL,
		HALF,
		EMPTY
	}

	public static class		KettleReport
	extends		AbstractSimulationReport
	{
		private static final long serialVersionUID = 1L;
		
		public			KettleReport(String modelURI)
		{
			super(modelURI);
		}


		@Override
		public String	toString()
		{
			return "KettleReport(" + this.getModelURI() + ")";
		}
	}

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long		serialVersionUID = 1L;
	protected static final double	CONSUMPTION = 1200.0; // Watts
	private static final String		SERIES = "temperature";
	public static final String		URI = "KettleModel";
	protected XYPlotter				temperaturePlotter;
	
	//CURRENT
	protected State					currentState;
	protected Content 				currentContent;
	protected final Value<Double>	currentTemperature = new Value<Double>(this, 0.0, 0);
	
	protected EmbeddingComponentStateAccessI componentRef;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public				KettleModel(
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
						"Kettle temperature",
						"Time (sec)",
						"Temperature (C)",
						100,
						0,
						600,
						400);
		this.temperaturePlotter = new XYPlotter(pd);
		this.temperaturePlotter.createSeries(SERIES);

		// create a standard logger (logging on the terminal)
		this.setLogger(new StandardLogger());
	}

	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------


	@Override
	public void			setSimulationRunParameters(
		Map<String, Object> simParams
		) throws Exception
	{
		// The reference to the embedding component
		this.componentRef =
			(EmbeddingComponentStateAccessI) simParams.get("componentRef");
	}


	@Override
	public void			initialiseState(Time initialTime)
	{
		// the kettle starts in mode OFF
		this.currentState = State.OFF;
		this.currentContent = Content.EMPTY;

		// initialisation of the temperature plotter 
		this.temperaturePlotter.initialise();
		// show the plotter on the screen
		this.temperaturePlotter.showPlotter();

		try {
			// set the debug level triggering the production of log messages.
			this.setDebugLevel(1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		super.initialiseState(initialTime);
	}


	@Override
	protected void		initialiseVariables(Time startTime)
	{
		// as the kettle starts in mode OFF, its power consumption is 0
		this.currentTemperature.v = 0.0;

		// first data in the plotter to start the plot.
		this.temperaturePlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getTemperature());

		super.initialiseVariables(startTime);
	}


	@Override
	public Vector<EventI>	output()
	{
		// the model does not export any event.
		return null;
	}


	@Override
	public Duration		timeAdvance()
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
	public void			userDefinedInternalTransition(Duration elapsedTime)
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
	public void			userDefinedExternalTransition(Duration elapsedTime)
	{
		if (this.hasDebugLevel(2)) {
//			this.logMessage("KettleModel::userDefinedExternalTransition 1");
		}

		// get the vector of current external events
		Vector<EventI> currentEvents = this.getStoredEventAndReset();
		// when this method is called, there is at least one external event,
		// and for the kettle model, there will be exactly one by
		// construction.
		assert	currentEvents != null && currentEvents.size() == 1;

		Event ce = (Event) currentEvents.get(0);
		assert	ce instanceof AbstractEvent;
		if (this.hasDebugLevel(2)) {
//			this.logMessage("KettleModel::userDefinedExternalTransition 2 "
//										+ ce.getClass().getCanonicalName());
		}

		// the plot is piecewise constant; this data will close the currently
		// open piece
		this.temperaturePlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getTemperature());

		if (this.hasDebugLevel(2)) {
			//this.logMessage("KettleModel::userDefinedExternalTransition 3 "+ this.getState());
		}

		// execute the current external event on this model, changing its state
		// and temperature level
		ce.executeOn(this);

		if (this.hasDebugLevel(1)) {
			//this.logMessage("KettleModel::userDefinedExternalTransition 4 " + this.getState());
		}

		// add a new data on the plotter; this data will open a new piece
		this.temperaturePlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getTemperature());

		super.userDefinedExternalTransition(elapsedTime);
		if (this.hasDebugLevel(2)) {
			//this.logMessage("KettleModel::userDefinedExternalTransition 5");
		}
	}


	@Override
	public void			endSimulation(Time endTime) throws Exception
	{
		this.temperaturePlotter.addData(
				SERIES,
				endTime.getSimulatedTime(),
				this.getTemperature());
		Thread.sleep(10000L);
		this.temperaturePlotter.dispose();

		super.endSimulation(endTime);
	}


	@Override
	public SimulationReportI	getFinalReport() throws Exception
	{
		return new KettleReport(this.getURI());
	}

	// ------------------------------------------------------------------------
	// Model-specific methods
	// ------------------------------------------------------------------------

	public State		getState()
	{
		return this.currentState;
	}
	public Content		getContent() {
		return this.currentContent;
	}
	public double		getTemperature()
	{
		return this.currentTemperature.v;
	}
	
	// ------------------------------------------------------------------------
	// Utils
	// ------------------------------------------------------------------------

	public void			updateTemperature() {
		if(currentState == State.ON) {
			if(currentContent == Content.FULL) {
				currentTemperature.v += 3.0;
				//System.out.println("ON/FULL Temperature : "+currentTemperature.v);
			}
			if(currentContent == Content.HALF) {
				currentTemperature.v += 6.0;
				//System.out.println("ON/HALF Temperature : "+currentTemperature.v);
			}
		}
		if(currentState == State.OFF) {
			if(currentContent == Content.EMPTY) {
				currentTemperature.v = 0.0;
				//System.out.println("OFF/EMPTY Temperature : "+currentTemperature.v);
			}
			else{
				if(currentTemperature.v > 0 ) {
					currentTemperature.v -= 1.0;
					//System.out.println("OFF/FULL or HALF  Temperature : "+currentTemperature);
				}
			}
		}
	}
	
	public void			updateState() {
		if(currentTemperature.v >= 100.0 && currentState == State.ON) {
			//System.out.println("State : OFF");
			currentState = State.OFF;
		}
		if(currentTemperature.v == 0.0 && currentState == State.OFF && currentContent != Content.EMPTY) {
			//System.out.println("State : ON");
			currentState = State.ON;
		}
		
		
	}
	
	public void			updateState(State s)
	{
		this.currentState = s;
	}
	
	public void 		updateContent() {
		if(currentContent == Content.EMPTY) {
			Double stay_empty = Math.random();
			if(stay_empty > 0.95) {
				Double rand = Math.random();
				if(rand <= 0.3) {
					currentContent = Content.HALF;
				}else {
					if(rand <= 0.6){
						currentContent = Content.FULL;
					}
				}
			}
			
		}
		if(currentTemperature.v > 0.0 && currentState == State.OFF) {
			if(Math.random() < 0.25) {
				currentContent = Content.EMPTY;
			}
		}
	}
	
	public void			updateContent(Content c)
	{
		this.currentContent = c;
	}
}
//------------------------------------------------------------------------------
