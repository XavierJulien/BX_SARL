package simulation.events.heating;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.heating.HeatingModel;

public class HeatingUpdater extends AbstractEvent {
	
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public HeatingUpdater(Time timeOfOccurrence)
	{
		super(timeOfOccurrence, null) ;
	}
	
	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	@Override
	public String eventAsString()
	{
		return "Heating::Updater" ;
	}
	
	@Override
	public boolean hasPriorityOver(EventI e)
	{
		return false;
	}
	
	@Override
	public void executeOn(AtomicModel model)
	{
		assert	model instanceof HeatingModel ;

		((HeatingModel)model).updateTemperature() ;
		((HeatingModel)model).updateMode();
	}
}
