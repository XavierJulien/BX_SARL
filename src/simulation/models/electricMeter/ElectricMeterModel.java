package simulation.models.electricMeter;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import bcm.launcher.CVM;
import fr.sorbonne_u.components.cyphy.interfaces.EmbeddingComponentStateAccessI;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
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
import simulation.events.electricMeter.ElectricMeterUpdater;

@ModelExternalEvents(imported = {ElectricMeterUpdater.class})
public class ElectricMeterModel extends	AtomicHIOAwithEquations {
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long		serialVersionUID = 1L ;
	public static final String		URI = "ElectricMeterModel" ;
	private static final String		SERIES = "consumption" ;
	protected static final double	TENSION = 220.0 ;
	@ExportedVariable(type = Double.class)
	protected final Value<Double>				currentConsumption = new Value<Double>(this, 0.0, 0);
	protected XYPlotter				consumptionPlotter ;
	protected EmbeddingComponentStateAccessI componentRef ;

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
	public ElectricMeterModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine) ;
		PlotterDescription pd =
				new PlotterDescription(
						"Total house consumption",
						"Time (sec)",
						"Consumption",
						600,
						250,
						300,
						200) ;
		this.consumptionPlotter = new XYPlotter(pd) ;
		this.consumptionPlotter.createSeries(SERIES) ;
		this.setLogger(new StandardLogger()) ;
	}
	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------
	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#setSimulationRunParameters(java.util.Map)
	 */
	@Override
	public void	setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
		this.componentRef = (EmbeddingComponentStateAccessI) simParams.get(CVM.EMRef) ;
	}
	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void	initialiseState(Time initialTime) {
		this.consumptionPlotter.initialise() ;
		this.consumptionPlotter.showPlotter() ;
		super.initialiseState(initialTime) ;
	}
	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseVariables(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	protected void initialiseVariables(Time startTime) {
		this.currentConsumption.v = 0.0 ;
		this.consumptionPlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getConsumption());
		super.initialiseVariables(startTime);
	}
	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
	 */
	@Override
	public Vector<EventI> output() {
		return null ;
	}
	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
	 */
	@Override
	public Duration	timeAdvance(){
		if (this.componentRef == null) {
			return Duration.INFINITY ;
		} else {
			return new Duration(10.0, TimeUnit.SECONDS) ;
		}
	}
	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void	userDefinedExternalTransition(Duration elapsedTime){

		Vector<EventI> currentEvents = this.getStoredEventAndReset() ;
		assert	currentEvents != null && currentEvents.size() == 1 ;
		Event ce = (Event) currentEvents.get(0) ;
		assert	ce instanceof AbstractEvent ;
		this.consumptionPlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getConsumption());
		ce.executeOn(this) ;
		this.consumptionPlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getConsumption());
		super.userDefinedExternalTransition(elapsedTime) ;
	}
	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void	endSimulation(Time endTime) throws Exception {
		this.consumptionPlotter.addData(
				SERIES,
				endTime.getSimulatedTime(),
				this.getConsumption()) ;
		Thread.sleep(10000L) ;
		this.consumptionPlotter.dispose() ;
		super.endSimulation(endTime) ;
	}

	// ------------------------------------------------------------------------
	// Model-specific methods
	// ------------------------------------------------------------------------
	/**
	 * return the consumption of the electric meter.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	ret != null
	 * </pre>
	 *
	 * @return	the consumption of the electric meter.
	 */
	public double getConsumption() {return currentConsumption.v;}

	/**
	 * This method is called by the ElectricMeterUpdater events to update the consumption.
	 * It reads the total consumption in the component ref 
	 */
	public void updateConsumption() {
		
		try {
			this.currentConsumption.v = (Double)componentRef.getEmbeddingComponentStateValue("total");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}