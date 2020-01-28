package simulation.models.heating;

import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.random.RandomDataGenerator;

import fr.sorbonne_u.devs_simulation.es.models.AtomicES_Model;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;
import simulation.events.heating.UpdaterHeating;

@ModelExternalEvents(exported = { UpdaterHeating.class})

public class HeatingUserModel extends AtomicES_Model {
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long 				serialVersionUID = 1L;
	public static final String				URI = "HeatingUserModel";
	protected double						meanTimeBetweenTempUpdate;
	protected Class<?>						nextEvent;
	protected final RandomDataGenerator		rg;
	protected HeatingModel.State 			hs;
	
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------
	public				HeatingUserModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine) ;
		this.rg = new RandomDataGenerator() ;
		this.setLogger(new StandardLogger()) ;
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------
	@Override
	public void	initialiseState(Time initialTime) {
		this.meanTimeBetweenTempUpdate = 7.0;
		this.hs = HeatingModel.State.OFF ;
		this.rg.reSeedSecure() ;
		super.initialiseState(initialTime) ;
		Duration d1 = new Duration(
							this.meanTimeBetweenTempUpdate,
							this.getSimulatedTimeUnit()) ;
		Duration d2 =
			new Duration(
					2.0 * this.meanTimeBetweenTempUpdate *
											this.rg.nextBeta(1.75, 1.75),
					this.getSimulatedTimeUnit()) ;
		Time t = this.getCurrentStateTime().add(d1).add(d2) ;
		this.scheduleEvent(new UpdaterHeating(t)) ;
		this.nextTimeAdvance = this.timeAdvance() ;
		this.timeOfNextEvent = this.getCurrentStateTime().add(this.nextTimeAdvance) ;
	}

	@Override
	public Duration	timeAdvance(){
		Duration d = super.timeAdvance() ;
		return d ;
	}

	@Override
	public Vector<EventI> output(){
		assert	!this.eventList.isEmpty() ;
		Vector<EventI> ret = super.output() ;
		assert	ret.size() == 1 ;
		this.nextEvent = ret.get(0).getClass() ;
		return ret ;
	}

	@Override
	public void	userDefinedInternalTransition(Duration elapsedTime){
		Duration d ;
		if (this.nextEvent.equals(UpdaterHeating.class)) {
			d = new Duration(this.meanTimeBetweenTempUpdate, this.getSimulatedTimeUnit()) ;
			Time t = this.getCurrentStateTime().add(d);
			this.scheduleEvent(new UpdaterHeating(t)) ;
		}
	}
}