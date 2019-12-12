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
	public double sendAllConsumption() throws Exception {
		return ((ElectricMeterI)this.connector).sendAllConsumption();		
	}

	@Override
	public double getHeatingConsumption() throws Exception {
		return ((ElectricMeterI)this.connector).getHeatingConsumption();		
	}

	@Override
	public double getKettleConsumption() throws Exception {
		return ((ElectricMeterI)this.connector).getKettleConsumption();	
	}

	@Override
	public double getChargerConsumption() throws Exception {
		return ((ElectricMeterI)this.connector).getChargerConsumption();	
	}

}
