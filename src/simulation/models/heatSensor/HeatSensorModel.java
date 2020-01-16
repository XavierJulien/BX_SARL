package simulation.models.heatSensor;

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
import simulation.events.heatSensor.HeatSensorUpdater;
import simulation.events.heatSensor.HeatSensorWindowOpen;
import simulation.events.heatSensor.HeatSensorWindowStillOpen;

@ModelExternalEvents(imported = {HeatSensorUpdater.class,
								HeatSensorWindowOpen.class,
								HeatSensorWindowStillOpen.class})

public class HeatSensorModel extends AtomicHIOAwithEquations {
	
	// -------------------------------------------------------------------------
	// Inner classes and types
	// -------------------------------------------------------------------------


	public static class		HeatSensorReport
	extends		AbstractSimulationReport
	{
		private static final long serialVersionUID = 1L;
		
		public			HeatSensorReport(String modelURI)
		{
			super(modelURI);
		}


		@Override
		public String	toString()
		{
			return "HeatSensorReport(" + this.getModelURI() + ")";
		}
	}

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long					serialVersionUID = 1L;
	private static final String					SERIES = "temperature";
	private static double 						xValue = 0;
	public static final String					URI = "HeatSensorModel";
	protected XYPlotter							temperaturePlotter;
	
	//CURRENT
	protected final Value<Double>				currentTemperature = new Value<Double>(this, 0.0, 0);
	
	protected EmbeddingComponentStateAccessI 	componentRef;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public						HeatSensorModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception
	{
		super(uri, simulatedTimeUnit, simulationEngine);

		// creation of a plotter to show the evolution of the wind speed over
		// time during the simulation.
		PlotterDescription pd =
				new PlotterDescription(
						"Temperature",
						"Time (sec)",
						"Temp (°C)",
						350,
						0,
						300,
						200);
		this.temperaturePlotter = new XYPlotter(pd);
		this.temperaturePlotter.createSeries(SERIES);
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
			(EmbeddingComponentStateAccessI) simParams.get("heatSensorRef");
	}


	@Override
	public void					initialiseState(Time initialTime)
	{
		// the heating starts in mode OFF
		// initialisation of the wind speed plotter 
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
	protected void				initialiseVariables(Time startTime)
	{
		// the wind sensor starts to emit the wind speed
		this.currentTemperature.v =(((Math.sin(xValue+8)+1.0/10*Math.cos((xValue+2)*5)+ Math.cos((xValue*7)/2.0))*3)+6);

		// first data in the plotter to start the plot.
		this.temperaturePlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getTemperature());

		
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
//			this.logMessage("WindSensorModel::userDefinedExternalTransition 1");
		}

		// get the vector of current external events
		Vector<EventI> currentEvents = this.getStoredEventAndReset();
		// when this method is called, there is at least one external event,
		// and for the wind sensor model, there will be exactly one by
		// construction.
		assert	currentEvents != null && currentEvents.size() == 1;

		Event ce = (Event) currentEvents.get(0);
		assert	ce instanceof AbstractEvent;

		// the plot is piecewise constant; this data will close the currently
		// open piece
		this.temperaturePlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getTemperature());
		
		


		// execute the current external event on this model, changing its state
		// and Wind speed
		ce.executeOn(this);



		// add a new data on the plotter; this data will open a new piece
		this.temperaturePlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getTemperature());
		

		super.userDefinedExternalTransition(elapsedTime);

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
		
		

		super.endSimulation(endTime);
	}


	@Override
	public SimulationReportI	getFinalReport() throws Exception
	{
		return new HeatSensorReport(this.getURI());
	}

	// ------------------------------------------------------------------------
	// Model-specific methods
	// ------------------------------------------------------------------------
	
	
	public double				getTemperature()
	{
		return this.currentTemperature.v;
	}
	
	
	// ------------------------------------------------------------------------
	// Utils
	// ------------------------------------------------------------------------

	public void					updateTemperature() {
		try {
			this.currentTemperature.v = ((Double)componentRef.getEmbeddingComponentStateValue("temperature"));
			this.currentTemperature.v *= 0.90;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public void 				openWindow() {
		this.currentTemperature.v -= 5;
	}
	
	public void keepTemperature() {
		this.currentTemperature.v -=0.5;
	}
}
//------------------------------------------------------------------------------
