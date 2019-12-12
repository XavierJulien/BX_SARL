package connectors.battery;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.battery.BatteryI;
import interfaces.controller.ControllerI;

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
	public void sendChargePercentage(double energy) throws Exception {
		((ControllerI)this.offering).getBatteryChargePercentage(energy);		
	}

	@Override
	public void sendEnergy(double energy) throws Exception {
		((ControllerI)this.offering).getBatteryProduction(energy);		
	}
}