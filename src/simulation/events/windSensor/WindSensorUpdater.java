package simulation.events.windSensor;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.windSensor.WindSensorModel;

public class WindSensorUpdater extends AbstractEvent {
	
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public WindSensorUpdater(Time timeOfOccurrence)
	{
		super(timeOfOccurrence, null) ;
	}
	
	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	@Override
	public String eventAsString()
	{
		return "WindSensor::Updater" ;
	}
	
	@Override
	public boolean hasPriorityOver(EventI e)
	{
		return false;
	}
	
	@Override
	public void executeOn(AtomicModel model)
	{
		assert	model instanceof WindSensorModel ;

		((WindSensorModel)model).updateWind() ;
	}
}
