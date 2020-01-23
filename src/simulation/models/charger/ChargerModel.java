package simulation.models.charger;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

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
import simulation.events.charger.UpdateCharger;

@ModelExternalEvents(imported = { UpdateCharger.class})

public class ChargerModel extends AtomicHIOAwithEquations {
	public static enum 		Mode {
		CHARGING,
		OFF
	}
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;
	private static final String SERIES = "consumption";
	private static final String SERIES2 = "mode";
	public static final String URI = "ChargerModel";
	protected XYPlotter consumptionPlotter;
	protected XYPlotter chargerModePlotter;
	protected EmbeddingComponentStateAccessI componentRef;
	protected Duration delay;

	protected Mode 								currentMode;
	protected final Value<Double> 				currentConsumption = new Value<Double>(this, 0.0, 0) ;
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public ChargerModel(String uri,TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		PlotterDescription pd = 
				new PlotterDescription(
						"Consumption", 
						"Time (sec)", 
						"kw", 
						600, 
						500, 
						300,
						200);
		this.consumptionPlotter = new XYPlotter(pd);
		this.consumptionPlotter.createSeries(SERIES);
		PlotterDescription pd2 = 
				new PlotterDescription(
						"Charger Mode", 
						"Time (sec)", 
						"CHARGING = 1 / OFF = 0", 
						600, 
						750, 
						300,
						200);
		this.chargerModePlotter = new XYPlotter(pd2);
		this.chargerModePlotter.createSeries(SERIES2);
		this.setLogger(new StandardLogger());
	}
	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	@Override
	public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
		this.componentRef = (EmbeddingComponentStateAccessI) simParams.get("chargerRef");
		this.delay = new Duration(1.0, this.getSimulatedTimeUnit());
	}

	@Override
	public void initialiseState(Time initialTime) {
		this.currentMode = Mode.OFF;
		this.consumptionPlotter.initialise();
		this.consumptionPlotter.showPlotter();
		this.chargerModePlotter.initialise();
		this.chargerModePlotter.showPlotter();
		super.initialiseState(initialTime);
	}

	@Override
	protected void initialiseVariables(Time startTime) {
		this.currentConsumption.v = 0.0;
		this.consumptionPlotter.addData(
				SERIES, 
				this.getCurrentStateTime().getSimulatedTime(), 
				currentConsumption.v);
		this.chargerModePlotter.addData(
				SERIES2, 
				this.getCurrentStateTime().getSimulatedTime(), 
				0);
		super.initialiseVariables(startTime);
	}

	@Override
	public Vector<EventI> output() {return null;}
	
	public Duration timeAdvance() {
		if (this.componentRef == null) {
			return Duration.INFINITY;
		} else {
			return new Duration(10.0, TimeUnit.SECONDS);
		}
	}

	public void userDefinedExternalTransition(Duration elapsedTime) {
		Vector<EventI> currentEvents = this.getStoredEventAndReset();
		assert currentEvents != null && currentEvents.size() == 1;
		Event ce = (Event) currentEvents.get(0);
		assert ce instanceof AbstractEvent;
		this.consumptionPlotter.addData(
				SERIES, 
				this.getCurrentStateTime().getSimulatedTime(), 
				currentConsumption.v);
		this.chargerModePlotter.addData(
				SERIES2, 
				this.getCurrentStateTime().getSimulatedTime(), 
				this.getModeDouble());
		ce.executeOn(this);
		this.consumptionPlotter.addData(
				SERIES, 
				this.getCurrentStateTime().getSimulatedTime(), 
				currentConsumption.v);
		this.chargerModePlotter.addData(
				SERIES2, 
				this.getCurrentStateTime().getSimulatedTime(), 
				this.getModeDouble());
		super.userDefinedExternalTransition(elapsedTime);
	}

	
	@Override
	public void endSimulation(Time endTime) throws Exception {
		this.consumptionPlotter.addData(
				SERIES, 
				this.getCurrentStateTime().getSimulatedTime(), 
				currentConsumption.v);
		Thread.sleep(10000L);
		this.consumptionPlotter.dispose();
		this.chargerModePlotter.addData(
				SERIES2, 
				this.getCurrentStateTime().getSimulatedTime(), 
				this.getModeDouble());
		Thread.sleep(10000L);
		this.chargerModePlotter.dispose();
		super.endSimulation(endTime);
	}
	
	// ------------------------------------------------------------------------
	// Model-specific methods
	// ------------------------------------------------------------------------
		
	public Mode	getMode() {return this.currentMode;}
	
	public double getConsumption() {return currentConsumption.v;}
	
	public double getModeDouble() {
		if(this.getMode() == Mode.CHARGING) {
			return 1;
		}else {
			return 0;
		}
	}

	// ------------------------------------------------------------------------
	// Utils
	// ------------------------------------------------------------------------
	public void update() { //se demerder pour envoyer un event
		try {
			boolean on =  (Boolean)componentRef.getEmbeddingComponentStateValue("state");
			if(on) {
				this.currentMode = Mode.CHARGING;
			}else {
				this.currentMode = Mode.OFF;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}