package simulation.events.heatSensor;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.heatSensor.HeatSensorModel;

public class HeatSensorUpdater extends AbstractEvent {
	
	private static final long serialVersionUID = 1L;
	
	public HeatSensorUpdater(Time timeOfOccurrence){
		super(timeOfOccurrence, null) ;
	}
	
	@Override
	public void executeOn(AtomicModel model){
		assert	model instanceof HeatSensorModel ;

		((HeatSensorModel)model).updateTemperature() ;
	}
}
