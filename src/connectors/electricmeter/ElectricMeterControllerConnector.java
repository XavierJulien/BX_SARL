package connectors.electricmeter;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.controller.ControllerI;
import interfaces.electricmeter.ElectricMeterI;

public class ElectricMeterControllerConnector extends AbstractConnector implements ElectricMeterI{

	@Override
	public void startElectricMeter() throws Exception {
		((ControllerI)this.offering).startElectricMeter();
	}

	@Override
	public void stopElectricMeter() throws Exception {
		((ControllerI)this.offering).stopElectricMeter();
	}

	@Override
	public void sendAllConsumption(double total) throws Exception {
		((ControllerI)this.offering).getAllConsumption(total);
	}

	@Override
	public void getHeatingConsumption(double consumption) throws Exception {
		((ElectricMeterI)this.offering).getHeatingConsumption(consumption);
	}

	@Override
	public void getKettleConsumption(double consumption) throws Exception {
		((ElectricMeterI)this.offering).getKettleConsumption(consumption);
	}

	@Override
	public void getChargerConsumption(double consumption) throws Exception {
		((ElectricMeterI)this.offering).getChargerConsumption(consumption);
	}
	
}