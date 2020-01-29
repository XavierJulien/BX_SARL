package bcm.connectors.battery;

import bcm.interfaces.battery.BatteryI;
import bcm.interfaces.controller.ControllerI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
/**
 * This class is used to link the in/out ports used in the Battery - controler connection 
 * @author Julien Xavier & Alexis Belanger
 *
 */
public class BatteryControllerConnector extends AbstractConnector implements BatteryI {

	
	/**
	 *  This method is called by the controller outboundport to start the battery
	 */
	@Override
	public void startBattery() throws Exception {
		((ControllerI)this.offering).startBattery();
	}
 
	/**
	 * This method is called by the controller outboundport to stop the battery
	 */
	@Override
	public void stopBattery() throws Exception {
		((ControllerI)this.offering).stopBattery();
	}
	
	/**
	 * this method is used by the battery outbound port to sned his charge percentage to the controller
	 */
	@Override
	public void sendChargePercentage(double energy) throws Exception {
		((ControllerI)this.offering).getBatteryChargePercentage(energy);		
	}

	/**
	 * this method is called by the battery outboundport to inform the controller of the energy quantity produced by the battery
	 */
	@Override
	public void sendEnergy(double energy) throws Exception {
		((ControllerI)this.offering).getBatteryProduction(energy);		
	}

	/**
	 * This method is not used here, only here because of the implements BatteryI
	 */
	@Override
	public void receivePower(double power) throws Exception {
		//unused
	}
}