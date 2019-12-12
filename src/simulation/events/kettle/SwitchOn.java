package simulation.events.kettle;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.kettle.KettleModel;

public class			SwitchOn
extends		AbstractEvent
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L ;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public				SwitchOn(Time timeOfOccurrence)
	{
		super(timeOfOccurrence, null) ;
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	@Override
	public String			eventAsString()
	{
		return "Kettle::SwitchOn" ;
	}

	@Override
	public boolean			hasPriorityOver(EventI e)
	{
		if (e instanceof FillKettle || e instanceof EmptyKettle) {
			return false ;
		} else {
			return true ;
		}
	}

	@Override
	public void				executeOn(AtomicModel model)
	{
		assert	model instanceof KettleModel ;

		((KettleModel)model).updateState(KettleModel.State.ON) ;
	}
}
//-----------------------------------------------------------------------------
