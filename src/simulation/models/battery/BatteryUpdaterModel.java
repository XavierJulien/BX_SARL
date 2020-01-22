package simulation.models.battery;

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
import simulation.events.battery.UpdateBattery;

@ModelExternalEvents(exported = { UpdateBattery.class})

public class BatteryUpdaterModel extends AtomicES_Model{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------
	
	private static final long serialVersionUID = 1L ;
	public static final String	URI = "BatteryUserModel" ;
	
	protected double	meanTimeBetweenBatteryUpdate ;
	
	protected Class<?>	nextEvent ;
	protected double chargingCapacity; // charge sent by the charger	
	protected double dischargingOnUse; // discharge while used
	protected double dischargingOffUse;// discharging while not used
	
	protected final RandomDataGenerator		rg ;
	protected BatteryModel.Mode bm;
	
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------
	
	public BatteryUpdaterModel(
			String uri,
			TimeUnit simulatedTimeUnit,
			SimulatorI simulationEngine
			) throws Exception
		{
			super(uri, simulatedTimeUnit, simulationEngine) ;
			
			this.rg = new RandomDataGenerator();
			// create a standard logger (logging on the terminal)
			this.setLogger(new StandardLogger()) ;
		}
	
	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	
	@Override
	public void			initialiseState(Time initialTime) 
	{

		
		
		this.meanTimeBetweenBatteryUpdate = 10.0;
		this.bm = BatteryModel.Mode.DISCHARGING;
		
		
		super.initialiseState(initialTime) ;

		Duration d1 = new Duration(
							this.meanTimeBetweenBatteryUpdate,
							this.getSimulatedTimeUnit()) ;
		
		Time t = this.getCurrentStateTime().add(d1);
		this.scheduleEvent(new UpdateBattery(t)) ;
		

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
	
	@Override
	public Duration			timeAdvance()
	{
		// This is just for debugging purposes; the time advance for an ES
		// model is given by the earliest time among the currently scheduled
		// events.
		Duration d = super.timeAdvance() ;
		this.logMessage("BatteryUserModel::timeAdvance() 1 " + d +
									" " + this.eventListAsString()) ;

		return d ;
	}
	
	@Override
	public Vector<EventI>	output()
	{
		assert	!this.eventList.isEmpty() ;
		Vector<EventI> ret = super.output() ;
		assert	ret.size() == 1 ;
		this.nextEvent = ret.get(0).getClass() ;

		this.logMessage("BatteryUserModel::output() " +
									this.nextEvent.getCanonicalName()) ;
		return ret ;
	}
	
	@Override
	public void				userDefinedInternalTransition(
		Duration elapsedTime
		)
	{

		Duration d;

		
		if (this.nextEvent.equals(UpdateBattery.class)) {
			d = new Duration(this.meanTimeBetweenBatteryUpdate, this.getSimulatedTimeUnit()) ;
			this.scheduleEvent(new UpdateBattery(this.getCurrentStateTime().add(d))) ;
		}
	}
	

}