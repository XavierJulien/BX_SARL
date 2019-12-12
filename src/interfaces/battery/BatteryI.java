package interfaces.battery;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface BatteryI extends DataOfferedI,DataRequiredI{
	
	public void startBattery() throws Exception;
	public void stopBattery() throws Exception;
	public double sendChargePercentage() throws Exception;
	public double sendEnergy() throws Exception;

}
