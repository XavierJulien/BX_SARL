package interfaces;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface BatterieI extends DataOfferedI,DataRequiredI{
	
	public void startBatterie() throws Exception;
	public void stopBatterie() throws Exception;
	
	
	public double sendChargePercentage() throws Exception;
	
	public double sendEnergy() throws Exception;

}
