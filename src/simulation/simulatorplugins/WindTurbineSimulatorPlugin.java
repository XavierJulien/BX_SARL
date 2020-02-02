package simulation.simulatorplugins;

import fr.sorbonne_u.components.cyphy.plugins.devs.AtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import simulation.models.windturbine.WindTurbineModel;

/**
 * The class <code>WindTurbineSimulatorPlugin</code> implements the simulation
 * plug-in for the component <code>WindTurbine</code>.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * This implementation shows how to use the simulation model access facility.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * 
 * @author	Julien Xavier et Alexis Belanger
 */
public class WindTurbineSimulatorPlugin extends AtomicSimulatorPlugin {
	
	private static final long serialVersionUID = 1L;

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AtomicSimulatorPlugin#getModelStateValue(java.lang.String, java.lang.String)
	 */
	@Override
	public Object getModelStateValue(String modelURI, String name) throws Exception {
		ModelDescriptionI m = this.simulator.getDescendentModel(modelURI) ;
		
		assert	m instanceof WindTurbineModel ;
		if (name.equals("state")) {
			return ((WindTurbineModel)m).getState() ;
		} else {
			assert	name.equals("production") ;
			return ((WindTurbineModel)m).getProduction() ;
		}
	}
}