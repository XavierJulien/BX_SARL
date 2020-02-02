package simulation.events.heatSensor;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.heatSensor.HeatSensorModel;

/**
 * The class <code>HeatSensorWindowOpen</code> defines the event of the window being
 * open.
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
public class HeatSensorWindowOpen extends AbstractEvent {
	
	private static final long serialVersionUID = 1L;
	
	public HeatSensorWindowOpen(Time timeOfOccurrence){
		super(timeOfOccurrence, null) ;
	}
	
	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#executeOn(fr.sorbonne_u.devs_simulation.models.AtomicModel)
	 */
	@Override
	public void executeOn(AtomicModel model){
		assert	model instanceof HeatSensorModel ;

		((HeatSensorModel)model).openWindow() ;
	}
}
