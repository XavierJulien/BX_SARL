package simulation.events.battery;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.battery.BatteryModel;

public class Discharging extends AbstractEvent{

	private static final long serialVersionUID = 1L;
	private double out_energy;
	
	public Discharging(Time timeOfOccurrence, double energy)
	{
		super(timeOfOccurrence, null) ;
		this.out_energy = energy;
	}
	
	@Override
	public String eventAsString()
	{
		return "Test::Charge" ;
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

		((BatteryModel)model).consume(out_energy) ;
	}
}