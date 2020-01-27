package bcm.components;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import bcm.interfaces.charger.ChargerBatteryI;
import bcm.interfaces.charger.ChargerElectricMeterI;
import bcm.interfaces.charger.ChargerI;
import bcm.ports.charger.ChargerInboundPort;
import bcm.ports.charger.ChargerOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.cyphy.interfaces.EmbeddingComponentStateAccessI;
import fr.sorbonne_u.components.cyphy.plugins.devs.SupervisorPlugin;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.simulators.SimulationEngine;
import simulation.models.charger.ChargerCoupledModel;
import simulation.simulatorplugins.ChargerSimulatorPlugin;

@RequiredInterfaces(required = {ChargerI.class, ChargerElectricMeterI.class, ChargerBatteryI.class})
@OfferedInterfaces(offered = {ChargerI.class, ChargerElectricMeterI.class, ChargerBatteryI.class})
public class Charger 
extends AbstractCyPhyComponent 
implements	EmbeddingComponentStateAccessI{

	protected SupervisorPlugin				sp ;
	protected final String					uri ;
	protected final String					chargerInboundPortURI ;	
	protected final String					chargerOutboundPortURI ;
	protected final String					chargerBatteryOutboundPortURI ;	
	protected final String					chargerElectricMeterOutboundPortURI ;
	protected final String					chargerElectricMeterInboundPortURI ;
	protected ChargerOutboundPort			chargerOutboundPort ;
	protected ChargerInboundPort			chargerInboundPort ;
	protected ChargerOutboundPort			chargerBatteryOutboundPort ;
	protected ChargerOutboundPort			chargerElectricMeterOutboundPort ;
	protected ChargerInboundPort			chargerElectricMeterInboundPort ;
	
	protected ChargerSimulatorPlugin		asp ;
	
	
	protected boolean 						isOn;
	protected final double 					conso;
	protected final String					chargerRef;
	
	
//------------------------------------------------------------------------
//----------------------------CONSTRUCTOR---------------------------------
//------------------------------------------------------------------------
	protected Charger(String uri,
					   String chargerOutboundPortURI,
					   String chargerInboundPortURI,
					   String chargerElectricMeterOutboundPortURI,
					   String chargerElectricMeterInboundPortURI,
					   String chargerBatteryOutboundPortURI,
					   double conso,
					   String chargerRef) throws Exception{
		super(uri, 2, 2);

		assert uri != null;
		assert chargerOutboundPortURI != null;
		assert chargerInboundPortURI != null;
		assert chargerBatteryOutboundPortURI != null;
		assert conso > 0;
		assert chargerRef != null;

		this.uri = uri;
		this.chargerInboundPortURI = chargerInboundPortURI;
		this.chargerOutboundPortURI = chargerOutboundPortURI;
		this.chargerBatteryOutboundPortURI = chargerBatteryOutboundPortURI;
		this.chargerElectricMeterOutboundPortURI = chargerElectricMeterOutboundPortURI;
		this.chargerElectricMeterInboundPortURI = chargerElectricMeterInboundPortURI;

		//-------------------PUBLISH-------------------
		chargerInboundPort = new ChargerInboundPort(chargerInboundPortURI, this);
		chargerInboundPort.publishPort();
		chargerElectricMeterInboundPort = new ChargerInboundPort(chargerElectricMeterInboundPortURI, this);
		chargerElectricMeterInboundPort.publishPort();
		chargerBatteryOutboundPort = new ChargerOutboundPort(chargerBatteryOutboundPortURI, this);
		chargerBatteryOutboundPort.publishPort();
		chargerOutboundPort = new ChargerOutboundPort(chargerOutboundPortURI, this);
		chargerOutboundPort.localPublishPort();
		chargerElectricMeterOutboundPort =	new ChargerOutboundPort(chargerElectricMeterOutboundPortURI, this);
		chargerElectricMeterOutboundPort.localPublishPort();

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		//-------------------GUI-------------------
		this.tracer.setTitle(uri) ;
		this.tracer.setRelativePosition(2, 3) ;
		
		//----------------Variables----------------
		this.isOn = false;
		this.conso = conso;
		this.chargerRef = chargerRef;
		//----------------Modelisation-------------
		
		this.initialise();
		
	}

		
	//------------------------------------------------------------------------
	//----------------------------SERVICES------------------------------------
	//------------------------------------------------------------------------
	
	/**
	 * turns the charger on
	 * @throws Exception	<i>catch dans le bloc supérieur</i>
	 */
	public void startCharger() throws Exception{
		this.logMessage("The charger is starting his job....") ;
		isOn = true;
	}

	/**
	 * turns the charger off
	 * @throws Exception	<i>catch dans le bloc supérieur</i>
	 */
	public void stopCharger() throws Exception{
		this.logMessage("The charger is stopping his job....") ;
		isOn =false;
	}
	
	/**
	 * used to inform the electric meter of the charger consumption
	 * @throws Exception	<i>catch dans le bloc supérieur</i>
	 */
	public void sendConsumption() throws Exception {
		this.logMessage("Sending consumption....") ;
		if(isOn) {
			this.chargerElectricMeterOutboundPort.sendConsumption(conso) ;
		}else {
			this.chargerElectricMeterOutboundPort.sendConsumption(0) ;			
		}
	}
	
	/**
	 * Sends energy to the battery using the bcm communication process
	 * pre : power greater or equal then 0
	 * @param power to send
	 * @throws Exception	<i>catch dans le bloc supérieur</i>
	 */
	public void sendPower(double power) throws Exception {
		assert power >=0;
		this.logMessage("Sending power to battery "+power+"....") ;
		this.chargerBatteryOutboundPort.sendPower(power) ;
	}

	
	/**
	 * starts the component
	 */
	public void	start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Charger component.") ;
	}
	//------------------------------------------------------------------------
	//----------------------------MODEL METHODS-------------------------------
	//------------------------------------------------------------------------
	
	/**
	 * Used for the simulation initialization
	 * @throws Exception
	 */
	protected void initialise() throws Exception {
		Architecture localArchitecture = this.createLocalArchitecture(null) ;
		this.asp = new ChargerSimulatorPlugin() ;
		this.asp.setPluginURI(localArchitecture.getRootModelURI()) ;
		this.asp.setSimulationArchitecture(localArchitecture) ;
		this.installPlugin(this.asp) ;
		this.toggleLogging() ;
	}
	
	
	/**
	 * Execute the component : First the simulation is launched, then the component behaviour
	 */
	@Override
	public void execute() throws Exception{
		super.execute();
		
		//---------------SIMULATION---------------
		SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 500L ;
		HashMap<String,Object> simParams = new HashMap<String,Object>() ;
		simParams.put(chargerRef, this) ;
		this.asp.setSimulationRunParameters(simParams) ;
		this.runTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							Thread.sleep(1000);
							asp.doStandAloneSimulation(0.0, 5000.0) ;
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				}) ;
		Thread.sleep(10L) ;
		
		//---------------BCM---------------
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							while(true) {
								if(isOn) {
									
									((Charger)this.getTaskOwner()).sendPower((conso*2)/3.0);
								}
								((Charger)this.getTaskOwner()).sendConsumption();
								Thread.sleep(1000);
							}
						} catch (Exception e) {throw new RuntimeException(e) ;}
					}
				},
				1000, TimeUnit.MILLISECONDS);
	}

	
