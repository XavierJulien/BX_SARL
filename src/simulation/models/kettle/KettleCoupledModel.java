package simulation.models.kettle;


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
import fr.sorbonne_u.devs_simulation.interfaces.SimulationReportI;
import fr.sorbonne_u.devs_simulation.models.CoupledModel;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.events.ReexportedEvent;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardCoupledModelReport;
import simulation.events.kettle.EmptyKettle;
import simulation.events.kettle.FillKettle;
import simulation.events.kettle.KettleUpdater;
import simulation.events.kettle.SwitchOff;
import simulation.events.kettle.SwitchOn;

public class KettleCoupledModel extends CoupledModel {
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L ;
	/** URI of the unique instance of this class (in this example).			*/
	public static final String	URI = "KettleCoupledModel" ;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public				KettleCoupledModel(
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

	public static Architecture	build() throws Exception
	{
		Map<String,AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>() ;

		atomicModelDescriptors.put(
				KettleModel.URI,
				AtomicHIOA_Descriptor.create(
						KettleModel.class,
						KettleModel.URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_ENGINE)) ;
		atomicModelDescriptors.put(
				KettleUserModel.URI,
				AtomicModelDescriptor.create(
						KettleUserModel.class,
						KettleUserModel.URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_ENGINE)) ;
		
		atomicModelDescriptors.put(
				KettleUpdaterModel.URI,
				AtomicModelDescriptor.create(
						KettleUpdaterModel.class,
						KettleUpdaterModel.URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_ENGINE)) ;

		Map<String,CoupledModelDescriptor> coupledModelDescriptors =
				new HashMap<String,CoupledModelDescriptor>() ;

		Set<String> submodels = new HashSet<String>() ;
		submodels.add(KettleModel.URI) ;
		submodels.add(KettleUserModel.URI) ;
		submodels.add(KettleUpdaterModel.URI) ;

		Map<EventSource,EventSink[]> connections = new HashMap<EventSource,EventSink[]>() ;
		EventSource from1 =
				new EventSource(KettleUserModel.URI, SwitchOn.class) ;
		EventSink[] to1 =
				new EventSink[] {
						new EventSink(KettleModel.URI, SwitchOn.class)} ;
		connections.put(from1, to1) ;
		EventSource from2 =
				new EventSource(KettleUserModel.URI, SwitchOff.class) ;
		EventSink[] to2 = new EventSink[] {
				new EventSink(KettleModel.URI, SwitchOff.class)} ;
		connections.put(from2, to2) ;
		EventSource from3 =
				new EventSource(KettleUserModel.URI, EmptyKettle.class) ;
		EventSink[] to3 = new EventSink[] {
				new EventSink(KettleModel.URI, EmptyKettle.class)} ;
		connections.put(from3, to3) ;
		EventSource from4 =
				new EventSource(KettleUserModel.URI, FillKettle.class) ;
		EventSink[] to4 = new EventSink[] {
				new EventSink(KettleModel.URI, FillKettle.class)} ;
		connections.put(from4, to4) ;
		EventSource from5 =
				new EventSource(KettleUserModel.URI, KettleUpdater.class) ;
		EventSink[] to5 = new EventSink[] {
				new EventSink(KettleModel.URI, KettleUpdater.class)} ;
		connections.put(from5, to5) ;
		EventSource from6 =
				new EventSource(KettleUpdaterModel.URI, KettleUpdater.class) ;
		EventSink[] to6 = new EventSink[] {
				new EventSink(KettleModel.URI, KettleUpdater.class)} ;
		connections.put(from6, to6) ;
		
		coupledModelDescriptors.put(
					KettleCoupledModel.URI,
					new CoupledHIOA_Descriptor(
							KettleCoupledModel.class,
							KettleCoupledModel.URI,
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
						KettleCoupledModel.URI,
						atomicModelDescriptors,
						coupledModelDescriptors,
						TimeUnit.SECONDS);
	}

	@Override
	public SimulationReportI	getFinalReport() throws Exception
	{
		StandardCoupledModelReport ret =
							new StandardCoupledModelReport(this.getURI()) ;
		for (int i = 0 ; i < this.submodels.length ; i++) {
			ret.addReport(this.submodels[i].getFinalReport()) ;
		}
		return ret ;
	}
}
//-----------------------------------------------------------------------------
