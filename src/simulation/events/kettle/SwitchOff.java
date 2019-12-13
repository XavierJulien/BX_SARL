package simulation.events.kettle;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.kettle.KettleModel;

public class SwitchOff extends AbstractEvent {
	
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------
	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public				SwitchOff(Time timeOfOccurrence)
	{
		super(timeOfOccurrence, null) ;
	}

	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	@Override
	public String		eventAsString()
	{
		return "Kettle::SwitchOff" ;
	}


	@Override
	public boolean			hasPriorityOver(EventI e)
	{
		return true ;
	}

	@Override
	public void				executeOn(AtomicModel model)
	{
		assert	model instanceof KettleModel ;

		((KettleModel)model).updateState(KettleModel.State.OFF) ;
	}
}
