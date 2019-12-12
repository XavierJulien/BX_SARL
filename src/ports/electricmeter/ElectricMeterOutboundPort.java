package ports.electricmeter;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.electricmeter.ElectricMeterI;

public class ElectricMeterOutboundPort extends AbstractOutboundPort implements ElectricMeterI{
	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public ElectricMeterOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ElectricMeterI.class, owner);
	}
	
	public ElectricMeterOutboundPort(ComponentI owner) throws Exception {
		super(ElectricMeterI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	
	@Override
	public void startElectricMeter() throws Exception {
		((ElectricMeterI)this.connector).startElectricMeter();		

	}

	@Override
	public void stopElectricMeter() throws Exception {
		((ElectricMeterI)this.connector).stopElectricMeter();		

	}

	@Override
	public void sendAllConsumption(double total) throws Exception {
		((ElectricMeterI)this.connector).sendAllConsumption(total);		
	}

	@Override
	public void getHeatingConsumption(double consumption) throws Exception {
		((ElectricMeterI)this.connector).getHeatingConsumption(consumption);		
	}

	@Override
	public void getKettleConsumption(double consumption) throws Exception {
		((ElectricMeterI)this.connector).getKettleConsumption(consumption);	
	}

	@Override
	public void getChargerConsumption(double consumption) throws Exception {
		((ElectricMeterI)this.connector).getChargerConsumption(consumption);	
	}

}
