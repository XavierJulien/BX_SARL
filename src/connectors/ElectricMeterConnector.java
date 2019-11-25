package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.KettleElectricMeterI;
import interfaces.ChargerElectricMeterI;
import interfaces.HeatingElectricMeterI;
import interfaces.ElectricMeterI;

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
	public double sendAllConsumption() throws Exception {
		return ((ElectricMeterI)this.offering).sendAllConsumption();
	}

	//---------------------------------------------------
	//--------------------BOUILLOIRE---------------------
	//---------------------------------------------------

	@Override
	public double getKettleConsumption() throws Exception {
		return ((KettleElectricMeterI)this.offering).sendConsumption();
	}
	
	//---------------------------------------------------
	//--------------------CHAUFFAGE----------------------
	//---------------------------------------------------

	@Override
	public double getHeatingConsumption() throws Exception {
		return ((HeatingElectricMeterI)this.offering).sendConsumption();
	}

	//---------------------------------------------------
	//---------------------CHARGEUR----------------------
	//---------------------------------------------------

	@Override
	public double getChargerConsumption() throws Exception {
		return ((ChargerElectricMeterI)this.offering).sendConsumption();
	}
}