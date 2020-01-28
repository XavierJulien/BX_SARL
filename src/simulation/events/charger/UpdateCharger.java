package simulation.events.charger;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.charger.ChargerModel;

/**
 * The class <code>UpdaterCharger</code> defines the event of the charger being
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
public class UpdateCharger extends AbstractEvent{

	private static final long serialVersionUID = 1L;
	
	public UpdateCharger(Time timeOfOccurrence){
		super(timeOfOccurrence, null) ;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#executeOn(fr.sorbonne_u.devs_simulation.models.AtomicModel)
	 */
	@Override
	public void executeOn(AtomicModel model){
		assert	model instanceof ChargerModel ;

		((ChargerModel)model).update() ;
	}
}
