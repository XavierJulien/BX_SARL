package simulation.events.electricMeter;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import simulation.events.AbstractEvent;
import simulation.models.electricMeter.ElectricMeterModel;
import simulation.models.windturbine.WindTurbineModel;

public class TotalConsumptionUpdater extends AbstractEvent {
	
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public TotalConsumptionUpdater(Time timeOfOccurrence)
	{
		super(timeOfOccurrence, null) ;
	}
	
	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	@Override
	public String eventAsString()
	{
		return "ElectricMeter Consumption::Updater" ;
	}
	
	@Override
	public boolean hasPriorityOver(EventI e)
	{
		return false;
	}
	
	@Override
	public void executeOn(AtomicModel model)
	{
		assert	model instanceof ElectricMeterModel ;

		((ElectricMeterModel)model).updateConsumption();
	}
}
