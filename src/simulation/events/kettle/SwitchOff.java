package simulation.events.kettle;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.kettle.KettleModel;

public class SwitchOff extends AbstractEvent {
	
	private static final long serialVersionUID = 1L;

	public SwitchOff(Time timeOfOccurrence){
		super(timeOfOccurrence, null) ;
	}

	@Override
	public void	executeOn(AtomicModel model){
		assert	model instanceof KettleModel ;
		((KettleModel)model).updateState(KettleModel.State.OFF) ;
	}
}
