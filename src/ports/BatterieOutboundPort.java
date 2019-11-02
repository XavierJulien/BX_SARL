package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.BatterieI;

public class BatterieOutboundPort extends AbstractOutboundPort implements BatterieI {
	
	private static final long serialVersionUID = 1L;

	public BatterieOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, BatterieI.class, owner);
	}
	
	public BatterieOutboundPort(ComponentI owner) throws Exception {
		super(BatterieI.class, owner);
	}

	@Override
	public void startBatterie() throws Exception {
		((BatterieI)this.connector).startBatterie() ;
	}

	@Override
	public void stopBatterie() throws Exception {
		((BatterieI)this.connector).stopBatterie() ;
	}

	@Override
	public double sendChargePercentage() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double sendEnergy() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}


	
	
	
}
