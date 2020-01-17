package simulation.models.electricMeter;

//Copyright Jacques Malenfant, Sorbonne Universite.
//Jacques.Malenfant@lip6.fr
//
//This software is a computer program whose purpose is to provide an extension
//of the BCM component model that aims to define a components tailored for
//cyber-physical control systems (CPCS) for Java.
//
//This software is governed by the CeCILL-C license under French law and
//abiding by the rules of distribution of free software.  You can use,
//modify and/ or redistribute the software under the terms of the
//CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
//URL "http://www.cecill.info".
//
//As a counterpart to the access to the source code and  rights to copy,
//modify and redistribute granted by the license, users are provided only
//with a limited warranty  and the software's author,  the holder of the
//economic rights,  and the successive licensors  have only  limited
//liability. 
//
//In this respect, the user's attention is drawn to the risks associated
//with loading,  using,  modifying and/or developing or reproducing the
//software by the user in light of its specific status of free software,
//that may mean  that it is complicated to manipulate,  and  that  also
//therefore means  that it is reserved for developers  and  experienced
//professionals having in-depth computer knowledge. Users are therefore
//encouraged to load and test the software's suitability as regards their
//requirements in conditions enabling the security of their systems and/or 
//data to be ensured and,  more generally, to use and operate it in the 
//same conditions as regards security. 
//
//The fact that you are presently reading this means that you have had
//knowledge of the CeCILL-C license and that you accept its terms.

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.cyphy.interfaces.EmbeddingComponentStateAccessI;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOAwithEquations;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.interfaces.SimulationReportI;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.devs_simulation.utils.AbstractSimulationReport;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;
import fr.sorbonne_u.utils.PlotterDescription;
import fr.sorbonne_u.utils.XYPlotter;
import simulation.events.AbstractEvent;
import simulation.events.electricMeter.TotalConsumptionUpdater;
import simulation.events.windturbine.WTProductionUpdater;
import simulation.events.windturbine.WindOk;
import simulation.events.windturbine.WindTooStrong;

//-----------------------------------------------------------------------------
/**
* The class <code>WindTurbineModel</code> implements a simplified DEVS
* simulation model of a windturbine providing the current intensity of
* electricity consumption as a continuous variable.
*
* <p><strong>Description</strong></p>
* 
* <p>
* The windturbine can of course be switch on and off as well as set to a low
* or a high mode. In the low mode, the
* power consumption is 800 watts while in the high mode it is 1200 watts.
* The appliance is assumed to use a 220 volts tension. The model compute
* the intensity of the current over time in amperes.
* </p>
* <p>
* The windturbine model is commanded through four external events:
* <code>SwitchOn</code>, <code>SwitchOff</code>, <code>SetLow</code> and
* <code>SetHigh</code> to make it go from one state to the other. When the
* windturbine is switched on, it is put in the low mode.
* </p>
* 
* <p><strong>Invariant</strong></p>
* 
* <pre>
* invariant		true	// TODO
* </pre>
* 
* <p>Created on : 2019-10-10</p>
* 
* @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
*/
//-----------------------------------------------------------------------------
@ModelExternalEvents(imported = {TotalConsumptionUpdater.class})

