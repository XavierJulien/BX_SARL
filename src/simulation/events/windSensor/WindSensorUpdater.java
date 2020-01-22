package simulation.events.windSensor;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.windSensor.WindSensorModel;

public class WindSensorUpdater extends AbstractEvent {
	
	private static final long serialVersionUID = 1L;
	
	public WindSensorUpdater(Time timeOfOccurrence){
		super(timeOfOccurrence, null) ;
	}
	
	@Override
	public void executeOn(AtomicModel model){
		assert	model instanceof WindSensorModel ;

		((WindSensorModel)model).updateWind() ;
	}
}
