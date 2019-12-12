package interfaces.electricmeter;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface ElectricMeterI extends DataOfferedI,DataRequiredI{

	public void startElectricMeter() throws Exception;
	public void stopElectricMeter() throws Exception;
	public double sendAllConsumption() throws Exception;
	public double getHeatingConsumption() throws Exception;
	public double getKettleConsumption() throws Exception;
	public double getChargerConsumption() throws Exception;

}
