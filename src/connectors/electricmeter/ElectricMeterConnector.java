package connectors.electricmeter;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.charger.ChargerElectricMeterI;
import interfaces.electricmeter.ElectricMeterI;
import interfaces.heating.HeatingElectricMeterI;
import interfaces.kettle.KettleElectricMeterI;

public class ElectricMeterConnector extends AbstractConnector implements ElectricMeterI{


	@Override
	public void startElectricMeter() throws Exception {
		((ElectricMeterI)this.offering).startElectricMeter();
	}

	@Override
	public void stopElectricMeter() throws Exception {
		((ElectricMeterI)this.offering).stopElectricMeter();
	}
	
	@Override
	public void sendAllConsumption(double total) throws Exception {
		((ElectricMeterI)this.offering).sendAllConsumption(total);
	}

	//---------------------------------------------------
	//--------------------BOUILLOIRE---------------------
	//---------------------------------------------------

	@Override
	public void getKettleConsumption(double consumption) throws Exception {
		((KettleElectricMeterI)this.offering).sendConsumption(consumption);
	}
	
	//---------------------------------------------------
	//--------------------CHAUFFAGE----------------------
	//---------------------------------------------------

	@Override
	public void getHeatingConsumption(double consumption) throws Exception {
		((HeatingElectricMeterI)this.offering).sendConsumption(consumption);
	}

	//---------------------------------------------------
	//---------------------CHARGEUR----------------------
	//---------------------------------------------------

	@Override
	public void getChargerConsumption(double consumption) throws Exception {
		((ChargerElectricMeterI)this.offering).sendConsumption(consumption);
	}
}