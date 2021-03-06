package simulation.events.battery;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.battery.BatteryModel;

/**
 * The class <code>UpdaterBattery</code> defines the event of the battery being
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
 * @author	Julien Xavier et Alexis Belanger
 */
public class UpdateBattery extends AbstractEvent{

	private static final long serialVersionUID = 1L;
	
	public UpdateBattery(Time timeOfOccurrence){
		super(timeOfOccurrence, null) ;
	}
	
	
	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#executeOn(fr.sorbonne_u.devs_simulation.models.AtomicModel)
	 */
	@Override
	public void executeOn(AtomicModel model){
		assert	model instanceof BatteryModel ;
		((BatteryModel)model).update();
	}
}
