package simulation.models.charger;

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
import simulation.events.charger.UpdateCharger;

@ModelExternalEvents(imported = { UpdateCharger.class})

public class ChargerModel extends AtomicHIOAwithEquations {
	
	/**
	 * The enum Mode represent the mode of the component (charging/off)
	 * 
	 *
	 */
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
	/**
	 * create a model instance for this component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	uri != null
	 * pre	simulatedTimeUnit != null
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param uri				URI of the model.
	 * @param simulatedTimeUnit	time unit used for the simulation time.
	 * @param simulationEngine	simulation engine to which the model is attached.
	 * @throws Exception		<i>to do.</i>
	 */
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
	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#setSimulationRunParameters(java.util.Map)
	 */
	@Override
	public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
		this.componentRef = (EmbeddingComponentStateAccessI) simParams.get(CVM.chargerRef);
		this.delay = new Duration(1.0, this.getSimulatedTimeUnit());
	}
	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void initialiseState(Time initialTime) {
		this.currentMode = Mode.OFF;
		this.consumptionPlotter.initialise();
		this.consumptionPlotter.showPlotter();
		this.chargerModePlotter.initialise();
		this.chargerModePlotter.showPlotter();
		super.initialiseState(initialTime);
	}
	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseVariables(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
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
	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
	 */
	@Override
	public Vector<EventI> output() {return null;}
	
	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
	 */
	public Duration timeAdvance() {
		if (this.componentRef == null) {
			return Duration.INFINITY;
		} else {
			return new Duration(10.0, TimeUnit.SECONDS);
		}
	}
	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
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

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
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
	/**
	 * return the mode of the charger.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	ret != null
	 * </pre>
	 *
	 * @return	the mode of the charger.
	 */
	public Mode	getMode() {return this.currentMode;}
	/**
	 * return the consumption of the charger.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	ret != null
	 * </pre>
	 *
	 * @return	the consumption of the charger.
	 */
	public double getConsumption() {return currentConsumption.v;}
	/**
	 * return the mode as a double.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	ret != null
	 * </pre>
	 *
	 * @return	the mode as a double.
	 */
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
	/**
	 * update the mode of the charger.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition
	 * </pre>
	 *
	 */
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