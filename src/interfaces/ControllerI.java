package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

public interface ControllerI extends OfferedI,RequiredI{

	//------------EOLIENNE------------
	public void startWindTurbine() throws Exception;
	public void stopWindTurbine() throws Exception;
	public double getProduction() throws Exception;
	
	//------------BOUILLOIRE------------
	public void startKettle() throws Exception;
	public void stopKettle() throws Exception;
	
	//------------CHAUFFAGE------------
	public void startHeating() throws Exception;
	public void stopHeating() throws Exception;
	
	//------------COMPTEUR------------
	public void startElectricMeter() throws Exception;
	public void stopElectricMeter() throws Exception;
	public double getAllConsumption() throws Exception;
	
	//------------CHARGEUR------------
	public void startCharger() throws Exception;
	public void stopCharger() throws Exception;
	
	//------------BATTERIE------------
	public void startBattery() throws Exception;
	public void stopBattery() throws Exception;
	public double getBatteryChargePercentage() throws Exception;
	public double getBatteryProduction() throws Exception;
	
	//------------CAPTEURS------------
	public double getWind() throws Exception;
	public double getTemperature() throws Exception;
	
	
}
