package simulation.components.windturbine;

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

import java.util.HashMap;
import java.util.Random;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.cyphy.interfaces.EmbeddingComponentStateAccessI;
import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.simulators.SimulationEngine;
import simulation.models.windturbine.WindTurbineCoupledModel;
import simulation.models.windturbine.WindTurbineModel;

//------------------------------------------------------------------------------
/**
* The class <code>WindTurbine</code> implements a windturbine component that will
* hold the windturbine simulation model.
*
* <p><strong>Description</strong></p>
* 
* <p><strong>Invariant</strong></p>
* 
* <pre>
* invariant		true
* </pre>
* 
* <p>Created on : 2019-10-11</p>
* 
* @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
*/
public class			WindTurbine
extends		AbstractCyPhyComponent
implements	EmbeddingComponentStateAccessI
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	protected WindTurbineSimulatorPlugin		asp ;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	protected			WindTurbine() throws Exception
	{
		// 2 threads to be able to execute tasks and requests while executing
		// the DEVS simulation.
		super(2, 0) ;
		this.initialise() ;

	}

	protected WindTurbine(String reflectionInboundPortURI) throws Exception
	{
		super(reflectionInboundPortURI, 1, 0) ;
		this.initialise() ;
	}

	protected void		initialise() throws Exception
	{
		// The coupled model has been made able to create the simulation
		// architecture description.
		Architecture localArchitecture = this.createLocalArchitecture(null) ;
		// Create the appropriate DEVS simulation plug-in.
		this.asp = new WindTurbineSimulatorPlugin() ;
		// Set the URI of the plug-in, using the URI of its associated
		// simulation model.
		this.asp.setPluginURI(localArchitecture.getRootModelURI()) ;
		// Set the simulation architecture.
		this.asp.setSimulationArchitecture(localArchitecture) ;
		// Install the plug-in on the component, starting its own life-cycle.
		this.installPlugin(this.asp) ;

		// Toggle logging on to get a log on the screen.
		this.toggleLogging() ;
	}
	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent#createLocalArchitecture(java.lang.String)
	 */
	@Override
	protected Architecture	createLocalArchitecture(String architectureURI)
	throws Exception
	{
		return WindTurbineCoupledModel.build() ;
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
	@Override
	public void			execute() throws Exception
	{
		SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 10L ;
		// To give an example of the embedding component access facility, the
		// following lines show how to set the reference to the embedding
		// component or a proxy responding to the access calls.
		HashMap<String,Object> simParams = new HashMap<String,Object>() ;
		simParams.put("componentRef", this) ;
		this.asp.setSimulationRunParameters(simParams) ;
		// Start the simulation.
		this.runTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							asp.doStandAloneSimulation(0.0, 500.0) ;
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				}) ;
		Thread.sleep(10L) ;
		// During the simulation, the following lines provide an example how
		// to use the simulation model access facility by the component.
		for (int i = 0 ; i < 100 ; i++) {
			this.logMessage("WindTurbine " +
				this.asp.getModelStateValue(WindTurbineModel.URI, "state") + " " +
				this.asp.getModelStateValue(WindTurbineModel.URI, "intensity")) ;
			Thread.sleep(5L) ;
		}
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.interfaces.EmbeddingComponentStateAccessI#getEmbeddingComponentStateValue(java.lang.String)
	 */
	@Override
	public Object		getEmbeddingComponentStateValue(String name)
	throws Exception
	{
		Random r = new Random();
		switch(r.nextInt(3)) {
		case 0 : return WindTurbineModel.State.LOW;
		case 1 : return WindTurbineModel.State.HIGH;
		case 2 : return WindTurbineModel.State.OFF;
		}
		return WindTurbineModel.State.OFF;
		// As there is only one value, don't care about the name.
		 // just an example...
		// With this facility, the state of the windturbine can be set by the
		// component part (e.g., the controller) rather than by the user model
		// included in the windturbine model in a development process going from
		// a pure MIL simulation to a SIL simulation.
	}
}
//------------------------------------------------------------------------------

