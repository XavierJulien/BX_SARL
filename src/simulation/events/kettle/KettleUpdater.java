package simulation.events.kettle;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.kettle.KettleModel;

public class KettleUpdater extends AbstractEvent {
	
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public KettleUpdater(Time timeOfOccurrence)
	{
		super(timeOfOccurrence, null) ;
	}
	
	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	@Override
	public String eventAsString()
	{
		return "Kettle::Updater" ;
	}
	
	@Override
	public boolean hasPriorityOver(EventI e)
	{
		return false;
	}
	
	@Override
	public void executeOn(AtomicModel model)
	{
		assert	model instanceof KettleModel ;

		((KettleModel)model).updateTemperature() ;
		((KettleModel)model).updateState();
		((KettleModel)model).updateContent();
	}
}