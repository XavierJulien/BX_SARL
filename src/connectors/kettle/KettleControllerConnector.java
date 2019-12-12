package connectors.kettle;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.controller.ControllerI;
import interfaces.kettle.KettleI;

public class KettleControllerConnector extends	AbstractConnector implements KettleI{

	@Override
	public void startKettle() throws Exception {
		((ControllerI)this.offering).startKettle();
	}

	@Override
	public void stopKettle() throws Exception {
		((ControllerI)this.offering).stopKettle();
	}

	@Override
	public double sendConsumption() throws Exception {
		return 0;
	}
}
