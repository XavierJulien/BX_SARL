package simulation.events.kettle;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.kettle.KettleModel;

public class SwitchOn extends AbstractEvent{

	private static final long serialVersionUID = 1L ;

	public				SwitchOn(Time timeOfOccurrence){
		super(timeOfOccurrence, null) ;
	}

	@Override
	public void				executeOn(AtomicModel model){
		assert	model instanceof KettleModel ;

		((KettleModel)model).updateState(KettleModel.State.ON) ;
	}
}
