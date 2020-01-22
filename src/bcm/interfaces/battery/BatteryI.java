package bcm.interfaces.battery;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface BatteryI extends DataOfferedI,DataRequiredI{
	
	public void startBattery() throws Exception;
	public void stopBattery() throws Exception;
	public void sendChargePercentage(double percentage) throws Exception;
	public void sendEnergy(double energy) throws Exception;
	public void receivePower(double power) throws Exception;

}
