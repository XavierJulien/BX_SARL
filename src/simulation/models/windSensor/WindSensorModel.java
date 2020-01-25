package simulation.models.windSensor;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import bcm.launcher.CVM;
import fr.sorbonne_u.components.cyphy.interfaces.EmbeddingComponentStateAccessI;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOAwithEquations;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;
import fr.sorbonne_u.utils.PlotterDescription;
import fr.sorbonne_u.utils.XYPlotter;
import simulation.events.AbstractEvent;
import simulation.events.windSensor.WindSensorUpdater;

@ModelExternalEvents(imported = {WindSensorUpdater.class})

public class WindSensorModel extends AtomicHIOAwithEquations {

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long					serialVersionUID = 1L;
	private static final String					SERIES = "wind";
	private static double 						xValue = 0;
	public static final String					URI = "WindSensorModel";
	protected XYPlotter							windPlotter;
	protected final Value<Double>				currentWind = new Value<Double>(this, 0.0, 0);
	protected EmbeddingComponentStateAccessI 	componentRef;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public WindSensorModel(
			String uri,
			TimeUnit simulatedTimeUnit,
			SimulatorI simulationEngine
			) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
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
		this.setLogger(new StandardLogger());
	}

	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	@Override
	public void	setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
		this.componentRef = (EmbeddingComponentStateAccessI) simParams.get(CVM.windSensorRef);
	}

	@Override
	public void	initialiseState(Time initialTime){
		this.windPlotter.initialise();
		this.windPlotter.showPlotter();
		super.initialiseState(initialTime);
	}

	@Override
	protected void initialiseVariables(Time startTime) {
		this.currentWind.v =(((Math.sin(xValue+8)+1.0/10*Math.cos((xValue+2)*5)+ Math.cos((xValue*7)/2.0))*3)+6);
		this.windPlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getWind());
		super.initialiseVariables(startTime);
	}


	@Override
	public Vector<EventI> output() {
		return null;
	}

	@Override
	public Duration	timeAdvance(){
		if (this.componentRef == null) {
			return Duration.INFINITY;
		} else {
			return new Duration(10.0, TimeUnit.SECONDS);
		}
	}

	@Override
	public void userDefinedExternalTransition(Duration elapsedTime) {
		Vector<EventI> currentEvents = this.getStoredEventAndReset();
		assert	currentEvents != null && currentEvents.size() == 1;
		Event ce = (Event) currentEvents.get(0);
		assert	ce instanceof AbstractEvent;
		this.windPlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getWind());
		ce.executeOn(this);
		this.windPlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getWind());
		super.userDefinedExternalTransition(elapsedTime);

	}

	@Override
	public void	endSimulation(Time endTime) throws Exception {
		this.windPlotter.addData(
				SERIES,
				endTime.getSimulatedTime(),
				this.getWind());
		Thread.sleep(10000L);
		this.windPlotter.dispose();
		super.endSimulation(endTime);
	}

	// ------------------------------------------------------------------------
	// Model-specific methods
	// ------------------------------------------------------------------------

	public double getWind() {return this.currentWind.v;}

	// ------------------------------------------------------------------------
	// Utils
	// ------------------------------------------------------------------------

	public void	updateWind() {
		currentWind.v =(((Math.sin(xValue+8)+1.0/10*Math.cos((xValue+2)*5)+ Math.cos((xValue*7)/2.0))*3)+6);
		xValue += 0.1;
	}
}