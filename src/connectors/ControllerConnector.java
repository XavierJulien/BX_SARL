package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.BatteryI;
import interfaces.KettleI;
import interfaces.TemperatureSensorI;
import interfaces.WindSensorI;
import interfaces.ChargerI;
import interfaces.HeatingI;
import interfaces.ElectricMeterI;
import interfaces.ControllerI;
import interfaces.WindTurbineI;

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
	public double getProduction() throws Exception {
		return ((WindTurbineI)this.offering).sendProduction() ;	
	}

	//---------------------------------------------------
	//--------------------BOUILLOIRE---------------------
	//---------------------------------------------------
	@Override
	public void startKettle() throws Exception {
		((KettleI)this.offering).startKettle();
	}
	@Override
	public void stopKettle() throws Exception {
		((KettleI)this.offering).stopKettle();
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
	public double getAllConsumption() throws Exception {
		return ((ElectricMeterI)this.offering).sendAllConsumption();
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
	public double getBatteryChargePercentage() throws Exception {
		return ((BatteryI)this.offering).sendChargePercentage();
	}
	@Override
	public double getBatteryProduction() throws Exception {
		return ((BatteryI)this.offering).sendEnergy();
	}
	
	//---------------------------------------------------
	//--------------------CAPTEUR------------------------
	//---------------------------------------------------
	@Override
	public double getWind() throws Exception {
		return ((WindSensorI)this.offering).sendWind();
	}
	@Override
	public double getTemperature() throws Exception {
		return ((TemperatureSensorI)this.offering).sendTemperature();
	}



}