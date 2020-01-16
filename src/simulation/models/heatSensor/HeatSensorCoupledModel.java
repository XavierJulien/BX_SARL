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
import simulation.events.heatSensor.HeatSensorUpdater;
import simulation.events.heatSensor.HeatSensorWindowOpen;
import simulation.events.heatSensor.HeatSensorWindowStillOpen;

public class HeatSensorCoupledModel extends CoupledModel {
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L ;
	/** URI of the unique instance of this class (in this example).			*/
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


		Map<String,CoupledModelDescriptor> coupledModelDescriptors =
				new HashMap<String,CoupledModelDescriptor>() ;

		Set<String> submodels = new HashSet<String>() ;
		submodels.add(HeatSensorModel.URI) ;
		submodels.add(HeatSensorUpdaterModel.URI) ;

		Map<EventSource,EventSink[]> connections = new HashMap<EventSource,EventSink[]>() ;
		
		EventSource from1 =
				new EventSource(HeatSensorUpdaterModel.URI, HeatSensorWindowOpen.class) ;
		EventSink[] to1 = new EventSink[] {
				new EventSink(HeatSensorModel.URI, HeatSensorWindowOpen.class)} ;
		connections.put(from1, to1) ;
		
		EventSource from2 =
				new EventSource(HeatSensorUpdaterModel.URI, HeatSensorUpdater.class) ;
		EventSink[] to2 = new EventSink[] {
				new EventSink(HeatSensorModel.URI, HeatSensorUpdater.class)} ;
		connections.put(from2, to2) ;
		
		EventSource from3 =
				new EventSource(HeatSensorUpdaterModel.URI, HeatSensorWindowStillOpen.class) ;
		EventSink[] to3 = new EventSink[] {
				new EventSink(HeatSensorModel.URI, HeatSensorWindowStillOpen.class)} ;
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
