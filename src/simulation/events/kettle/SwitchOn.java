package simulation.events.kettle;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.kettle.KettleModel;

/**
 * The class <code>SwitchOn</code> defines the event of the kettle being
 * switch on.
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
 * @author	Julien Xavier & Alexis Belanger</a>
 */
public class SwitchOn extends AbstractEvent{

	private static final long serialVersionUID = 1L ;

	public				SwitchOn(Time timeOfOccurrence){
		super(timeOfOccurrence, null) ;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#executeOn(fr.sorbonne_u.devs_simulation.models.AtomicModel)
	 */
	@Override
	public void				executeOn(AtomicModel model){
		assert	model instanceof KettleModel ;
		((KettleModel)model).updateState(KettleModel.State.ON) ;
	}
}
