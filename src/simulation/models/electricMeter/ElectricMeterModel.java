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
import simulation.events.electricMeter.ElectricMeterUpdater;

//-----------------------------------------------------------------------------
/**
* The class <code>ElectricMeter</code> implements a simplified DEVS
* simulation model of an Electric meter providing the current consumption of the different "consumers" components 
*
* <p><strong>Description</strong></p>
* 
* <p>
* 
* The house total consumption is determined by the values taken in the Electric meter component
* </p>
* <p>
* The Electric model is commanded through only one event
* <code>ElectrcMeterUpdater</code>, which is used to update the total consumption of all the components.
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
//-----------------------------------------------------------------------------
@ModelExternalEvents(imported = {ElectricMeterUpdater.class})

//-----------------------------------------------------------------------------
public class			ElectricMeterModel
extends		AtomicHIOAwithEquations
{
	// -------------------------------------------------------------------------
	// Inner classes and types
	// -------------------------------------------------------------------------
	public static class		ElectricMeterReport
	extends		AbstractSimulationReport
	{
		private static final long serialVersionUID = 1L;
		
		public			ElectricMeterReport(String modelURI)
		{
			super(modelURI);
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String	toString()
		{
			return "ElectricMeterReport(" + this.getModelURI() + ")" ;
		}
	}

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long		serialVersionUID = 1L ;
	/** URI used to create instances of the model; assumes a singleton,
	 *  otherwise a different URI must be given to each instance.			*/
	public static final String		URI = "ElectricMeterModel" ;

	private static final String		SERIES = "consumption" ;

	
	protected static final double	TENSION = 220.0 ; // Volts

	/**
	 * Value currentConsumption is used to store the total consumption
	 */
	@ExportedVariable(type = Double.class)
	protected final Value<Double>				currentConsumption = new Value<Double>(this, 0.0, 0);

	

	/** plotter for the consumption over time.							*/
	protected XYPlotter				consumptionPlotter ;

	/** reference on the object representing the component that holds the
	 *  model; enables the model to access the state of this component.		*/
	protected EmbeddingComponentStateAccessI componentRef ;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public				ElectricMeterModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception
	{
		super(uri, simulatedTimeUnit, simulationEngine) ;

		// creation of a plotter to show the evolution of the consumption over
		// time during the simulation.
		PlotterDescription pd =
				new PlotterDescription(
						"Total house consumption",
						"Time (sec)",
						"Consumption",
						300,
						250,
						300,
						200) ;
		this.consumptionPlotter = new XYPlotter(pd) ;
		this.consumptionPlotter.createSeries(SERIES) ;

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
			(EmbeddingComponentStateAccessI) simParams.get("electricMeterRef") ;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			initialiseState(Time initialTime)
	{
		

		// initialisation of the consumption plotter 
		this.consumptionPlotter.initialise() ;
		// show the plotter on the screen
		this.consumptionPlotter.showPlotter() ;


		super.initialiseState(initialTime) ;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseVariables(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	protected void		initialiseVariables(Time startTime)
	{
		//initialize the total consumption at 0
		this.currentConsumption.v = 0.0 ;

		// first data in the plotter to start the plot.
		this.consumptionPlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getConsumption());

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
			this.logMessage("ElectricMeterModel::userDefinedExternalTransition 1") ;
		}

		// get the vector of current external events
		Vector<EventI> currentEvents = this.getStoredEventAndReset() ;
		// when this method is called, there is at least one external event,
		// and for the Electric Meter model, there will be exactly one by
		// construction.
		assert	currentEvents != null && currentEvents.size() == 1 ;

		Event ce = (Event) currentEvents.get(0) ;
		assert	ce instanceof AbstractEvent ;

		// the plot is piecewise constant; this data will close the currently
		// open piece
		this.consumptionPlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getConsumption());

		
		// execute the current external event on this model, changing its state
		// and consumption
		ce.executeOn(this) ;


		// add a new data on the plotter; this data will open a new piece
		this.consumptionPlotter.addData(
				SERIES,
				this.getCurrentStateTime().getSimulatedTime(),
				this.getConsumption());

		super.userDefinedExternalTransition(elapsedTime) ;
		if (this.hasDebugLevel(2)) {
			this.logMessage("ElectricMeterModel::userDefinedExternalTransition 5") ;
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			endSimulation(Time endTime) throws Exception
	{
		this.consumptionPlotter.addData(
				SERIES,
				endTime.getSimulatedTime(),
				this.getConsumption()) ;
		Thread.sleep(10000L) ;
		this.consumptionPlotter.dispose() ;

		super.endSimulation(endTime) ;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#getFinalReport()
	 */
	@Override
	public SimulationReportI	getFinalReport() throws Exception
	{
		return new ElectricMeterReport(this.getURI()) ;
	}

	// ------------------------------------------------------------------------
	// Model-specific methods
	// ------------------------------------------------------------------------

	/**
	 * 
	 * @return the current consumption
	 */
	public double getConsumption() {
		return currentConsumption.v;
	}
	
	/**
	 * This method is called by the ElectricMeterUpdater events to update the consumption.
	 * It reads the total consumption in the component ref 
	 */
	public void updateConsumption() {
		
		try {
			this.currentConsumption.v = (Double)componentRef.getEmbeddingComponentStateValue("total");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
//------------------------------------------------------------------------------
