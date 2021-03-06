package simulation.models.battery;

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
import simulation.events.battery.UpdateBattery;

@ModelExternalEvents(imported = { UpdateBattery.class})

public class BatteryModel extends AtomicHIOAwithEquations {
	
	public static enum 		Mode {
		CHARGING,
		DISCHARGING
	}
	
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;
	private static final String SERIES = "battery";
	private static final String SERIES2 = "mode";
	public static final String URI = "BatteryModel";
	protected XYPlotter batteryRemainingPlotter;
	protected XYPlotter batteryModePlotter;
	protected EmbeddingComponentStateAccessI componentRef;
	protected Duration delay;


	protected Mode 								currentMode;
	protected final Value<Double> 				currentBattery = new Value<Double>(this, 0.0, 0) ;

	
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
	public BatteryModel(String uri,TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		PlotterDescription pd = 
				new PlotterDescription(
						"Remaining Battery", 
						"Time (sec)", 
						"kw", 
						0, 
						500, 
						300,
						200);
		this.batteryRemainingPlotter = new XYPlotter(pd);
		this.batteryRemainingPlotter.createSeries(SERIES);
		PlotterDescription pd2 = 
				new PlotterDescription(
						"Battery Mode", 
						"Time (sec)", 
						"CHARGING = 1 / DISCHARGING = 0", 
						300, 
						500, 
						300,
						200);
		this.batteryModePlotter = new XYPlotter(pd2);
		this.batteryModePlotter.createSeries(SERIES2);
		this.setLogger(new StandardLogger());
	}
	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------
	
	@Override
	public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
		this.componentRef = (EmbeddingComponentStateAccessI) simParams.get(CVM.batteryRef);
		this.delay = new Duration(1.0, this.getSimulatedTimeUnit());
	}

	@Override
	public void initialiseState(Time initialTime) {
		
		this.currentMode = Mode.DISCHARGING;
		this.batteryRemainingPlotter.initialise();
		this.batteryRemainingPlotter.showPlotter();
		this.batteryModePlotter.initialise();
		this.batteryModePlotter.showPlotter();

		super.initialiseState(initialTime);
	}
	
	@Override
	protected void initialiseVariables(Time startTime) {

		this.currentBattery.v = 0.0;
		this.batteryRemainingPlotter.addData(
				SERIES, 
				this.getCurrentStateTime().getSimulatedTime(), 
				currentBattery.v);
		this.batteryModePlotter.addData(
				SERIES2, 
				this.getCurrentStateTime().getSimulatedTime(), 
				0);
		super.initialiseVariables(startTime);
	}

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
		this.batteryRemainingPlotter.addData(
				SERIES, 
				this.getCurrentStateTime().getSimulatedTime(), 
				currentBattery.v);
		this.batteryModePlotter.addData(
				SERIES2, 
				this.getCurrentStateTime().getSimulatedTime(), 
				this.getModeDouble());
		ce.executeOn(this);
		this.batteryRemainingPlotter.addData(
				SERIES, 
				this.getCurrentStateTime().getSimulatedTime(), 
				currentBattery.v);
		this.batteryModePlotter.addData(
				SERIES2, 
				this.getCurrentStateTime().getSimulatedTime(), 
				this.getModeDouble());
		super.userDefinedExternalTransition(elapsedTime);
	}

	@Override
	public void	endSimulation(Time endTime) throws Exception
	{
		this.batteryRemainingPlotter.addData(
				SERIES, 
				this.getCurrentStateTime().getSimulatedTime(), 
				currentBattery.v);
		Thread.sleep(10000L);
		this.batteryRemainingPlotter.dispose();
		this.batteryModePlotter.addData(
				SERIES2, 
				this.getCurrentStateTime().getSimulatedTime(), 
				this.getModeDouble());
		Thread.sleep(10000L);
		this.batteryModePlotter.dispose();
		super.endSimulation(endTime);
	}
	
	// ------------------------------------------------------------------------
	// Model-specific methods
	// ------------------------------------------------------------------------
	/**
	 * 
	 * @return the current mode of the battery : Charging / discharging
	 */
	public Mode	getMode() {return this.currentMode;}
 
	public double getBattery() {return currentBattery.v;}
	
	public double getModeDouble() {
		if(this.getMode() == Mode.DISCHARGING) {
			return 1;
		}else {
			return 0;
		}
	}

	// ------------------------------------------------------------------------
	// Utils
	// ------------------------------------------------------------------------
	/**
	 * the method update is called by the updateBattery events
	 * is updates the battery mode
	 */
	public void update(){
		try {
			this.currentBattery.v = (Double)componentRef.getEmbeddingComponentStateValue("charge");
			boolean b = (Boolean)componentRef.getEmbeddingComponentStateValue("charging");
			if(b) {
				this.currentMode = Mode.CHARGING;
			}else {
				this.currentMode = Mode.DISCHARGING;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Vector<EventI> output() {return null;}
}