package simulation.events.battery;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.battery.BatteryModel;

public class UpdateBatteryCharge extends AbstractEvent{

	private static final long serialVersionUID = 1L;
	
	public UpdateBatteryCharge(Time timeOfOccurrence)
	{
		super(timeOfOccurrence, null) ;
	}
	
	@Override
	public String eventAsString()
	{
		return "Battery::Charging" ;
	}
	
	@Override
	public boolean hasPriorityOver(EventI e)
	{
		return true ;
	}
	
	@Override
	public void executeOn(AtomicModel model)
	{
		assert	model instanceof BatteryModel ;
		((BatteryModel)model).update();
	}
}
