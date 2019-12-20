package simulation.components.heating;


import fr.sorbonne_u.components.cyphy.plugins.devs.AtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import simulation.models.heating.HeatingModel;

public class			HeatingSimulatorPlugin
extends		AtomicSimulatorPlugin
{
	private static final long serialVersionUID = 1L;

	@Override
	public Object		getModelStateValue(String modelURI, String name)
	throws Exception
	{
		// Get a Java reference on the object representing the corresponding
		// simulation model.
		ModelDescriptionI m = this.simulator.getDescendentModel(modelURI) ;
		assert	m instanceof HeatingModel ;
		if (name.equals("state")) {
			return ((HeatingModel)m).getState() ;
		}
		if (name.equals("mode")) {
			return ((HeatingModel)m).getMode() ;
		}
		else{
			assert name.equals("temperature");
			return ((HeatingModel)m).getTemperature() ;
		}
	}
}
//------------------------------------------------------------------------------

