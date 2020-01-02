package simulation.models.kettle;

import java.util.Vector;
import java.util.concurrent.TimeUnit;
import org.apache.commons.math3.random.RandomDataGenerator;

import fr.sorbonne_u.cyphy.examples.sg.equipments.hairdryer.models.events.SetHigh;
import fr.sorbonne_u.cyphy.examples.sg.equipments.hairdryer.models.events.SetLow;
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
import simulation.events.kettle.KettleUpdater;

@ModelExternalEvents(exported = {SwitchOn.class,
								 SwitchOff.class,
								 FillKettle.class,
								 EmptyKettle.class,
								 KettleUpdater.class})

public class KettleUserModel extends AtomicES_Model {
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L ;
	public static final String	URI = "KettleUserModel" ;

	/** initial delay before sending the first switch on event.				*/
	protected double	initialDelay ;
	/** delay between uses of the kettle from one day to another.		*/
	protected double	interdayDelay ;
	/** mean time between uses of the kettle in the same day.			*/
	protected double	meanTimeBetweenUsages ;

	protected double	meanTimeBetweenTempUpdate ;
	/** next event to be sent.												*/
	protected Class<?>	nextEvent ;

	/**	a random number generator from common math library.					*/
	protected final RandomDataGenerator		rg ;
	/** the current state of the kettle simulation model.				*/
	protected KettleModel.State ks ;
	
