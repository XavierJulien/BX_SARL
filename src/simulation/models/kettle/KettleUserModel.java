package simulation.models.kettle;

import java.util.Vector;
import java.util.concurrent.TimeUnit;
import org.apache.commons.math3.random.RandomDataGenerator;

import fr.sorbonne_u.devs_simulation.es.models.AtomicES_Model;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;
import simulation.events.kettle.FillKettle;
import simulation.events.kettle.EmptyKettle;
import simulation.events.kettle.SwitchOff;
import simulation.events.kettle.SwitchOn;
import simulation.events.kettle.UpdaterKettle;

@ModelExternalEvents(exported = {SwitchOn.class,
								 SwitchOff.class,
								 FillKettle.class,
								 EmptyKettle.class,
								 UpdaterKettle.class})

/**
 * The class <code>KettleUserModel</code> implements a simple user simulation
 * model for the kettle.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * The model is meant to send events controlling the use of the kettle:
 * switching on and off, filling or emptying it.
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
public class KettleUserModel extends AtomicES_Model {
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L ;
	public static final String	URI = "KettleUserModel" ;
	protected double	meanTimeBetweenTempUpdate ;
	protected Class<?>	nextEvent ;
	protected final RandomDataGenerator		rg ;
	protected KettleModel.State ks ;
	
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a kettle user model instance.
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
	public				KettleUserModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine) ;
		this.rg = new RandomDataGenerator() ;
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
		this.meanTimeBetweenTempUpdate = 7.0;
		this.ks = KettleModel.State.OFF ;
		this.rg.reSeedSecure() ;
		super.initialiseState(initialTime) ;
		Duration d1 = new Duration(this.meanTimeBetweenTempUpdate,this.getSimulatedTimeUnit()) ;
		Duration d2 =
			new Duration(
					2.0 * this.meanTimeBetweenTempUpdate *
											this.rg.nextBeta(1.75, 1.75),
					this.getSimulatedTimeUnit()) ;
		Time t = this.getCurrentStateTime().add(d1).add(d2) ;
		this.scheduleEvent(new FillKettle(t)) ;
		this.nextTimeAdvance = this.timeAdvance() ;
		this.timeOfNextEvent = this.getCurrentStateTime().add(this.nextTimeAdvance) ;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.es.models.AtomicES_Model#timeAdvance()
	 */
	@Override
	public Duration timeAdvance() {
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
		if (this.nextEvent.equals(FillKettle.class)) {
			d = new Duration(2.0 * this.rg.nextBeta(1.75, 1.75),this.getSimulatedTimeUnit()) ;
			Time t = this.getCurrentStateTime().add(d) ;
			this.scheduleEvent(new SwitchOn(t)) ;
			d = new Duration(this.meanTimeBetweenTempUpdate, this.getSimulatedTimeUnit()) ;
			this.scheduleEvent(new EmptyKettle(this.getCurrentStateTime().add(d))) ;
		} else if (this.nextEvent.equals(SwitchOn.class)) {
			d =	new Duration(2.0 * this.meanTimeBetweenTempUpdate * this.rg.nextBeta(1.75, 1.75),this.getSimulatedTimeUnit()) ;
			this.scheduleEvent(new UpdaterKettle(this.getCurrentStateTime().add(d)));
		} else if (this.nextEvent.equals(UpdaterKettle.class)) {
			d =	new Duration(2.0 * this.meanTimeBetweenTempUpdate * this.rg.nextBeta(1.75, 1.75),this.getSimulatedTimeUnit()) ;
			this.scheduleEvent(new UpdaterKettle(this.getCurrentStateTime().add(d))) ;
		}else if(this.nextEvent.equals(EmptyKettle.class)) {
			d =	new Duration(this.meanTimeBetweenTempUpdate * this.rg.nextBeta(1.75, 1.75)*4,this.getSimulatedTimeUnit()) ;
			this.scheduleEvent(new SwitchOff(this.getCurrentStateTime().add(d))) ;
			d =	new Duration(2.0 * this.meanTimeBetweenTempUpdate * this.rg.nextBeta(1.75, 1.75),this.getSimulatedTimeUnit()) ;
			this.scheduleEvent(new FillKettle(this.getCurrentStateTime().add(d))) ;
		}
	}
}