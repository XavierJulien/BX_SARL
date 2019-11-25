package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.HeatSensorHeatingI;
import interfaces.HeatingHeatSensorI;

public class HeatSensorHeatingConnector extends AbstractConnector implements HeatSensorHeatingI {

	@Override
	public double getHeating() throws Exception {
		return ((HeatingHeatSensorI)this.offering).sendHeating() ;
	}

}
