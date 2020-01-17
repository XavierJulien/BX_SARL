package ports.charger;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.charger.ChargerBatteryI;
import interfaces.charger.ChargerElectricMeterI;
import interfaces.charger.ChargerI;

public class ChargerOutboundPort extends AbstractOutboundPort implements ChargerI{

	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public ChargerOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri,ChargerI.class, owner);
	}
	
	public ChargerOutboundPort(ComponentI owner) throws Exception {
		super(ChargerI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	@Override
	public void startCharger() throws Exception {
		((ChargerI)this.connector).startCharger() ;
		
	}

	@Override
	public void stopCharger() throws Exception {
		((ChargerI)this.connector).stopCharger() ;		
	}

	@Override
	public void sendConsumption(double consumption) throws Exception {
		((ChargerElectricMeterI)this.connector).sendConsumption(consumption) ;
	}

	@Override
	public void sendPower(double power) throws Exception {
		((ChargerBatteryI)this.connector).sendPower(power) ;
	}
}
