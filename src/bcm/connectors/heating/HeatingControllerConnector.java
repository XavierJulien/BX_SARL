package bcm.connectors.heating;

import bcm.interfaces.controller.ControllerI;
import bcm.interfaces.heating.HeatingI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class HeatingControllerConnector extends AbstractConnector implements HeatingI{

	@Override
	public void startHeating() throws Exception {
		((ControllerI)this.offering).startHeating();
	}

	@Override
	public void stopHeating() throws Exception {
		((ControllerI)this.offering).stopHeating();
	}

	@Override
	public void putExtraPowerInHeating(int power) throws Exception {
		//shouldn't be used
	}

	@Override
	public void slowHeating(int power) throws Exception {
		//shouldn't be used		
	}

}