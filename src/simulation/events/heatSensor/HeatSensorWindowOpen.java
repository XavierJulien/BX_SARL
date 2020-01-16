package simulation.events.heatSensor;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.heatSensor.HeatSensorModel;

public class HeatSensorWindowOpen extends AbstractEvent {
	
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public HeatSensorWindowOpen(Time timeOfOccurrence)
	{
		super(timeOfOccurrence, null) ;
	}
	
	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	@Override
	public String eventAsString()
	{
		return "HeatSensor::Updater" ;
	}
	
	@Override
	public boolean hasPriorityOver(EventI e)
	{
		return false;
	}
	
	@Override
	public void executeOn(AtomicModel model)
	{
		assert	model instanceof HeatSensorModel ;

		((HeatSensorModel)model).openWindow() ;
	}
}
