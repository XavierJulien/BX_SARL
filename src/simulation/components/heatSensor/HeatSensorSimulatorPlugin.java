package simulation.components.heatSensor;


import fr.sorbonne_u.components.cyphy.plugins.devs.AtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import simulation.models.heatSensor.HeatSensorModel;

public class			HeatSensorSimulatorPlugin
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
		assert	m instanceof HeatSensorModel ;
		//if (name.equals("wind")) {
			return ((HeatSensorModel)m).getTemperature() ;
		//}
	}
}
//------------------------------------------------------------------------------

