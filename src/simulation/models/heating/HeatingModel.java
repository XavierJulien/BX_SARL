package simulation.models.heating;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.cyphy.interfaces.EmbeddingComponentStateAccessI;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOAwithEquations;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;
import fr.sorbonne_u.utils.PlotterDescription;
import fr.sorbonne_u.utils.XYPlotter;
import simulation.events.AbstractEvent;
import simulation.events.heating.HeatingUpdater;

@ModelExternalEvents(imported = { HeatingUpdater.class})

public class HeatingModel extends AtomicHIOAwithEquations {
	
	/**
	 * The enum State represent the state of the component (on/off)
	 * 
	 *
	 */
	public static enum 		State {
		OFF,
		ON
	}
	
	
	/**
	* The class <code>Heating</code> implements a simplified DEVS
	* simulation model of an Heating providing the current power level (in percentage) of the heating 
	*
	* <p><strong>Description</strong></p>
	* 
	* <p>
	* 
	* The heating power is determined by the controller, which choose to put more power or dicrease it.
	* </p>
	* <p>
	* The Electric model is commanded through only one event
	* <code>HeatingUpdater</code>, which is used to update the heating power level.
	* </p>
	* 
	* <p><strong>Invariant</strong></p>
	* 
	* <pre>
	* invariant		true	
	* </pre>
	* 
	* <p>Created on : 2019-10-10</p>
	* 
	* 
	*/
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long					serialVersionUID = 1L;
	private static final String					SERIESPOWER = "power";
	public static final String					URI = "HeatingModel";
	protected XYPlotter							powerPlotter; 
	protected int								currentPower;
	protected boolean 							on= false;					
	protected EmbeddingComponentStateAccessI 	componentRef;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public HeatingModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		PlotterDescription pdOnOff =
				new PlotterDescription(
						"Heating power",
						"Time (sec)",
						"Power percentage",
						300,
						750,
						300,
						200);
		this.powerPlotter = new XYPlotter(pdOnOff);
		this.powerPlotter.createSeries(SERIESPOWER);
		this.setLogger(new StandardLogger());
	}

	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------
	@Override
	public void	setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
		this.componentRef = (EmbeddingComponentStateAccessI) simParams.get("heatingRef");
	}

	@Override
	public void	initialiseState(Time initialTime) {
		this.currentPower = 0;
		this.powerPlotter.initialise();
		this.powerPlotter.showPlotter();
		super.initialiseState(initialTime);
	}

	@Override
	protected void initialiseVariables(Time startTime){
		this.powerPlotter.addData(
				SERIESPOWER,
				this.getCurrentStateTime().getSimulatedTime(),
				0);
		super.initialiseVariables(startTime);
	}

	@Override
	public Vector<EventI> output(){
		return null;
	}

	@Override
	public Duration	timeAdvance(){
		if (this.componentRef == null) {
			return Duration.INFINITY;
		} else {
			return new Duration(10.0, TimeUnit.SECONDS);
		}
	}

	@Override
	public void	userDefinedExternalTransition(Duration elapsedTime) { 
		Vector<EventI> currentEvents = this.getStoredEventAndReset();
		assert	currentEvents != null && currentEvents.size() == 1;
		Event ce = (Event) currentEvents.get(0);
		assert	ce instanceof AbstractEvent;
		this.powerPlotter.addData(
				SERIESPOWER,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getPower());
		ce.executeOn(this);
		this.powerPlotter.addData(
				SERIESPOWER,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getPower());
		super.userDefinedExternalTransition(elapsedTime);
	}

	@Override
	public void endSimulation(Time endTime) throws Exception {
		this.powerPlotter.addData(
				SERIESPOWER,
				endTime.getSimulatedTime(),
				this.getPower());
		Thread.sleep(10000L);
		this.powerPlotter.dispose();
		super.endSimulation(endTime);
	}

	// ------------------------------------------------------------------------
	// Model-specific methods
	// ------------------------------------------------------------------------
	
	/**
	 * 
	 * @return the current heating power percentage
	 */
	public int getPower(){return this.currentPower;}
	
	// ------------------------------------------------------------------------
	// Utils
	// ------------------------------------------------------------------------

	/**
	 * The method UpdatePower is used by the HeatingUpdater events to update the power percentage by reading it in the component
	 */
	public void	updatePower(){
		try {
			this.currentPower= (Integer)componentRef.getEmbeddingComponentStateValue("power");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}