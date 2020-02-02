package simulation.events.kettle;

import java.util.Random;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.kettle.KettleModel;

/**
 * The class <code>FillKettle</code> defines the event of the kettle being
 * filled.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * 
 * @author	Julien Xavier et Alexis Belanger
 */
public class FillKettle extends AbstractEvent {
	
	private static final long serialVersionUID = 1L;

	public				FillKettle(Time timeOfOccurrence){
		super(timeOfOccurrence, null) ;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#executeOn(fr.sorbonne_u.devs_simulation.models.AtomicModel)
	 */
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
