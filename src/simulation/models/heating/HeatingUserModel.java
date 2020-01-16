package simulation.models.heating;

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
import simulation.events.heating.HeatingUpdater;

@ModelExternalEvents(exported = { HeatingUpdater.class})

public class HeatingUserModel extends AtomicES_Model {
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L ;
	public static final String	URI = "HeatingUserModel" ;

	/** initial delay before sending the first switch on event.				*/
	protected double			initialDelay ;
	/** delay between uses of the heating from one day to another.		*/
	protected double			interdayDelay ;
	/** mean time between uses of the heating in the same day.			*/
	protected double			meanTimeBetweenUsages ;

	protected double			meanTimeBetweenTempUpdate ;
	/** next event to be sent.												*/
	protected Class<?>			nextEvent ;

	/**	a random number generator from common math library.					*/
	protected final RandomDataGenerator		rg ;
	/** the current state of the heating simulation model.				*/
	protected HeatingModel.State hs ;
	
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public				HeatingUserModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception
	{
		super(uri, simulatedTimeUnit, simulationEngine) ;

		this.rg = new RandomDataGenerator() ;

		// create a standard logger (logging on the terminal)
		this.setLogger(new StandardLogger()) ;
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	@Override
	public void			initialiseState(Time initialTime) 
	{
		
		this.initialDelay = 10.0 ;
		this.interdayDelay = 100.0 ;
		this.meanTimeBetweenUsages = 10.0 ;
		

		this.meanTimeBetweenTempUpdate = 7.0;
		this.hs = HeatingModel.State.OFF ;

		this.rg.reSeedSecure() ;

		// Initialise to get the correct current time.
		super.initialiseState(initialTime) ;

		// Schedule the first SwitchOn event.
		Duration d1 = new Duration(
							this.initialDelay,
							this.getSimulatedTimeUnit()) ;
		Duration d2 =
			new Duration(
					2.0 * this.meanTimeBetweenUsages *
											this.rg.nextBeta(1.75, 1.75),
					this.getSimulatedTimeUnit()) ;
		Time t = this.getCurrentStateTime().add(d1).add(d2) ;
		this.scheduleEvent(new HeatingUpdater(t)) ;

		
		
		// Redo the initialisation to take into account the initial event
		// just scheduled.
		this.nextTimeAdvance = this.timeAdvance() ;
		this.timeOfNextEvent =
				this.getCurrentStateTime().add(this.nextTimeAdvance) ;

		try {
			// set the debug level triggering the production of log messages.
			this.setDebugLevel(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.es.models.AtomicES_Model#timeAdvance()
	 */
	@Override
	public Duration			timeAdvance()
	{
		// This is just for debugging purposes; the time advance for an ES
		// model is given by the earliest time among the currently scheduled
		// events.
		Duration d = super.timeAdvance() ;
		this.logMessage("HeatingUserModel::timeAdvance() 1 " + d +
									" " + this.eventListAsString()) ;
		return d ;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.es.models.AtomicES_Model#output()
	 */
	@Override
	public Vector<EventI>	output()
	{
		assert	!this.eventList.isEmpty() ;
		Vector<EventI> ret = super.output() ;
		assert	ret.size() == 1 ;
		this.nextEvent = ret.get(0).getClass() ;
		
		this.logMessage("HeatingUserModel::output() " +
									this.nextEvent.getCanonicalName()) ;
		return ret ;
	}

	@Override
	public void				userDefinedInternalTransition(Duration elapsedTime){
		Duration d ;
		
		if (this.nextEvent.equals(HeatingUpdater.class)) {
			System.out.println("HEATING MODE");
			d = new Duration(this.meanTimeBetweenTempUpdate, this.getSimulatedTimeUnit()) ;
			Time t = this.getCurrentStateTime().add(d);
			this.scheduleEvent(new HeatingUpdater(t)) ;
		}else {
			System.out.println("UPDATER");
		}
	}
	
}
