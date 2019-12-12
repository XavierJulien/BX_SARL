package connectors.charger;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.charger.ChargerI;
import interfaces.controller.ControllerI;
public class ChargerControllerConnector extends AbstractConnector implements ChargerI{

	@Override
	public void startCharger() throws Exception {
		((ControllerI)this.offering).startCharger();
	}

	@Override
	public void stopCharger() throws Exception {
		((ControllerI)this.offering).stopCharger();
	}

	@Override
	public double sendConsumption() throws Exception {
		//shouldn't be used
		return 0;
	}	
}
