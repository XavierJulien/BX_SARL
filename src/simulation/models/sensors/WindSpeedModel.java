package simulation.models.sensors;


import java.util.Map;

//Copyright Jacques Malenfant, Sorbonne Universite.
//Jacques.Malenfant@lip6.fr
//
//This software is a computer program whose purpose is to provide a
//new implementation of the DEVS simulation standard for Java.
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

import java.util.Vector;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.examples.molene.tic.TicEvent;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ImportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOAwithEquations;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.interfaces.SimulationReportI;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.devs_simulation.utils.AbstractSimulationReport;
import fr.sorbonne_u.utils.PlotterDescription;
import fr.sorbonne_u.utils.XYPlotter;
import simulation.events.sensors.WindPowerReading;
import simulation.events.sensors.WindTooStrongEvent;


//-----------------------------------------------------------------------------
@ModelExternalEvents(imported = {TicEvent.class},
					 exported = {WindPowerReading.class, WindTooStrongEvent.class})
//-----------------------------------------------------------------------------
public class			WindSpeedModel
extends		AtomicHIOAwithEquations
{
	// -------------------------------------------------------------------------
	// Inner classes
	// -------------------------------------------------------------------------


	public static class	WindPowerSensorReport
	extends		AbstractSimulationReport
	{
		private static final long 					serialVersionUID = 1L ;
		public final Vector<WindPowerReading>	readings ;

		public			WindPowerSensorReport(
			String modelURI,
			Vector<WindPowerReading> readings
			)
		{
			super(modelURI) ;
			this.readings = readings ;
		}

		@Override
		public String	toString()
		{
			String ret = "\n-----------------------------------------\n" ;
			ret += "Wind Power Sensor Report\n" ;
			ret += "-----------------------------------------\n" ;
			ret += "number of readings = " + this.readings.size() + "\n" ;
			ret += "Readings:\n" ;
			for (int i = 0 ; i < this.readings.size() ; i++) {
				ret += "    " + this.readings.get(i).eventAsString() + "\n" ;
			}
			ret += "-----------------------------------------\n" ;
			return ret ;
		}
	}

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long		serialVersionUID = 1L ;
	private static final String		SERIES1 = "wind power" ;
	private static final String		SERIES2 = "wind too strong" ;
	
	public static final String		URI = "windPowerSensor-1" ;
	
	public static final String	WINDPOWER_PLOTTING_PARAM_NAME = "wind-power-plotting" ;
public static final String	WIND_TOO_STRONG_PLOTTING_PARAM_NAME = "wind-too-strong-plotting" ;

	/** true when a external event triggered a reading.						*/
	protected boolean								triggerReading ;
	/** the last value emitted as a reading of the windPower.			 	*/
	protected double								lastReading ;
	/** the simulation time at the last reading.							*/
	protected double								lastReadingTime ;
	/** history of readings, for the simulation report.						*/
	protected final Vector<WindPowerReading>	readings ;

	/** frame used to plot the wind power readings during the simulation.	*/
	protected XYPlotter				wind_power_plotter ;
	protected XYPlotter				wind_too_strong_plotter ;
	

	// -------------------------------------------------------------------------
	// HIOA model variables
	// -------------------------------------------------------------------------

	/** windPower in meters per second.								*/
	@ImportedVariable(type = Double.class)
	protected Value<Double> windPower;
	
	@ImportedVariable(type = Boolean.class)
	protected Value<Boolean> windTooStrong;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------


	public				WindSpeedModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception
	{
		super(uri, simulatedTimeUnit, simulationEngine) ;

		// Uncomment to get a log of the events.
		//this.setLogger(new StandardLogger()) ;

		// Model implementation variable initialisation
		this.lastReading = -1.0 ;

		// Create the representation of the sensor windPower function
		this.readings = new Vector<WindPowerReading>() ;
	}

	// -------------------------------------------------------------------------
	// Simulation protocol and related methods
	// -------------------------------------------------------------------------

	@Override
	public void			setSimulationRunParameters(
		Map<String, Object> simParams
		) throws Exception
	{
		// Get the values of the run parameters in the map using their names
		// and set the model implementation variables accordingly
		String vname = this.getURI() + ":" +
				WindSpeedModel.WINDPOWER_PLOTTING_PARAM_NAME ;
		PlotterDescription pd = (PlotterDescription) simParams.get(vname) ;
		this.wind_power_plotter = new XYPlotter(pd) ;
		this.wind_power_plotter.createSeries(SERIES2) ;
		vname = this.getURI() + ":" +
				WindSpeedModel.WIND_TOO_STRONG_PLOTTING_PARAM_NAME ;
		pd = (PlotterDescription) simParams.get(vname) ;
		this.wind_too_strong_plotter = new XYPlotter(pd) ;
		this.wind_too_strong_plotter.createSeries(SERIES1) ;
	}

	@Override
	public void			initialiseState(Time initialTime)
	{
		this.triggerReading = false ;

		this.lastReadingTime = initialTime.getSimulatedTime() ;
		this.readings.clear() ;
		if (this.wind_power_plotter != null) {
			this.wind_power_plotter.initialise() ;
			this.wind_power_plotter.showPlotter() ;
		}
		if (this.wind_too_strong_plotter != null) {
			this.wind_too_strong_plotter.initialise() ;
			this.wind_too_strong_plotter.showPlotter() ;
		}

		super.initialiseState(initialTime);
	}

	
	
	@Override
	protected void		initialiseVariables(Time startTime)
	{
		super.initialiseVariables(startTime);
		this.windPower.v = 0.0 ;

		assert	startTime.equals(this.windPower.time) ;

		if (this.wind_power_plotter != null) {
			this.wind_power_plotter.addData(SERIES1,
											startTime.getSimulatedTime(),
											this.windPower.v) ;
		}
	}
	
	
	
	
	@Override
	public Duration		timeAdvance()
	{
		if (this.triggerReading) {
			return Duration.zero(this.getSimulatedTimeUnit()) ;
		} else {
			return Duration.INFINITY ;
		}
	}

	@Override
	public Vector<EventI>	output()
	{
		if (this.triggerReading) {
			if (this.wind_power_plotter != null) {
				this.wind_power_plotter.addData(
					SERIES1,
					this.lastReadingTime,
					this.windPower.v) ;
				this.wind_power_plotter.addData(
					SERIES1,
					this.getCurrentStateTime().getSimulatedTime(),
					this.windPower.v) ;
			}
			this.lastReading = this.windPower.v ;
			this.lastReadingTime =
					this.getCurrentStateTime().getSimulatedTime() ;

			Vector<EventI> ret = new Vector<EventI>(1) ;
			Time currentTime = 
					this.getCurrentStateTime().add(this.getNextTimeAdvance()) ;
			WindPowerReading wbr =
					new WindPowerReading(currentTime, this.windPower.v) ;
			ret.add(wbr) ;

			this.readings.addElement(wbr) ;
			this.logMessage(this.getCurrentStateTime() +
					"|output|windPower reading " +
					this.readings.size() + " with value = " +
					this.windPower.v) ;

			this.triggerReading = false ;
			return ret ;
		} else {
			return null ;
		}
	}

	@Override
	public void			userDefinedInternalTransition(Duration elapsedTime)
	{
		super.userDefinedInternalTransition(elapsedTime) ;
		this.logMessage(this.getCurrentStateTime() +
								"|internal|windPower = " +
								this.windPower.v + " Mbps.") ;
	}

	@Override
	public void			userDefinedExternalTransition(Duration elapsedTime)
	{
		super.userDefinedExternalTransition(elapsedTime) ;

		Vector<EventI> current = this.getStoredEventAndReset() ;
		boolean	ticReceived = false ;
		for (int i = 0 ; !ticReceived && i < current.size() ; i++) {
			if (current.get(i) instanceof TicEvent) {
				ticReceived = true ;
			}
		}
		if (ticReceived) {
			this.triggerReading = true ;
			this.logMessage(this.getCurrentStateTime() +
									"|external|tic event received.") ;
		}
	}

	@Override
	public void			endSimulation(Time endTime) throws Exception
	{
		if (this.wind_power_plotter != null) {
			this.wind_power_plotter.addData(SERIES1,
								 endTime.getSimulatedTime(),
								 this.lastReading) ;
		}

		super.endSimulation(endTime);
	}

	@Override
	public SimulationReportI		getFinalReport() throws Exception
	{
		return new WindPowerSensorReport(this.getURI(), this.readings) ;
	}
}
//-----------------------------------------------------------------------------
