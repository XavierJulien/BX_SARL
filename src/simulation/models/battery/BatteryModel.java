package simulation.models.battery;

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
import simulation.events.battery.UpdateBatteryCharge;

@ModelExternalEvents(imported = { UpdateBatteryCharge.class})

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

	//CURRENT
	protected Mode 								currentMode;
	protected final Value<Double> 				currentBattery = new Value<Double>(this, 0.0, 0) ;


	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public							BatteryModel(
			String uri,
			TimeUnit simulatedTimeUnit, 
			SimulatorI simulationEngine
			) throws Exception 
	{
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
		this.componentRef = (EmbeddingComponentStateAccessI) simParams.get("batteryRef");
		this.delay = new Duration(1.0, this.getSimulatedTimeUnit());
	}

	@Override
	public void initialiseState(Time initialTime) {
		
		this.currentMode = Mode.DISCHARGING;
		
		
		this.batteryRemainingPlotter.initialise();
		this.batteryRemainingPlotter.showPlotter();
		
		this.batteryModePlotter.initialise();
		this.batteryModePlotter.showPlotter();

		try {
			this.setDebugLevel(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

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

	@Override
	public Vector<EventI> output() {
		
		return null;
	}
	
	public Duration timeAdvance() {
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
	public void userDefinedInternalTransition(Duration elapsedTime) {
		if (this.componentRef != null) {

			try {
				this.logMessage("component state = " + 
						componentRef.getEmbeddingComponentStateValue("mode"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void userDefinedExternalTransition(Duration elapsedTime) {
		
		if (this.hasDebugLevel(2)) {
		//	this.logMessage("BatteryModel::userDefinedExternalTransition 1");
		}


		Vector<EventI> currentEvents = this.getStoredEventAndReset();

		assert currentEvents != null && currentEvents.size() == 1;

		Event ce = (Event) currentEvents.get(0);
		assert ce instanceof AbstractEvent;
		if (this.hasDebugLevel(2)) {
//			this.logMessage("TestModel::userDefinedExternalTransition 2 " + 
//										ce.getClass().getCanonicalName());
		}

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
		if (this.hasDebugLevel(2)) {
			this.logMessage("ChargerModel::userDefinedExternalTransition 5");
		}
	}

	
	@Override
	public void					endSimulation(Time endTime) throws Exception
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
		
	public Mode					getMode() {
		return this.currentMode;
	}
	
	
	public double getBattery() {
		return currentBattery.v;
	}
	
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

	
	public void update()
	{
		
		try {
			this.currentBattery.v = (Double)componentRef.getEmbeddingComponentStateValue("charge");
			boolean b = (Boolean)componentRef.getEmbeddingComponentStateValue("charging");
			if(b) {
				this.currentMode = Mode.CHARGING;
			}else {
				this.currentMode = Mode.DISCHARGING;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}