//-----------------------------------------------------------------------------
public class			ElectricMeterModel
extends		AtomicHIOAwithEquations
{
	// -------------------------------------------------------------------------
	// Inner classes and types
	// -------------------------------------------------------------------------

	/**
	 * The enumeration <code>State</code> describes the discrete states or
	 * modes of the windturbine.
	 *
	 * <p><strong>Description</strong></p>
	 * 
	 * The windturbine can be <code>OFF</code> or on, and then it is either in
	 * <code>LOW</code> mode (wind is weak) or in
	 * <code>HIGH</code> mode (wind is strong).
	 * 
	 * <p>Created on : 2019-10-10</p>
	 * 
	 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
	 */
	public static enum State {
		OFF,
		/** low mode is when wind is weak.						*/
		ON
	}

	/**
	 * The class <code>WindTurbineReport</code> implements the simulation
	 * report of the windturbine model.
	 *
	 * <p><strong>Description</strong></p>
	 * 
	 * <p><strong>Invariant</strong></p>
	 * 
	 * <pre>
	 * invariant		true
	 * </pre>
	 * 
	 * <p>Created on : 2019-10-10</p>
	 * 
	 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
	 */
	public static class		WindTurbineReport
	extends		AbstractSimulationReport
	{
		private static final long serialVersionUID = 1L;
		
		public			WindTurbineReport(String modelURI)
		{
			super(modelURI);
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String	toString()
		{
			return "WindTurbineReport(" + this.getModelURI() + ")" ;
		}
	}

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long		serialVersionUID = 1L ;
	/** URI used to create instances of the model; assumes a singleton,
	 *  otherwise a different URI must be given to each instance.			*/
	public static final String		URI = "WindTurbineModel" ;

	private static final String		SERIES = "production" ;

	
	protected static final double	TENSION = 220.0 ; // Volts

	/** current intensity in Amperes; intensity is power/tension.			*/
	@ExportedVariable(type = Double.class)
	protected final Value<Double>				currentProd = new Value<Double>(this, 0.0, 0);

	protected  double wind;
	
	protected State					currentState ;

	/** plotter for the intensity level over time.							*/
	protected XYPlotter				intensityPlotter ;

	/** reference on the object representing the component that holds the
	 *  model; enables the model to access the state of this component.		*/
	protected EmbeddingComponentStateAccessI componentRef ;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a windturbine model instance.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	uri != null
	 * pre	simulatedTimeUnit != null
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param uri				URI of the model.
	 * @param simulatedTimeUnit	time unit used for the simulation time.
	 * @param simulationEngine	simulation engine to which the model is attached.
	 * @throws Exception		<i>to do.</i>
	 */
	public				ElectricMeterModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception
	{
		super(uri, simulatedTimeUnit, simulationEngine) ;

		// creation of a plotter to show the evolution of the intensity over
		// time during the simulation.
		PlotterDescription pd =
				new PlotterDescription(
						"WindTurbine intensity",
						"Time (sec)",
						"Intensity (Amp)",
						300,
						250,
						300,
						200) ;
		this.intensityPlotter = new XYPlotter(pd) ;
		this.intensityPlotter.createSeries(SERIES) ;
		this.wind = 0;

		// create a standard logger (logging on the terminal)
		this.setLogger(new StandardLogger()) ;
	}

	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#setSimulationRunParameters(java.util.Map)
	 */
	@Override
	public void			setSimulationRunParameters(
		Map<String, Object> simParams
		) throws Exception
	{
		// The reference to the embedding component
		this.componentRef =
			(EmbeddingComponentStateAccessI) simParams.get("windTurbineRef") ;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			initialiseState(Time initialTime)
	{
		// the windturbine starts in mode OFF
		this.currentState = State.ON ;

		// initialisation of the intensity plotter 
		this.intensityPlotter.initialise() ;
		// show the plotter on the screen
		this.intensityPlotter.showPlotter() ;

		try {
			// set the debug level triggering the production of log messages.
			this.setDebugLevel(1) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}

		super.initialiseState(initialTime) ;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseVariables(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	protected void		initialiseVariables(Time startTime)
	{
		// as the windturbine starts in mode OFF, its power consumption is 0
		this.currentProd.v = 0.0 ;

		// first data in the plotter to start the plot.
		this.intensityPlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getProduction());

		super.initialiseVariables(startTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
	 */
	@Override
	public Vector<EventI>	output()
	{
		// the model does not export any event.
		return null ;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
	 */
	@Override
	public Duration		timeAdvance()
	{
		if (this.componentRef == null) {
			// the model has no internal event, however, its state will evolve
			// upon reception of external events.
			return Duration.INFINITY ;
		} else {
			// This is to test the embedding component access facility.
			return new Duration(10.0, TimeUnit.SECONDS) ;
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void			userDefinedInternalTransition(Duration elapsedTime)
	{
		if (this.componentRef != null) {

		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void			userDefinedExternalTransition(Duration elapsedTime)
	{
		if (this.hasDebugLevel(2)) {
			this.logMessage("WindTurbineModel::userDefinedExternalTransition 1") ;
		}

		// get the vector of current external events
		Vector<EventI> currentEvents = this.getStoredEventAndReset() ;
		// when this method is called, there is at least one external event,
		// and for the windturbine model, there will be exactly one by
		// construction.
		assert	currentEvents != null && currentEvents.size() == 1 ;

		Event ce = (Event) currentEvents.get(0) ;
		assert	ce instanceof AbstractEvent ;
		if (this.hasDebugLevel(2)) {
			this.logMessage("WindTurbineModel::userDefinedExternalTransition 2 "
										+ ce.getClass().getCanonicalName()) ;
		}

		// the plot is piecewise constant; this data will close the currently
		// open piece
		this.intensityPlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getProduction());

		if (this.hasDebugLevel(2)) {
			this.logMessage("WindTurbineModel::userDefinedExternalTransition 3 "
															+ this.getState()) ;
		}

		// execute the current external event on this model, changing its state
		// and intensity level
		ce.executeOn(this) ;

		if (this.hasDebugLevel(1)) {
			this.logMessage("WindTurbineModel::userDefinedExternalTransition 4 "
															+ this.getState()) ;
		}

		// add a new data on the plotter; this data will open a new piece
		this.intensityPlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getProduction());

		super.userDefinedExternalTransition(elapsedTime) ;
		if (this.hasDebugLevel(2)) {
			this.logMessage("WindTurbineModel::userDefinedExternalTransition 5") ;
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			endSimulation(Time endTime) throws Exception
	{
		this.intensityPlotter.addData(
				SERIES,
				endTime.getSimulatedTime(),
				this.getProduction()) ;
		Thread.sleep(10000L) ;
		this.intensityPlotter.dispose() ;

		super.endSimulation(endTime) ;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#getFinalReport()
	 */
	@Override
	public SimulationReportI	getFinalReport() throws Exception
	{
		return new WindTurbineReport(this.getURI()) ;
	}

	// ------------------------------------------------------------------------
	// Model-specific methods
	// ------------------------------------------------------------------------

	/**
	 * set the state of the windturbine.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	s != null
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param s		the new state.
	 */
	public void			setState(State s)
	{
		this.currentState = s ;
//		switch (s)
//		{
//			case OFF : this.currentProd.v = 0.0 ; break ;
//			case ON :
//			try {
//				this.currentProd.v = ((Double)componentRef.getEmbeddingComponentStateValue("windSpeed")).doubleValue() ;
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//				break ;
//		}
	}

	/**
	 * return the state of the windturbine.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	ret != null
	 * </pre>
	 *
	 * @return	the state of the windturbine.
	 */
	public State		getState()
	{
		return this.currentState ;
	}

	/**
	 * return the current intensity of electricity consumption in amperes.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	{@code ret >= 0.0 and ret <= 1200.0/220.0}
	 * </pre>
	 *
	 * @return	the current intensity of electricity consumption in amperes.
	 */
	public double getProduction() {
		return currentProd.v;
	}
	
	public void updateConsumption() {
		
		try {
			this.wind = ((Double)componentRef.getEmbeddingComponentStateValue("windSpeed")).doubleValue();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if(currentState == State.OFF) {
			if(this.wind < 8) {
				setState(State.ON);
				currentProd.v = 3*wind;
			}else {
				currentProd.v = Math.max(0, currentProd.v-6);
			}
		}else {
			if(this.wind < 8) {
				currentProd.v = 3*wind;
			}else {
				setState(State.OFF);
				currentProd.v = Math.max(0, currentProd.v-6);
			}
		}		
		
	}
}
//------------------------------------------------------------------------------
