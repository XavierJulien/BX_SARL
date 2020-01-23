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


/**
* The class <code>WindTurbineModel</code> implements a simplified DEVS
* simulation model of a windturbine providing the current intensity of
* electricity production as a continuous variable.
*
* <p><strong>Description</strong></p>
* 
* <p>
* The windturbine can of course be switch on and off in case of a too strong wind
* in order to avoid a break. 
* The production of the wind turbine is computed depending of the wind speed 
* </p>
* <p>
* The windturbine model is commanded through only one event
* <code>WTPupdater</code>, which is used to update the production of the wind turbine.
* </p>
* 
* <p><strong>Invariant</strong></p>
* 
* <pre>
* invariant		true	
* </pre>
* 
* <p>Created on : 2019-10-10</p>
* 
* 
*/
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
			return "WindSensorReport(" + this.getModelURI() + ")";
		}
	}

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long					serialVersionUID = 1L;
	private static final String					SERIES = "wind";
	private static double 						xValue = 0;
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

		// creation of a plotter to show the evolution of the wind speed over
		// time during the simulation.
		PlotterDescription pd =
				new PlotterDescription(
						"Wind Speed",
						"Time (sec)",
						"Wind (m/s)",
						0,
						250,
						300,
						200);
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
			(EmbeddingComponentStateAccessI) simParams.get("windSensorRef");
	}


	@Override
	public void					initialiseState(Time initialTime)
	{
		// the heating starts in mode OFF
		// initialisation of the wind speed plotter 
		this.windPlotter.initialise();
		// show the plotter on the screen
		this.windPlotter.showPlotter();

		super.initialiseState(initialTime);
	}


	@Override
	protected void				initialiseVariables(Time startTime)
	{
		// the wind sensor starts to emit the wind speed
		this.currentWind.v =(((Math.sin(xValue+8)+1.0/10*Math.cos((xValue+2)*5)+ Math.cos((xValue*7)/2.0))*3)+6);

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
	public void					userDefinedExternalTransition(Duration elapsedTime)
	{

		Vector<EventI> currentEvents = this.getStoredEventAndReset();
		assert	currentEvents != null && currentEvents.size() == 1;

		Event ce = (Event) currentEvents.get(0);
		assert	ce instanceof AbstractEvent;
		this.windPlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getWind());
		ce.executeOn(this);



		// add a new data on the plotter; this data will open a new piece
		this.windPlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getWind());
		

		super.userDefinedExternalTransition(elapsedTime);

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
		return new WindSensorReport(this.getURI());
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
		currentWind.v =(((Math.sin(xValue+8)+1.0/10*Math.cos((xValue+2)*5)+ Math.cos((xValue*7)/2.0))*3)+6);
		xValue += 0.1;
		
	}
}
//------------------------------------------------------------------------------
