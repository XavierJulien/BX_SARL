package bcm.connectors.controller;

import bcm.interfaces.battery.BatteryI;
import bcm.interfaces.charger.ChargerI;
import bcm.interfaces.controller.ControllerI;
import bcm.interfaces.electricmeter.ElectricMeterI;
import bcm.interfaces.heating.HeatingI;
import bcm.interfaces.windturbine.WindTurbineI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ControllerConnector extends AbstractConnector implements ControllerI{

	//---------------------------------------------------
	//--------------------EOLIENNE-----------------------
	//---------------------------------------------------
	@Override
	public void startWindTurbine() throws Exception {
		((WindTurbineI)this.offering).startWindTurbine();
	}
	@Override
	public void stopWindTurbine() throws Exception {
		((WindTurbineI)this.offering).stopWindTurbine();
	}
	@Override
	public void getProduction(double production) throws Exception {
		((WindTurbineI)this.offering).sendProduction(production) ;	
	}


	//---------------------------------------------------
	//--------------------CHAUFFAGE----------------------
	//---------------------------------------------------
	@Override
	public void startHeating() throws Exception {
		((HeatingI)this.offering).startHeating();
	}
	@Override
	public void stopHeating() throws Exception {
		((HeatingI)this.offering).stopHeating();
	}
	@Override
	public void putExtraPowerInHeating(int power) throws Exception {
		((HeatingI)this.offering).putExtraPowerInHeating(power);
	}
	@Override
	public void slowHeating(int power) throws Exception {
		((HeatingI)this.offering).slowHeating(power);		
	}
		
	//---------------------------------------------------
	//--------------------COMPTEUR-----------------------
	//---------------------------------------------------
	@Override
	public void startElectricMeter() throws Exception {
		((ElectricMeterI)this.offering).startElectricMeter();
	}
	@Override
	public void stopElectricMeter() throws Exception {
		((ElectricMeterI)this.offering).stopElectricMeter();
	}
	@Override
	public void getAllConsumption(double total) throws Exception {
		((ElectricMeterI)this.offering).sendAllConsumption(total);
	}
	
	//---------------------------------------------------
	//---------------------CHARGEUR----------------------
	//---------------------------------------------------
	@Override
	public void startCharger() throws Exception {
		((ChargerI)this.offering).startCharger();	
	}
	@Override
	public void stopCharger() throws Exception {
		((ChargerI)this.offering).stopCharger();
	}
	
	//---------------------------------------------------
	//---------------------BATTERIE----------------------
	//---------------------------------------------------
	@Override
	public void startBattery() throws Exception {
		((BatteryI)this.offering).startBattery();
	}
	@Override
	public void stopBattery() throws Exception {
		((BatteryI)this.offering).stopBattery();
	}
	@Override
	public void getBatteryChargePercentage(double percentage) throws Exception {
		((BatteryI)this.offering).sendChargePercentage(percentage);
	}
	@Override
	public void getBatteryProduction(double energy) throws Exception {
		((BatteryI)this.offering).sendEnergy(energy);
	}
	
	//---------------------------------------------------
	//--------------------CAPTEUR------------------------
	//---------------------------------------------------
	@Override
	public void getWindSpeed(double speed) throws Exception {
		//unused
	}
	@Override
	public void getTemperature(double temperature) throws Exception {
		//unused
	}





}