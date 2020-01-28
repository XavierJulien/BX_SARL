package simulation.models.electricMeter;

import java.util.Vector;
import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.devs_simulation.es.models.AtomicES_Model;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;
import simulation.events.electricMeter.UpdaterElectricMeter;

@ModelExternalEvents(exported = {UpdaterElectricMeter.class})

/**
 * The class <code>ElectricMeterUpdaterModel</code> implements a simple user simulation
 * model for the electric meter.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * The model is meant to send an updater event to the electric meter so it can update it's values.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * 
 * @author	Julien Xavier & Alexis Belanger</a>
 */
public class ElectricMeterUpdaterModel extends AtomicES_Model {
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------
	private static final long serialVersionUID = 1L ;
	public static final String	URI = "ElectricMeterUpdaterModel" ;
	protected double	initialDelay ;
	protected double	interdayDelay ;
	protected double	meanTimeBetweenConsumptionUpdate ;
	protected Class<?>	nextEvent ;
	
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------
	
	/**
	 * create a electric meter updater model instance.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	simulatedTimeUnit != null
	 * pre	simulationEngine == null ||
	 * 		    	simulationEngine instanceof HIOA_AtomicEngine
	 * post	this.getURI() != null
	 * post	uri != null implies this.getURI().equals(uri)
	 * post	this.getSimulatedTimeUnit().equals(simulatedTimeUnit)
	 * post	simulationEngine != null implies
	 * 			this.getSimulationEngine().equals(simulationEngine)
	 * </pre>
	 *
	 * @param uri					unique identifier of the model.
	 * @param simulatedTimeUnit		time unit used for the simulation clock.
	 * @param simulationEngine		simulation engine enacting the model.
	 * @throws Exception   			<i>TODO</i>.
	 */
	public ElectricMeterUpdaterModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine) ;
		this.setLogger(new StandardLogger()) ;
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void	initialiseState(Time initialTime) {
		this.initialDelay = 10.0 ;
		this.interdayDelay = 100.0 ;
		super.initialiseState(initialTime) ;
		Duration d1 = new Duration(
							this.initialDelay,
							this.getSimulatedTimeUnit()) ;
		Duration d2 =
				new Duration(1, TimeUnit.SECONDS) ;
		Time t = this.getCurrentStateTime().add(d1).add(d2) ;
		this.scheduleEvent(new UpdaterElectricMeter(t)) ;
		this.nextTimeAdvance = this.timeAdvance() ;
		this.timeOfNextEvent = this.getCurrentStateTime().add(this.nextTimeAdvance) ;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.es.models.AtomicES_Model#timeAdvance()
	 */
	@Override
	public Duration	timeAdvance(){
		Duration d = super.timeAdvance() ;
		return d ;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.es.models.AtomicES_Model#output()
	 */
	@Override
	public Vector<EventI> output() {
		assert	!this.eventList.isEmpty() ;
		Vector<EventI> ret = super.output() ;
		assert	ret.size() == 1 ;
		this.nextEvent = ret.get(0).getClass() ;
		return ret ;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void	userDefinedInternalTransition(Duration elapsedTime){	
		Duration d ;
		if (this.nextEvent.equals(UpdaterElectricMeter.class)) {
			d = new Duration(1,
							 this.getSimulatedTimeUnit()) ;
			Time t = this.getCurrentStateTime().add(d) ;
			this.scheduleEvent(new UpdaterElectricMeter(t)) ;
		}
	}
}