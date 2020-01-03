package simulation.models.windSensor;

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
import simulation.events.windSensor.WindSensorUpdater;
import simulation.models.heating.HeatingModel.HeatingReport;

@ModelExternalEvents(imported = {WindSensorUpdater.class})

public class WindSensorModel extends AtomicHIOAwithEquations {
	
	// -------------------------------------------------------------------------
	// Inner classes and types
	// -------------------------------------------------------------------------


	public static class		WindSensorReport
	extends		AbstractSimulationReport
	{
		private static final long serialVersionUID = 1L;
		
		public			WindSensorReport(String modelURI)
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
	private static final String					SERIES = "wind";
	public static final String					URI = "WindSensorModel";
	protected XYPlotter							windPlotter;
	
	//CURRENT
	protected final Value<Double>				currentWind = new Value<Double>(this, 0.0, 0);
	
	protected EmbeddingComponentStateAccessI 	componentRef;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public						WindSensorModel(
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
						"Wind Speed",
						"Time (sec)",
						"Wind (m/s)",
						100,
						0,
						600,
						400);
		this.windPlotter = new XYPlotter(pd);
		this.windPlotter.createSeries(SERIES);
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

		// initialisation of the temperature plotter 
		this.windPlotter.initialise();
		// show the plotter on the screen
		this.windPlotter.showPlotter();
		
		
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
		this.currentWind.v = 0.0;

		// first data in the plotter to start the plot.
		this.windPlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getWind());

		
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
		this.windPlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getWind());
		
		

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
		this.windPlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getWind());
		

		super.userDefinedExternalTransition(elapsedTime);
		if (this.hasDebugLevel(2)) {
			//this.logMessage("HeatingModel::userDefinedExternalTransition 5");
		}
	}


	@Override
	public void					endSimulation(Time endTime) throws Exception
	{
		this.windPlotter.addData(
				SERIES,
				endTime.getSimulatedTime(),
				this.getWind());
		Thread.sleep(10000L);
		this.windPlotter.dispose();
		
		

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
	
	
	public double				getWind()
	{
		return this.currentWind.v;
	}
	
	
	// ------------------------------------------------------------------------
	// Utils
	// ------------------------------------------------------------------------

	public void					updateWind() {
		//TODO will be changed 
		currentWind.v += 3;
		
	}
}
//------------------------------------------------------------------------------
