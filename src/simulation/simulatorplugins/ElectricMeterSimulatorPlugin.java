package simulation.simulatorplugins;

import fr.sorbonne_u.components.cyphy.plugins.devs.AtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import simulation.models.electricMeter.ElectricMeterModel;

/**
 * The class <code>ElectricMeterSimulatorPlugin</code> implements the simulation
 * plug-in for the component <code>ElectricMeter</code>.
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
 * @author	Julien Xavier & Alexis Belanger</a>
 */
public class ElectricMeterSimulatorPlugin extends	AtomicSimulatorPlugin{
	
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AtomicSimulatorPlugin#getModelStateValue(java.lang.String, java.lang.String)
	 */
	@Override
	public Object getModelStateValue(String modelURI, String name) throws Exception{
		ModelDescriptionI m = this.simulator.getDescendentModel(modelURI) ;
		assert	m instanceof ElectricMeterModel ;
		assert	name.equals("total") ;
		return ((ElectricMeterModel)m).getConsumption() ;
	}
}