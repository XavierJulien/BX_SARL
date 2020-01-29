package bcm.interfaces.battery;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

/**
 * this class defines the interface BatteryI that defines the differnet methods represneting the Battery behaviour
 * 
 * @author Julien Xavier et Alexis Belanger
 *
 */
public interface BatteryI extends DataOfferedI,DataRequiredI{
	
	/**
	 * This method is called by the Controller Outboundport and calls the Battery OutboundPort startBattery method to start the battery
	 * @throws Exception
	 */
	public void startBattery() throws Exception;
	
	/**
	 * This method is called by the Controller Outboundport and calls the Battery OutboundPort stopBattery method to stop the battery
	 * @throws Exception
	 */
	public void stopBattery() throws Exception;
	
	/**
	 * This method is called by the Battery Outboundport and calls the Controller OutboundPort getBatteryChargePercentage method to get the battery charge level
	 * @param percentage the current purcentage of energy remaining in the battery
	 * @throws Exception
	 */
	public void sendChargePercentage(double percentage) throws Exception;
	
	/**
	 * This method is called by the Battery Outboundport and calls the Controller OutboundPort getBatteryProduction method to get the battery energy production
	 * @param energy the intensity of energy send by the battery
	 * @throws Exception
	 */
	public void sendEnergy(double energy) throws Exception;
	public void receivePower(double power) throws Exception;

}
