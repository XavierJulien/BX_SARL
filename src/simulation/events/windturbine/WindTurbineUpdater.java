package simulation.events.windturbine;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.windturbine.WindTurbineModel;

public class WindTurbineUpdater extends AbstractEvent {
	
	private static final long serialVersionUID = 1L;
	
	public WindTurbineUpdater(Time timeOfOccurrence){
		super(timeOfOccurrence, null) ;
	}
	
	@Override
	public void executeOn(AtomicModel model){
		assert	model instanceof WindTurbineModel ;

		((WindTurbineModel)model).updateProduction();
	}
}
