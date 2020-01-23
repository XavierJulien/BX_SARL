package simulation.simulatorplugins;

import fr.sorbonne_u.components.cyphy.plugins.devs.AtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import simulation.models.heating.HeatingModel;

public class HeatingSimulatorPlugin extends	AtomicSimulatorPlugin{
	
	private static final long serialVersionUID = 1L;

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