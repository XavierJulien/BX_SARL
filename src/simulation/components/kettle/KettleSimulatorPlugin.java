package simulation.components.kettle;


import fr.sorbonne_u.components.cyphy.plugins.devs.AtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import simulation.models.kettle.KettleModel;

public class			KettleSimulatorPlugin
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
		assert	m instanceof KettleModel ;
		if (name.equals("state")) {
			return ((KettleModel)m).getState() ;
		}
		if (name.equals("content")) {
			return ((KettleModel)m).getContent() ;
		}
		else{
			assert name.equals("temperature");
			return ((KettleModel)m).getTemperature() ;
		}
	}
}
//------------------------------------------------------------------------------

