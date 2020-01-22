package bcm.connectors.kettle;

import bcm.interfaces.controller.ControllerI;
import bcm.interfaces.kettle.KettleI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class KettleControllerConnector extends	AbstractConnector implements KettleI{

	@Override
	public void startKettle() throws Exception {
		((ControllerI)this.offering).startKettle();
	}

	@Override
	public void stopKettle() throws Exception {
		((ControllerI)this.offering).stopKettle();
	}

}
