package simulation.models.heatSensor;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import bcm.launcher.CVM;
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
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long					serialVersionUID = 1L;
	private static final String					SERIES = "temperature";
	private static double 						xValue = 0;
	public static final String					URI = "HeatSensorModel";
	protected XYPlotter							temperaturePlotter;
	protected final Value<Double>				currentTemperature = new Value<Double>(this, 0.0, 0);
	protected EmbeddingComponentStateAccessI 	componentRef;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public HeatSensorModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception{
		super(uri, simulatedTimeUnit, simulationEngine);
		PlotterDescription pd =
				new PlotterDescription(
						"Temperature",
						"Time (sec)",
						"Temp (°C)",
						0,
						750,
						300,
						200);
		this.temperaturePlotter = new XYPlotter(pd);
		this.temperaturePlotter.createSeries(SERIES);
		this.setLogger(new StandardLogger());
	}

	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	@Override
	public void	setSimulationRunParameters(
		Map<String, Object> simParams
		) throws Exception {
		this.componentRef = (EmbeddingComponentStateAccessI) simParams.get(CVM.temperatureSensorRef);
	}

	@Override
	public void initialiseState(Time initialTime){
		this.temperaturePlotter.initialise();
		this.temperaturePlotter.showPlotter();
		super.initialiseState(initialTime);
	}

	@Override
	protected void initialiseVariables(Time startTime) {
		this.currentTemperature.v =(((Math.sin(xValue+8)+1.0/10*Math.cos((xValue+2)*5)+ Math.cos((xValue*7)/2.0))*3)+6);
		this.temperaturePlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getTemperature());
		super.initialiseVariables(startTime);
	}


	@Override
	public Vector<EventI> output(){
		return null;
	}


	@Override
	public Duration timeAdvance(){
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
		this.temperaturePlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getTemperature());
		ce.executeOn(this);
		this.temperaturePlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getTemperature());
		super.userDefinedExternalTransition(elapsedTime);
	}


	@Override
	public void endSimulation(Time endTime) throws Exception {
		this.temperaturePlotter.addData(
				SERIES,
				endTime.getSimulatedTime(),
				this.getTemperature());
		Thread.sleep(10000L);
		this.temperaturePlotter.dispose();
		super.endSimulation(endTime);
	}

	// ------------------------------------------------------------------------
	// Model-specific methods
	// ------------------------------------------------------------------------
	
	public double getTemperature(){return this.currentTemperature.v;}
	
	// ------------------------------------------------------------------------
	// Utils
	// ------------------------------------------------------------------------

	public void updateTemperature() {
		try {
			this.currentTemperature.v = ((Double)componentRef.getEmbeddingComponentStateValue("temperature"));
			this.currentTemperature.v *= 0.90;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void openWindow() {this.currentTemperature.v -= 5;}
	public void keepTemperature() {this.currentTemperature.v -=0.5;}
}
