package simulation.components.windSensor;


import fr.sorbonne_u.components.cyphy.plugins.devs.AtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import simulation.models.heating.HeatingModel;
import simulation.models.windSensor.WindSensorModel;

public class			WindSensorSimulatorPlugin
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
		assert	m instanceof WindSensorModel ;
		//if (name.equals("wind")) {
			return ((WindSensorModel)m).getWind() ;
		//}
	}
}
//------------------------------------------------------------------------------

