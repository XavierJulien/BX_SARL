package components;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.cyphy.plugins.devs.SupervisorPlugin;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.examples.molene.SimulationMain;
import fr.sorbonne_u.devs_simulation.examples.molene.pcm.PortableComputerModel;
import fr.sorbonne_u.devs_simulation.examples.molene.pcsm.PortableComputerStateModel;
import fr.sorbonne_u.devs_simulation.hioa.architectures.AtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.utils.PlotterDescription;
import interfaces.sensors.WindSensorI;
import ports.sensors.WindSensorInboundPort;
import ports.sensors.WindSensorOutboundPort;
import simulation.models.sensors.WindSpeedModel;

@RequiredInterfaces(required = {WindSensorI.class})
@OfferedInterfaces(offered = {WindSensorI.class})
public class WindSensor extends AbstractCyPhyComponent {

	
	protected SupervisorPlugin		sp ;
	
	protected final String				uri ;
	/** The inbound port URI for the eolienne.*/
	protected final String				windSensorInboundPortURI ;
	protected final String				windSensorOutboundPortURI ;

	protected final WindSensorInboundPort windSensorInboundPort;
	protected final WindSensorOutboundPort windSensorOutboundPort;

	protected double power = 0;



	protected WindSensor(String uri, String windSensorInboundPortURI, String windSensorOutboundPortURI) throws Exception{

		super(uri, 1, 1);
		this.uri = uri;
		this.windSensorInboundPortURI = windSensorInboundPortURI;
		this.windSensorOutboundPortURI = windSensorOutboundPortURI;

		windSensorInboundPort = new WindSensorInboundPort(windSensorInboundPortURI, this) ;
		windSensorInboundPort.publishPort() ;
		
		windSensorOutboundPort = new WindSensorOutboundPort(windSensorOutboundPortURI, this) ;
		windSensorOutboundPort.localPublishPort() ;

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		this.tracer.setTitle("WindSensor") ;
		this.tracer.setRelativePosition(2, 0) ;
	}

	public void sendWindSpeed() throws Exception {
		this.logMessage("Sending wind power....") ;
		power+=0.2;
		power = Math.abs(Math.sin(power));
		this.windSensorOutboundPort.sendWindSpeed(power) ;
		//		return 0.3;

	}

	@Override
	public void			start() throws ComponentStartException
	{
		super.start() ;
		this.logMessage("starting WindSensor component.") ;
		
	}
	@Override
	public void execute() throws Exception {
super.execute();
		
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							while(true) {
								
									
									((WindSensor)this.getTaskOwner()).sendWindSpeed();
								
								
								Thread.sleep(1000);
							}
							

						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				},
				1000, TimeUnit.MILLISECONDS);
		
		
	}
	
	// ------------------------------------------------------------------------
	// MODELISATION
	// ------------------------------------------------------------------------

	
	
	protected Architecture	createLocalArchitecture(String architectureURI)
	throws Exception
	{
		Map<String,AbstractAtomicModelDescriptor> atomicModelDescriptors =
				new HashMap<>() ;

		atomicModelDescriptors.put(
				PortableComputerStateModel.URI,
				AtomicHIOA_Descriptor.create(
						WindSpeedModel.class,
						WindSpeedModel.URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_ENGINE)) ;


		
		Architecture localArchitecture =
				new Architecture(
						PortableComputerModel.URI,
						atomicModelDescriptors,
						new HashMap<>(),
						TimeUnit.SECONDS) ;

		return localArchitecture ;
	}

	
	
	
	
	
	

	// ------------------------------------------------------------------------
	// FINALISE / SHUTDOWN
	// ------------------------------------------------------------------------


	@Override
	public void finalise() throws Exception {
		super.finalise();
	}

	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			windSensorInboundPort.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.shutdown();
	}
}
