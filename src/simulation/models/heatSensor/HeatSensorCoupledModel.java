package simulation.models.heatSensor;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.hioa.architectures.AtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.architectures.CoupledHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.StaticVariableDescriptor;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSink;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSource;
import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import fr.sorbonne_u.devs_simulation.models.CoupledModel;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.events.ReexportedEvent;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import simulation.events.heatSensor.UpdaterHeatSensor;
import simulation.events.heatSensor.HeatSensorWindowOpen;
import simulation.events.heatSensor.HeatSensorWindowStillOpen;

/**
 * The class <code>HeatSensorCoupledModel</code> implements the DEVS simulation
 * coupled model for the heat sensor.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * 
 * @author	Julien Xavier & Alexis Belanger</a>
 */
public class HeatSensorCoupledModel extends CoupledModel {
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L ;
	public static final String	URI = "HeatingSensorCoupledModel" ;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------
	public				HeatSensorCoupledModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine,
		ModelDescriptionI[] submodels,
		Map<Class<? extends EventI>,EventSink[]> imported,
		Map<Class<? extends EventI>, ReexportedEvent> reexported,
		Map<EventSource, EventSink[]> connections,
		Map<StaticVariableDescriptor, VariableSink[]> importedVars,
		Map<VariableSource, StaticVariableDescriptor> reexportedVars,
		Map<VariableSource, VariableSink[]> bindings
		) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine, submodels,
			  imported, reexported, connections,
			  importedVars, reexportedVars, bindings);
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------
	/**
	 * build the simulation architecture corresponding to this coupled model.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @return				the simulation architecture corresponding to this coupled model.
	 * @throws Exception	<i>catch dans le bloc supérieur</i>
	 */
	public static Architecture build() throws Exception {
		Map<String,AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>() ;
		atomicModelDescriptors.put(
				HeatSensorModel.URI,
				AtomicHIOA_Descriptor.create(
						HeatSensorModel.class,
						HeatSensorModel.URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_ENGINE)) ;
		atomicModelDescriptors.put(
				HeatSensorUpdaterModel.URI,
				AtomicModelDescriptor.create(
						HeatSensorUpdaterModel.class,
						HeatSensorUpdaterModel.URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_ENGINE)) ;
		Map<String,CoupledModelDescriptor> coupledModelDescriptors = new HashMap<String,CoupledModelDescriptor>() ;
		Set<String> submodels = new HashSet<String>() ;
		submodels.add(HeatSensorModel.URI) ;
		submodels.add(HeatSensorUpdaterModel.URI) ;
		Map<EventSource,EventSink[]> connections = new HashMap<EventSource,EventSink[]>() ;
		EventSource from1 = new EventSource(HeatSensorUpdaterModel.URI, HeatSensorWindowOpen.class) ;
		EventSink[] to1 = new EventSink[] {new EventSink(HeatSensorModel.URI, HeatSensorWindowOpen.class)} ;
		connections.put(from1, to1) ;
		EventSource from2 = new EventSource(HeatSensorUpdaterModel.URI, UpdaterHeatSensor.class) ;
		EventSink[] to2 = new EventSink[] {new EventSink(HeatSensorModel.URI, UpdaterHeatSensor.class)} ;
		connections.put(from2, to2) ;
		EventSource from3 =	new EventSource(HeatSensorUpdaterModel.URI, HeatSensorWindowStillOpen.class) ;
		EventSink[] to3 = new EventSink[] {new EventSink(HeatSensorModel.URI, HeatSensorWindowStillOpen.class)} ;
		connections.put(from3, to3) ;
		coupledModelDescriptors.put(
					HeatSensorCoupledModel.URI,
					new CoupledHIOA_Descriptor(
							HeatSensorCoupledModel.class,
							HeatSensorCoupledModel.URI,
							submodels,
							null,
							null,
							connections,
							null,
							SimulationEngineCreationMode.COORDINATION_ENGINE,
							null,
							null,
							null)) ;
		return new Architecture(
						HeatSensorCoupledModel.URI,
						atomicModelDescriptors,
						coupledModelDescriptors,
						TimeUnit.SECONDS);
	}
}