package simulation.models.charger;

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
import simulation.events.charger.UpdateCharger;

@ModelExternalEvents(exported = { UpdateCharger.class})

public class ChargerUserModel extends AtomicES_Model{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------
	private static final long serialVersionUID = 1L ;
	public static final String	URI = "ChargerUserModel" ;
	
	protected double	meanTimeBetweenChargerUpdate ;
	
	protected Class<?>	nextEvent ;
	protected double chargingCapacity;	
	
	protected final RandomDataGenerator		rg ;
	protected ChargerModel.Mode cm;
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------
	public ChargerUserModel(String uri,TimeUnit simulatedTimeUnit,SimulatorI simulationEngine) throws Exception {
			super(uri, simulatedTimeUnit, simulationEngine);
			this.rg = new RandomDataGenerator();
			this.setLogger(new StandardLogger());
		}
	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------
	
	@Override
	public void	initialiseState(Time initialTime) {
		
		this.meanTimeBetweenChargerUpdate = 7.0;
		this.cm = ChargerModel.Mode.OFF;
		this.chargingCapacity = 205.0;
		super.initialiseState(initialTime) ;

		Duration d1 = new Duration(this.meanTimeBetweenChargerUpdate,this.getSimulatedTimeUnit()) ;
		
		Time t = this.getCurrentStateTime().add(d1);
		this.scheduleEvent(new UpdateCharger(t)) ;
		this.nextTimeAdvance = this.timeAdvance() ;
		this.timeOfNextEvent = this.getCurrentStateTime().add(d1) ;
	}
	
	@Override
	public Duration timeAdvance() {
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
		Duration d;
		if (this.nextEvent.equals(UpdateCharger.class)) {
			d = new Duration(this.meanTimeBetweenChargerUpdate, this.getSimulatedTimeUnit()) ;
			this.scheduleEvent(new UpdateCharger(this.getCurrentStateTime().add(d))) ;
		}
	}
}