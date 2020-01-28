package simulation.events.heating;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.heating.HeatingModel;

/**
 * The class <code>UpdaterHeating</code> defines the event of the heating being
 * updated.
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
public class UpdaterHeating extends AbstractEvent {
	
	private static final long serialVersionUID = 1L;

	public UpdaterHeating(Time timeOfOccurrence){
		super(timeOfOccurrence, null) ;
	}
	
	
	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#executeOn(fr.sorbonne_u.devs_simulation.models.AtomicModel)
	 */
	@Override
	public void executeOn(AtomicModel model){
		assert	model instanceof HeatingModel ;

		((HeatingModel)model).updatePower();
		
	}
}
