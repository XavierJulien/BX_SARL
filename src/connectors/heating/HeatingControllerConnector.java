package connectors.heating;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.controller.ControllerI;
import interfaces.heating.HeatingI;

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