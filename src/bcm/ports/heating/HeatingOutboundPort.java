package bcm.ports.heating;

import bcm.interfaces.heating.HeatingElectricMeterI;
import bcm.interfaces.heating.HeatingI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class HeatingOutboundPort extends AbstractOutboundPort implements HeatingI, HeatingElectricMeterI{
	private static final long serialVersionUID = 1L;

//--------------------------------------------------------------
//-------------------------CONSTRUCTORS-------------------------
//--------------------------------------------------------------
	public HeatingOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, HeatingI.class, owner);
	}
	
	public HeatingOutboundPort(ComponentI owner) throws Exception {
		super(HeatingI.class, owner);
	}

//--------------------------------------------------------------
//-------------------------SERVICES-----------------------------
//--------------------------------------------------------------
	@Override
	public void startHeating() throws Exception {
		((HeatingI)this.connector).startHeating() ;
	}

	@Override
	public void stopHeating() throws Exception {
		((HeatingI)this.connector).stopHeating() ;
	}

	@Override
	public void putExtraPowerInHeating(int power) throws Exception {
		//Shouldn't be used
	}

	@Override
	public void slowHeating(int power) throws Exception {
		//Shouldn't be used
	}

	@Override
	public void sendConsumption(double consumption) throws Exception {
		((HeatingElectricMeterI)this.connector).sendConsumption(consumption) ;
	}

	
}
