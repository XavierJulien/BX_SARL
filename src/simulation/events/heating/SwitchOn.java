package simulation.events.heating;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.heating.HeatingModel;

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
		return "Heating::SwitchOn" ;
	}

	@Override
	public boolean			hasPriorityOver(EventI e)
	{
		return true;
	}

	@Override
	public void				executeOn(AtomicModel model)
	{
		System.out.println("Model");
		assert	model instanceof HeatingModel ;
		((HeatingModel)model).updateState(HeatingModel.State.ON) ;
		System.out.println("State : "+((HeatingModel)model).getState());
	}
}
//-----------------------------------------------------------------------------
