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

public class HeatingUpdaterModel extends AtomicES_Model
{
	private static final long serialVersionUID = 1L ;
	public static final String	URI = "HeatingUpdaterModel" ;
	
	/** initial delay before sending the first updater event.				*/
	protected double	initialDelay ;
	
	protected double	meanTimeBetweenTempUpdate ;
	/** next event to be sent.												*/
	protected Class<?>	nextEvent ;
	
	protected final RandomDataGenerator		rg ;
	
	protected boolean initialCall;
	
	
	public HeatingUpdaterModel(
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
	
	@Override
	public void			initialiseState(Time initialTime) 
	{
		
		this.initialDelay = 10.0 ;
		
		this.initialCall = true;
		
		this.meanTimeBetweenTempUpdate = 7.0;

		this.rg.reSeedSecure() ;

		// Initialise to get the correct current time.
		super.initialiseState(initialTime) ;

		// Schedule the first updater event.
		Duration d1 = new Duration(
							this.initialDelay,
							this.getSimulatedTimeUnit()) ;
		Duration d2 =
			new Duration(
					2.0 * this.meanTimeBetweenTempUpdate *
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
			this.setDebugLevel(1) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
	
	@Override
	public Duration			timeAdvance()
	{

		Duration d = super.timeAdvance() ;
		return d ;
	}
	
	@Override
	public Vector<EventI>	output()
	{
		assert	!this.eventList.isEmpty() ;
		Vector<EventI> ret = super.output() ;
		assert	ret.size() == 1 ;
		this.nextEvent = ret.get(0).getClass() ;
		return ret ;
	}
	
	
	/**
	 * 
	 */
	@Override
	public void userDefinedInternalTransition(Duration elapsedTime)	{

		Duration d ;
		if (this.nextEvent.equals(HeatingUpdater.class)) {
			d = new Duration(this.meanTimeBetweenTempUpdate, this.getSimulatedTimeUnit()) ;
			Time t = this.getCurrentStateTime().add(d) ;
			this.scheduleEvent(new HeatingUpdater(t)) ;
		}
		
	}
	
}
