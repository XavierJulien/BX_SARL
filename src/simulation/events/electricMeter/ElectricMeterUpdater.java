package simulation.events.electricMeter;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.electricMeter.ElectricMeterModel;

public class ElectricMeterUpdater extends AbstractEvent {
	
	private static final long serialVersionUID = 1L;
	
	public ElectricMeterUpdater(Time timeOfOccurrence){
		super(timeOfOccurrence, null) ;
	}
	
	@Override
	public void executeOn(AtomicModel model){
		assert	model instanceof ElectricMeterModel ;

		((ElectricMeterModel)model).updateConsumption();
	}
}
