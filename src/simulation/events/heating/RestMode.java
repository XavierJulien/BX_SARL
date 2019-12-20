package simulation.events.heating;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.heating.HeatingModel;

public class RestMode extends AbstractEvent {
	
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------
	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------
	
	public				RestMode(Time timeOfOccurrence)
	{
		super(timeOfOccurrence, null) ;
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	@Override
	public String			eventAsString()
	{
		return "Heating::RestMode" ;
	}
	
	
	@Override
	public boolean			hasPriorityOver(EventI e)
	{
		if (e instanceof SwitchOff || e instanceof HeatingMode) {
			return false ;
		} else {
			return true ;
		}
	}
	
	@Override
	public void				executeOn(AtomicModel model)
	{
		assert	model instanceof HeatingModel ;
		((HeatingModel)model).updateMode(HeatingModel.Mode.REST);	
	}
}
//-----------------------------------------------------------------------------
