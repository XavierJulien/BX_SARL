package simulation.simulatorplugins;

import fr.sorbonne_u.components.cyphy.plugins.devs.AtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import simulation.models.charger.ChargerModel;

public class ChargerSimulatorPlugin extends	AtomicSimulatorPlugin {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public Object getModelStateValue(String modelURI, String name) throws Exception{
		ModelDescriptionI m = this.simulator.getDescendentModel(modelURI) ;
		
		assert	m instanceof ChargerModel ;
		assert (name.equals("consumption"));
		return ((ChargerModel)m).getConsumption() ;
	}
}