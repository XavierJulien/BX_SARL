package simulation.models.windturbine;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import bcm.launcher.CVM;
import fr.sorbonne_u.components.cyphy.interfaces.EmbeddingComponentStateAccessI;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
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
import simulation.events.windturbine.WindTurbineUpdater;

@ModelExternalEvents(imported = {WindTurbineUpdater.class})

public class WindTurbineModel extends AtomicHIOAwithEquations {

	public static enum State {
		OFF,
		ON
	}

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------
	private static final long		serialVersionUID = 1L ;
	public static final String		URI = "WindTurbineModel" ;
	private static final String		SERIES = "production" ;
	protected static final double	TENSION = 220.0 ; // Volts
	@ExportedVariable(type = Double.class)
	protected final Value<Double>	currentProd = new Value<Double>(this, 0.0, 0);
	protected  double 				wind;
	protected State					currentState ;
	protected XYPlotter				intensityPlotter ;
	protected EmbeddingComponentStateAccessI componentRef ;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a windturbine model instance.
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
	public				WindTurbineModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine) ;
		PlotterDescription pd =
				new PlotterDescription(
						"WindTurbine intensity",
						"Time (sec)",
						"Intensity (Amp)",
						300,
						250,
						300,
						200) ;
		this.intensityPlotter = new XYPlotter(pd) ;
		this.intensityPlotter.createSeries(SERIES) ;
		this.wind = 0;
		this.setLogger(new StandardLogger()) ;
	}

	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	@Override
	public void	setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
		this.componentRef = (EmbeddingComponentStateAccessI) simParams.get(CVM.windTurbineRef) ;
	}

	@Override
	public void	initialiseState(Time initialTime) {
		this.currentState = State.ON ;
		this.intensityPlotter.initialise() ;
		this.intensityPlotter.showPlotter() ;
		super.initialiseState(initialTime) ;
	}

	@Override
	protected void initialiseVariables(Time startTime) {
		this.currentProd.v = 0.0 ;
		this.intensityPlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getProduction());
		super.initialiseVariables(startTime);
	}

	@Override
	public Vector<EventI> output() {
		return null ;
	}

	@Override
	public Duration	timeAdvance() {
		if (this.componentRef == null) {
			return Duration.INFINITY ;
		} else {
			return new Duration(10.0, TimeUnit.SECONDS) ;
		}
	}

	@Override
	public void	userDefinedExternalTransition(Duration elapsedTime) {
		Vector<EventI> currentEvents = this.getStoredEventAndReset() ;
		assert	currentEvents != null && currentEvents.size() == 1 ;
		Event ce = (Event) currentEvents.get(0) ;
		assert	ce instanceof AbstractEvent ;
		this.intensityPlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getProduction());
		ce.executeOn(this) ;
		this.intensityPlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getProduction());
		super.userDefinedExternalTransition(elapsedTime) ;
	}

	@Override
	public void	endSimulation(Time endTime) throws Exception {
		this.intensityPlotter.addData(
				SERIES,
				endTime.getSimulatedTime(),
				this.getProduction()) ;
		Thread.sleep(10000L) ;
		this.intensityPlotter.dispose() ;
		super.endSimulation(endTime) ;
	}

	// ------------------------------------------------------------------------
	// Model-specific methods
	// ------------------------------------------------------------------------

	/**
	 * set the state of the windturbine.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	s != null
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param s		the new state.
	 */
	public void setState(State s){this.currentState = s ;}

	/**
	 * return the state of the windturbine.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	ret != null
	 * </pre>
	 *
	 * @return	the state of the windturbine.
	 */
	public State getState(){return this.currentState ;}

	/**
	 * return the current intensity of electricity consumption in amperes.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	{@code ret >= 0.0 and ret <= 1200.0/220.0}
	 * </pre>
	 *
	 * @return	the current intensity of electricity consumption in amperes.
	 */
	public double getProduction() {return currentProd.v;}
	
	public void updateProduction() {	
		try {
			this.wind = ((Double)componentRef.getEmbeddingComponentStateValue("windSpeed")).doubleValue();
			if((Boolean)componentRef.getEmbeddingComponentStateValue("state")) {
				this.currentState = State.ON;
			}else {
				this.currentState = State.OFF;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(currentState == State.OFF) {
			if(this.wind < 7) {
				setState(State.ON);
				currentProd.v = 2*wind;
			}else {
				currentProd.v = Math.max(0, currentProd.v-6);
			}
		}else {
			if(this.wind < 7) {
				currentProd.v = 2*wind;
			}else {
				setState(State.OFF);
				currentProd.v = Math.max(0, currentProd.v-6);
			}
		}		
	}
}