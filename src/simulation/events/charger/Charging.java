package simulation.events.charger;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.charger.ChargerModel;

public class Charging extends AbstractEvent{

	private static final long serialVersionUID = 1L;
	private double in_energy;
	
	public Charging(Time timeOfOccurrence, double in_energy)
	{
		super(timeOfOccurrence, null) ;
		this.in_energy = in_energy;
	}
	
	@Override
	public String eventAsString()
	{
		return "Charger::Charging" ;
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

		((ChargerModel)model).charging(in_energy) ;
	}
}
