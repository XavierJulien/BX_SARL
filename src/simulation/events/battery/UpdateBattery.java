package simulation.events.battery;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.battery.BatteryModel;

public class UpdateBattery extends AbstractEvent{

	private static final long serialVersionUID = 1L;
	
	public UpdateBattery(Time timeOfOccurrence){
		super(timeOfOccurrence, null) ;
	}
	
	
	/**
	 * this methode is used when a scheduled event is executed
	 */
	@Override
	public void executeOn(AtomicModel model){
		assert	model instanceof BatteryModel ;
		((BatteryModel)model).update();
	}
}
