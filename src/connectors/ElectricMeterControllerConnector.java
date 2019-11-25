package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ElectricMeterI;
import interfaces.ControllerI;

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
	public double sendAllConsumption() throws Exception {
		return ((ControllerI)this.offering).getAllConsumption();
	}

	@Override
	public double getHeatingConsumption() throws Exception {
		return ((ElectricMeterI)this.offering).getHeatingConsumption();
	}

	@Override
	public double getKettleConsumption() throws Exception {
		return ((ElectricMeterI)this.offering).getKettleConsumption();
	}

	@Override
	public double getChargerConsumption() throws Exception {
		return ((ElectricMeterI)this.offering).getChargerConsumption();
	}
	
}