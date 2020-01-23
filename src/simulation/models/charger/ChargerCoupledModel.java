package simulation.models.charger;

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
import simulation.events.charger.UpdateCharger;

public class ChargerCoupledModel extends CoupledModel {
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------
	private static final long serialVersionUID = 1L ;
	public static final String	URI = "ChargerCoupledModel" ;
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------
	public ChargerCoupledModel(
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
	
	public static Architecture build() throws Exception {
		Map<String,AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>() ;
		atomicModelDescriptors.put(
				ChargerModel.URI,
				AtomicHIOA_Descriptor.create(
						ChargerModel.class,
						ChargerModel.URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_ENGINE)) ;
		atomicModelDescriptors.put(
				ChargerUserModel.URI,
				AtomicModelDescriptor.create(
						ChargerUserModel.class,
						ChargerUserModel.URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_ENGINE)) ;
		Map<String,CoupledModelDescriptor> coupledModelDescriptors = new HashMap<String,CoupledModelDescriptor>() ;
		Set<String> submodels = new HashSet<String>() ;
		submodels.add(ChargerModel.URI) ;
		submodels.add(ChargerUserModel.URI) ;
		Map<EventSource,EventSink[]> connections = new HashMap<EventSource,EventSink[]>() ;
		EventSource from1 = new EventSource(ChargerUserModel.URI, UpdateCharger.class) ;
		EventSink[] to1 = new EventSink[] {new EventSink(ChargerModel.URI, UpdateCharger.class)} ;
		connections.put(from1, to1) ;
		coupledModelDescriptors.put(
				ChargerCoupledModel.URI,
					new CoupledHIOA_Descriptor(
							ChargerCoupledModel.class,
							ChargerCoupledModel.URI,
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
						ChargerCoupledModel.URI,
						atomicModelDescriptors,
						coupledModelDescriptors,
						TimeUnit.SECONDS);
	}
}