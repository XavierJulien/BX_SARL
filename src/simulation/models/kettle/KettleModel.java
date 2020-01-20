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
	private static final String		SERIESONOFF = "OnOff";
	public static final String		URI = "KettleModel";
	protected XYPlotter				temperaturePlotter;
	protected XYPlotter				onOffPlotter; 
	
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
						0,
						0,
						300,
						200);
		
		PlotterDescription pdOnOff =
				new PlotterDescription(
						"Kettle State",
						"Time (sec)",
						"State (ON = 1/OFF = 0)",
						300,
						0,
						300,
						200);
		
		this.temperaturePlotter = new XYPlotter(pd);
		this.temperaturePlotter.createSeries(SERIES);

		
		this.onOffPlotter = new XYPlotter(pdOnOff);
		this.onOffPlotter.createSeries(SERIESONOFF);

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
	protected void		initialiseVariables(Time startTime)
	{
		// as the kettle starts in mode OFF, its power consumption is 0
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
	public void			userDefinedExternalTransition(Duration elapsedTime)
	{
		Vector<EventI> currentEvents = this.getStoredEventAndReset();
		assert	currentEvents != null && currentEvents.size() == 1;

		Event ce = (Event) currentEvents.get(0);
		assert	ce instanceof AbstractEvent;

		this.temperaturePlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getTemperature());
		
		this.onOffPlotter.addData(
				SERIESONOFF,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getStateDouble());


		ce.executeOn(this);

		this.temperaturePlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getTemperature());
		
		this.onOffPlotter.addData(
				SERIESONOFF,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getStateDouble());

		super.userDefinedExternalTransition(elapsedTime);

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
		return new KettleReport(this.getURI());
	}

	// ------------------------------------------------------------------------
	// Model-specific methods
	// ------------------------------------------------------------------------

	public State		getState()
	{
		return this.currentState;
	}
	
	public double getStateDouble() {
		if(this.getState() == State.ON) {
			return 1;
		}else {
			return 0;
		}
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
			}
			if(currentContent == Content.HALF) {
				currentTemperature.v += 6.0;
			}
		}
		if(currentState == State.OFF) {
			if(currentContent == Content.EMPTY) {
				currentTemperature.v = 0.0;
			}
			else{
				if(currentTemperature.v > 0 ) {
					currentTemperature.v -= 1.0;
				}
			}
		}
	}
	
	public void	updateState() {
		if(currentTemperature.v >= 100.0 && currentState == State.ON) {
			currentState = State.OFF;
		}
		if(currentTemperature.v == 0.0 && currentState == State.OFF && currentContent != Content.EMPTY) {
			currentState = State.ON;
		}
		
		
	}
	
	public void			updateState(State s)
	{
		this.currentState = s;
	}
	

	
	public void			updateContent(Content c)
	{
		this.currentContent = c;
	}
}
//------------------------------------------------------------------------------
