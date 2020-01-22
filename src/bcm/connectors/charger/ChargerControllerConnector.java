package bcm.connectors.charger;

import bcm.interfaces.charger.ChargerI;
import bcm.interfaces.controller.ControllerI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
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
	public void sendConsumption(double consumption) throws Exception {
		//unused		
	}

	@Override
	public void sendPower(double power) throws Exception {
		//unused
	}

}
