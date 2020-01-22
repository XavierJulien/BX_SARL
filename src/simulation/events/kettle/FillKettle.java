package simulation.events.kettle;

import java.util.Random;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.kettle.KettleModel;

public class FillKettle extends AbstractEvent {
	
	private static final long serialVersionUID = 1L;

	public				FillKettle(Time timeOfOccurrence){
		super(timeOfOccurrence, null) ;
	}

	@Override
	public void				executeOn(AtomicModel model){
		assert	model instanceof KettleModel ;
		if(new Random().nextBoolean()) {
			((KettleModel)model).updateContent(KettleModel.Content.FULL);			
		}else {
			((KettleModel)model).updateContent(KettleModel.Content.HALF);
		}
	}
}
