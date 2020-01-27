package simulation.models.heating;


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
import simulation.events.heating.HeatingUpdater;

public class HeatingCoupledModel extends CoupledModel {
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L ;
	public static final String	URI = "HeatingCoupledModel" ;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public				HeatingCoupledModel(
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
			) throws Exception
	{
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
	 * @throws Exception	
	 */
	public static Architecture	build() throws Exception
	{
		Map<String,AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>() ;
		atomicModelDescriptors.put(
				HeatingModel.URI,
				AtomicHIOA_Descriptor.create(
						HeatingModel.class,
						HeatingModel.URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_ENGINE)) ;
		atomicModelDescriptors.put(
				HeatingUserModel.URI,
				AtomicModelDescriptor.create(
						HeatingUserModel.class,
						HeatingUserModel.URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_ENGINE)) ;
		atomicModelDescriptors.put(
				HeatingUpdaterModel.URI,
				AtomicModelDescriptor.create(
						HeatingUpdaterModel.class,
						HeatingUpdaterModel.URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_ENGINE)) ;
		Map<String,CoupledModelDescriptor> coupledModelDescriptors = new HashMap<String,CoupledModelDescriptor>() ;
		Set<String> submodels = new HashSet<String>() ;
		submodels.add(HeatingModel.URI) ;
		submodels.add(HeatingUserModel.URI) ;
		submodels.add(HeatingUpdaterModel.URI) ;
		Map<EventSource,EventSink[]> connections = new HashMap<EventSource,EventSink[]>() ;
		EventSource from5 = new EventSource(HeatingUserModel.URI, HeatingUpdater.class) ;
		EventSink[] to5 = new EventSink[] {new EventSink(HeatingModel.URI, HeatingUpdater.class)} ;
		connections.put(from5, to5) ;
		EventSource from6 = new EventSource(HeatingUpdaterModel.URI, HeatingUpdater.class) ;
		EventSink[] to6 = new EventSink[] {new EventSink(HeatingModel.URI, HeatingUpdater.class)} ;
		connections.put(from6, to6) ;
		coupledModelDescriptors.put(
				HeatingCoupledModel.URI,
				new CoupledHIOA_Descriptor(
						HeatingCoupledModel.class,
						HeatingCoupledModel.URI,
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
				HeatingCoupledModel.URI,
				atomicModelDescriptors,
				coupledModelDescriptors,
				TimeUnit.SECONDS);
	}
}