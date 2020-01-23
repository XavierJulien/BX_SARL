package simulation.models.electricMeter;

import java.util.Vector;
import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.devs_simulation.es.models.AtomicES_Model;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;
import simulation.events.electricMeter.ElectricMeterUpdater;

@ModelExternalEvents(exported = {ElectricMeterUpdater.class})
public class ElectricMeterUpdaterModel extends AtomicES_Model {
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------
	private static final long serialVersionUID = 1L ;
	public static final String	URI = "ElectricMeterUpdaterModel" ;
	protected double	initialDelay ;
	protected double	interdayDelay ;
	protected double	meanTimeBetweenConsumptionUpdate ;
	protected Class<?>	nextEvent ;
	
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------
	public ElectricMeterUpdaterModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine) ;
		this.setLogger(new StandardLogger()) ;
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	@Override
	public void	initialiseState(Time initialTime) {
		this.initialDelay = 10.0 ;
		this.interdayDelay = 100.0 ;
		super.initialiseState(initialTime) ;
		Duration d1 = new Duration(
							this.initialDelay,
							this.getSimulatedTimeUnit()) ;
		Duration d2 =
				new Duration(1, TimeUnit.SECONDS) ;
		Time t = this.getCurrentStateTime().add(d1).add(d2) ;
		this.scheduleEvent(new ElectricMeterUpdater(t)) ;
		this.nextTimeAdvance = this.timeAdvance() ;
		this.timeOfNextEvent = this.getCurrentStateTime().add(this.nextTimeAdvance) ;
	}

	@Override
	public Duration	timeAdvance(){
		Duration d = super.timeAdvance() ;
		return d ;
	}

	@Override
	public Vector<EventI> output() {
		assert	!this.eventList.isEmpty() ;
		Vector<EventI> ret = super.output() ;
		assert	ret.size() == 1 ;
		this.nextEvent = ret.get(0).getClass() ;
		return ret ;
	}

	@Override
	public void	userDefinedInternalTransition(Duration elapsedTime){	
		Duration d ;
		if (this.nextEvent.equals(ElectricMeterUpdater.class)) {
			d = new Duration(1,
							 this.getSimulatedTimeUnit()) ;
			Time t = this.getCurrentStateTime().add(d) ;
			this.scheduleEvent(new ElectricMeterUpdater(t)) ;
		}
	}
}