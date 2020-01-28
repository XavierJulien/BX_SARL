package simulation.events.windSensor;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.windSensor.WindSensorModel;

/**
 * The class <code>UpdaterWindSensor</code> defines the event of the wind sensor being
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
public class UpdaterWindSensor extends AbstractEvent {
	
	private static final long serialVersionUID = 1L;
	
	public UpdaterWindSensor(Time timeOfOccurrence){
		super(timeOfOccurrence, null) ;
	}
	
	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#executeOn(fr.sorbonne_u.devs_simulation.models.AtomicModel)
	 */
	@Override
	public void executeOn(AtomicModel model){
		assert	model instanceof WindSensorModel ;

		((WindSensorModel)model).updateWind() ;
	}
}
