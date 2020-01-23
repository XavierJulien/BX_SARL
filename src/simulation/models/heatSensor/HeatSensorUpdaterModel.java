package simulation.models.heatSensor;

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
import simulation.events.heatSensor.HeatSensorUpdater;
import simulation.events.heatSensor.HeatSensorWindowOpen;
import simulation.events.heatSensor.HeatSensorWindowStillOpen;

@ModelExternalEvents(exported = { HeatSensorUpdater.class,
								  HeatSensorWindowOpen.class,
								  HeatSensorWindowStillOpen.class})

public class HeatSensorUpdaterModel extends AtomicES_Model {
	private static final long serialVersionUID = 1L ;
	public static final String	URI = "HeatSensorUpdaterModel" ;
	protected double	meanTimeBetweenHeatUpdate ;
	protected Class<?>	nextEvent ;
	protected final RandomDataGenerator		rg ;
	
	public HeatSensorUpdaterModel(
			String uri,
			TimeUnit simulatedTimeUnit,
			SimulatorI simulationEngine
			) throws Exception {
			super(uri, simulatedTimeUnit, simulationEngine) ;
			this.rg = new RandomDataGenerator() ;
			this.setLogger(new StandardLogger()) ;
		}
	
	@Override
	public void	initialiseState(Time initialTime) {
		this.meanTimeBetweenHeatUpdate = 7.0;
		this.rg.reSeedSecure() ;
		super.initialiseState(initialTime) ;
		Duration d1 = new Duration(meanTimeBetweenHeatUpdate,this.getSimulatedTimeUnit()) ;
		Duration d2 = new Duration(meanTimeBetweenHeatUpdate,this.getSimulatedTimeUnit()) ;
		Time t = this.getCurrentStateTime().add(d1).add(d2) ;
		this.scheduleEvent(new HeatSensorUpdater(t)) ;
		this.nextTimeAdvance = this.timeAdvance() ;
		this.timeOfNextEvent =this.getCurrentStateTime().add(this.nextTimeAdvance) ;
		try {
			this.setDebugLevel(1) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
	
	@Override
	public Duration timeAdvance(){
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
		if (this.nextEvent.equals(HeatSensorUpdater.class)) {
			d = new Duration(meanTimeBetweenHeatUpdate, this.getSimulatedTimeUnit()) ;
			Time t = this.getCurrentStateTime().add(d) ;
			if(Math.random()>0.95) {
				this.scheduleEvent(new HeatSensorWindowOpen(t)) ;
			}else {
				this.scheduleEvent(new HeatSensorUpdater(t)) ;	
			}
		}else if (this.nextEvent.equals(HeatSensorWindowOpen.class)) {
			d = new Duration(meanTimeBetweenHeatUpdate, this.getSimulatedTimeUnit()) ;
			Time t = this.getCurrentStateTime().add(d) ;
			this.scheduleEvent(new HeatSensorWindowStillOpen(t)) ;
		}else if (this.nextEvent.equals(HeatSensorWindowStillOpen.class)) {
			d = new Duration(meanTimeBetweenHeatUpdate, this.getSimulatedTimeUnit()) ;
			Time t = this.getCurrentStateTime().add(d) ;
			if(Math.random()<0.1) {
				this.scheduleEvent(new HeatSensorWindowStillOpen(t)) ;
			}else {
				this.scheduleEvent(new HeatSensorUpdater(t)) ;	
			}
		}
	}
}