	protected boolean initialCall;
	protected boolean offCalled;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public				KettleUserModel(
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
		
		this.initialCall = true;
		this.offCalled = false;
		
		this.meanTimeBetweenTempUpdate = 7.0;
		this.ks = KettleModel.State.OFF ;

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
		this.scheduleEvent(new FillKettle(t)) ;

		
		
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
		this.logMessage("KettleUserModel::timeAdvance() 1 " + d +
									" " + this.eventListAsString()) ;
		return d ;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.es.models.AtomicES_Model#output()
	 */
	@Override
	public Vector<EventI>	output()
	{
		// output is called just before executing an internal transition
		// in ES models, this corresponds to having at least one event in
		// the event list which time of occurrence corresponds to the current
		// simulation time when performing the internal transition.

		// when called, there must be an event to be executed and it will
		// be sent to other models when they are external events.
		assert	!this.eventList.isEmpty() ;
		// produce the set of such events by calling the super method
		Vector<EventI> ret = super.output() ;
		// by construction, there will be only one such event
		assert	ret.size() == 1 ;

		// remember which external event was sent (in ES model, events are
		// either internal or external, hence an external event is removed
		// from the event list to be sent and it will not be accessible to
		// the internal transition method; hence, we store the information
		// to keep it for the internal transition)
		this.nextEvent = ret.get(0).getClass() ;

		this.logMessage("KettleUserModel::output() " +
									this.nextEvent.getCanonicalName()) ;
		return ret ;
	}

	@Override
	public void				userDefinedInternalTransition(
		Duration elapsedTime
		)
	{
		// This method implements a usage scenario for the kettle.
		// Here, we assume that the kettle is used once each cycle (day)
		// and then it starts in low mode, is set in high mode shortly after,
		// used for a while in high mode and then set back in low mode to
		// complete the drying.
		Duration d ;
		// See what is the type of event to be executed
		
	/*	if (this.nextEvent.equals(FillKettle.class)) {
			// when a switch on event has been issued, plan the next event as
			// a set high (the kettle is switched on in low mode
			d = new Duration(2.0 * this.rg.nextBeta(1.75, 1.75),
							 this.getSimulatedTimeUnit()) ;
			// compute the time of occurrence (in the future)
			Time t = this.getCurrentStateTime().add(d) ;
			// schedule the event
			this.scheduleEvent(new SwitchOn(t)) ;

			// also, plan the next switch on for the next day
			d = new Duration(this.interdayDelay, this.getSimulatedTimeUnit()) ;
			this.scheduleEvent(
						new KettleUpdater(this.getCurrentStateTime().add(d))) ;
			//System.out.println("Kettle Update " + nextEvent.toString());
		
			if(this.initialCall)
			{
				this.logMessage("test initial call...");
				d = new Duration(this.meanTimeBetweenTempUpdate, this.getSimulatedTimeUnit()) ;
				this.scheduleEvent(
							new KettleUpdater(this.getCurrentStateTime().add(d))) ;
				this.initialCall = false;
			}
			
			
		}else {
			if (this.nextEvent.equals(KettleUpdater.class)) {
				//System.out.println("next event : KettleUpdater");
				// when a set high event has been issued, plan the next switch off
				// after some time of usage
				System.out.println("PROCESS");
				d =	new Duration(
						2.0 * this.meanTimeBetweenTempUpdate * this.rg.nextBeta(1.75, 1.75),
						this.getSimulatedTimeUnit()) ;
				this.scheduleEvent(
					new KettleUpdater(this.getCurrentStateTime().add(d))) ;
				
				
			}
		}*/
		if (this.nextEvent.equals(FillKettle.class)) {
			System.out.println("FILLING KETTLE");
			// when a fill kettle event has been issued, plan the next event as
			// a switchOn (the kettle is initialized as OFF)
			d = new Duration(2.0 * this.rg.nextBeta(1.75, 1.75),
							 this.getSimulatedTimeUnit()) ;
			// compute the time of occurrence (in the future)
			Time t = this.getCurrentStateTime().add(d) ;
			// schedule the event
			this.scheduleEvent(new SwitchOn(t)) ;
			// also, plan the EmptyKettle Event 
			
			d = new Duration(this.interdayDelay, this.getSimulatedTimeUnit()) ;
			this.scheduleEvent(
						new EmptyKettle(this.getCurrentStateTime().add(d))) ;
		} else if (this.nextEvent.equals(SwitchOn.class)) {
			//When a SwitchOn is issued, plan the SwitchOff event, then update the KettleData
			System.out.println("SWITCH ON KETTLE");
			d =	new Duration(
					2.0 * this.interdayDelay * this.rg.nextBeta(1.75, 1.75),
					this.getSimulatedTimeUnit()) ;
			//Plan the SwitchOff event
			/*this.scheduleEvent(new SwitchOff(this.getCurrentStateTime().add(d))) ;
			d =	new Duration(
					2.0 * this.meanTimeBetweenTempUpdate * this.rg.nextBeta(1.75, 1.75),
					this.getSimulatedTimeUnit()) ;*/
			//update data
			this.scheduleEvent(new KettleUpdater(this.getCurrentStateTime().add(d)));
		} else if (this.nextEvent.equals(KettleUpdater.class)) {
			//when a KettleUpdater is issued, continue the data update
			System.out.println("UPDATING...");
			d =	new Duration(
					2.0 * this.meanTimeBetweenTempUpdate * this.rg.nextBeta(1.75, 1.75),
					this.getSimulatedTimeUnit()) ;
			this.scheduleEvent(
					new KettleUpdater(this.getCurrentStateTime().add(d))) ;
		}else if(this.nextEvent.equals(EmptyKettle.class)) {
			//when an EmptyKettle is issued, 
			System.out.println("EMPTY KETTLE");
			d =	new Duration(
					this.meanTimeBetweenUsages * this.rg.nextBeta(1.75, 1.75),
					this.getSimulatedTimeUnit()) ;
			this.scheduleEvent(
					new SwitchOff(this.getCurrentStateTime().add(d))) ;
			
			d =	new Duration(
					2.0 * this.interdayDelay * this.rg.nextBeta(1.75, 1.75),
					this.getSimulatedTimeUnit()) ;
			this.scheduleEvent(
					new FillKettle(this.getCurrentStateTime().add(d))) ;
		}
		
	}
	
}