//------------------------------------------------------------------------
//-------------------------SIMULATION METHODS-----------------------------
//------------------------------------------------------------------------
	@Override
	protected Architecture createLocalArchitecture(String architectureURI) throws Exception{
		return ChargerCoupledModel.build() ;
	}

	@Override
	public Object getEmbeddingComponentStateValue(String name) throws Exception{
		if(name.equals("state")) {
			return (Boolean)isOn;
		}else {
			return null;
		}
	}

	
	
//------------------------------------------------------------------------
//----------------------------FINALISE------------------------------------
//------------------------------------------------------------------------
	@Override
	public void finalise() throws Exception {
		chargerOutboundPort.doDisconnection();
		chargerBatteryOutboundPort.doDisconnection();
		chargerElectricMeterOutboundPort.doDisconnection();
		super.finalise();
	}

//------------------------------------------------------------------------
//----------------------------SHUTDOWN------------------------------------
//------------------------------------------------------------------------
	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			chargerInboundPort.unpublishPort();
			chargerOutboundPort.unpublishPort();
			chargerBatteryOutboundPort.unpublishPort();
			chargerElectricMeterInboundPort.unpublishPort();
			chargerElectricMeterOutboundPort.unpublishPort();
		} catch (Exception e) {e.printStackTrace();}
		super.shutdown();
	}
}
