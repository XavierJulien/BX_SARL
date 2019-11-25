package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.BatteryI;
import interfaces.ControllerI;

public class BatteryControllerConnector extends AbstractConnector implements BatteryI {

	@Override
	public void startBattery() throws Exception {
		((ControllerI)this.offering).startBattery();
	}
 
	@Override
	public void stopBattery() throws Exception {
		((ControllerI)this.offering).stopBattery();
	}

	@Override
	public double sendChargePercentage() throws Exception {
		return 0;
	}

	@Override
	public double sendEnergy() throws Exception {
		return 0;
	}
}