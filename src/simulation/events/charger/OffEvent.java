package simulation.events.charger;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.charger.ChargerModel;

public class OffEvent extends AbstractEvent{

	private static final long serialVersionUID = 1L;
	
	public OffEvent(Time timeOfOccurrence)
	{
		super(timeOfOccurrence, null) ;
	}
	
	@Override
	public String eventAsString()
	{
		return "Charger::OffEvent" ;
	}
	
	@Override
	public boolean hasPriorityOver(EventI e)
	{
		return true ;
	}
	
	@Override
	public void executeOn(AtomicModel model)
	{
		assert	model instanceof ChargerModel ;

		((ChargerModel)model).offCharging() ;
	}
}