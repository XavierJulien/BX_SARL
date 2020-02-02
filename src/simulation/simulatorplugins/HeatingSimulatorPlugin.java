package simulation.simulatorplugins;

import fr.sorbonne_u.components.cyphy.plugins.devs.AtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import simulation.models.heating.HeatingModel;

/**
 * The class <code>HeatingSimulatorPlugin</code> implements the simulation
 * plug-in for the component <code>Heating</code>.
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
public class HeatingSimulatorPlugin extends	AtomicSimulatorPlugin{
	
	private static final long serialVersionUID = 1L;

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AtomicSimulatorPlugin#getModelStateValue(java.lang.String, java.lang.String)
	 */
	@Override
	public Object getModelStateValue(String modelURI, String name) throws Exception{
		ModelDescriptionI m = this.simulator.getDescendentModel(modelURI) ;
		assert	m instanceof HeatingModel ;
		if (name.equals("power")) {
			return ((HeatingModel)m).getPower() ;
		}
		else{
			return null ;
		}
	}
}