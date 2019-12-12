package interfaces.controller;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

public interface ControllerI extends OfferedI,RequiredI{

	//------------EOLIENNE------------
	public void startWindTurbine() throws Exception;
	public void stopWindTurbine() throws Exception;
	public void getProduction(double production) throws Exception;
	
	//------------BOUILLOIRE------------
	public void startKettle() throws Exception;
	public void stopKettle() throws Exception;
	
	//------------CHAUFFAGE------------
	public void startHeating() throws Exception;
	public void stopHeating() throws Exception;
	public void putExtraPowerInHeating(int power) throws Exception;
	public void slowHeating(int power) throws Exception;
	
	//------------COMPTEUR------------
	public void startElectricMeter() throws Exception;
	public void stopElectricMeter() throws Exception;
	public void getAllConsumption(double total) throws Exception;
	
	//------------CHARGEUR------------
	public void startCharger() throws Exception;
	public void stopCharger() throws Exception;
	
	//------------BATTERIE------------
	public void startBattery() throws Exception;
	public void stopBattery() throws Exception;
	public void getBatteryChargePercentage(double percentage) throws Exception;
	public void getBatteryProduction(double energy) throws Exception;
	
	//------------CAPTEURS------------
	public double getWind() throws Exception;
	public void getTemperature(double temperature) throws Exception;
	
	
}
