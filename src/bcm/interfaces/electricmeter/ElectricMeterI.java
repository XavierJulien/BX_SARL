package bcm.interfaces.electricmeter;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface ElectricMeterI extends DataOfferedI,DataRequiredI{

	public void startElectricMeter() throws Exception;
	public void stopElectricMeter() throws Exception;
	public void sendAllConsumption(double total) throws Exception;
	public void getHeatingConsumption(double consumption) throws Exception;
	public void getKettleConsumption(double consumption) throws Exception;
	public void getChargerConsumption(double consumption) throws Exception;

}
