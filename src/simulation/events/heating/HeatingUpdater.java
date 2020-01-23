package simulation.events.heating;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.heating.HeatingModel;

public class HeatingUpdater extends AbstractEvent {
	
	private static final long serialVersionUID = 1L;

	public HeatingUpdater(Time timeOfOccurrence){
		super(timeOfOccurrence, null) ;
	}
	
	
	/**
	 * The method executeOn is used when the event is processed
	 */
	@Override
	public void executeOn(AtomicModel model){
		assert	model instanceof HeatingModel ;

		((HeatingModel)model).updatePower();
		
	}
}
