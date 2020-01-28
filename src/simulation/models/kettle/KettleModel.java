package simulation.models.kettle;

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
import simulation.events.kettle.EmptyKettle;
import simulation.events.kettle.FillKettle;
import simulation.events.kettle.UpdaterKettle;
import simulation.events.kettle.SwitchOff;
import simulation.events.kettle.SwitchOn;

@ModelExternalEvents(imported = {SwitchOn.class,
								 SwitchOff.class,
								 FillKettle.class,
								 EmptyKettle.class,
								 UpdaterKettle.class})

public class KettleModel extends AtomicHIOAwithEquations {

	/**
	 * The enum State represent the state of the component (on/off)
	 * 
	 *
	 */
	public static enum State {
		OFF,
		ON
	}
	
	/**
	 * The enum Content represent the content of the kettle (on/off)
	 * 
	 *
	 */
	public static enum Content {
		FULL,
		HALF,
		EMPTY
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
	protected State					currentState;
	protected Content 				currentContent;
	protected final Value<Double>	currentTemperature = new Value<Double>(this, 0.0, 0);
	protected EmbeddingComponentStateAccessI componentRef;

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
	public KettleModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
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
		this.setLogger(new StandardLogger());
	}

	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------
	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#setSimulationRunParameters(java.util.Map)
	 */
	@Override
	public void	setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
		this.componentRef = (EmbeddingComponentStateAccessI) simParams.get(CVM.kettleRef);
	}
	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void	initialiseState(Time initialTime) {
		this.currentState = State.OFF;
		this.currentContent = Content.EMPTY;
		this.temperaturePlotter.initialise();
		this.temperaturePlotter.showPlotter();
		this.onOffPlotter.initialise();
		this.onOffPlotter.showPlotter();
		super.initialiseState(initialTime);
	}
	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseVariables(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	protected void initialiseVariables(Time startTime){
		this.currentTemperature.v = 0.0;
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

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
	 */
	@Override
	public Vector<EventI> output() {
		return null;
	}
	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
	 */
	@Override
	public Duration	timeAdvance() {
		if (this.componentRef == null) {
			return Duration.INFINITY;
		} else {
			return new Duration(10.0, TimeUnit.SECONDS);
		}
	}
	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void	userDefinedExternalTransition(Duration elapsedTime) {
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
	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void	endSimulation(Time endTime) throws Exception {
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

	// ------------------------------------------------------------------------
	// Model-specific methods
	// ------------------------------------------------------------------------
	/**
	 * return the state of the kettle.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	ret != null
	 * </pre>
	 *
	 * @return	the state of the kettle.
	 */
	public State getState() {return this.currentState;}
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
	public double getStateDouble() {
		if(this.getState() == State.ON) {
			return 1;
		}else {
			return 0;
		}
	}
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
	public Content getContent() {return this.currentContent;}
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
	public double getTemperature(){return this.currentTemperature.v;}
	
	// ------------------------------------------------------------------------
	// Utils
	// ------------------------------------------------------------------------
	/**
	 * update the temperature of the kettle.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition
	 * </pre>
	 *
	 * @return	void
	 */
	public void updateTemperature() {
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
	/**
	 * update the state of the kettle.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition
	 * </pre>
	 *
	 * @return	void
	 */
	public void	updateState() {
		if(currentTemperature.v >= 100.0 && currentState == State.ON) {
			currentState = State.OFF;
		}
		if(currentTemperature.v == 0.0 && currentState == State.OFF && currentContent != Content.EMPTY) {
			currentState = State.ON;
		}
	}
	/**
	 * update the state of the kettle with a defined state.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	s != null
	 * post	true			// no postcondition
	 * </pre>
	 *
	 * @return	void
	 */
	public void	updateState(State s){assert s!= null;this.currentState = s;}
	/**
	 * update the content of the kettle with a defined content.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	c != null
	 * post	true			// no postcondition
	 * </pre>
	 *
	 * @return	void
	 */
	public void	updateContent(Content c){assert c!= null;this.currentContent = c;}
}
