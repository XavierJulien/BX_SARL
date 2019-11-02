package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ControleurI;

public class ControleurOutboundPort extends AbstractOutboundPort implements	ControleurI {
	
	private static final long serialVersionUID = 1L;

	public ControleurOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ControleurI.class, owner);
	}
	
	public ControleurOutboundPort(ComponentI owner) throws Exception {
		super(ControleurI.class, owner);
	}

	@Override
	public void startEolienne() throws Exception {
		((ControleurI)this.connector).startEolienne() ;
	}

	@Override
	public void stopEolienne() throws Exception {
		((ControleurI)this.connector).stopEolienne() ;
	}

	@Override
	public double getProd() throws Exception {
		return ((ControleurI)this.connector).getProd();
		
	}

	@Override
	public double getVent() throws Exception {
		return ((ControleurI)this.connector).getVent();
	}

	@Override
	public void startBouilloire() throws Exception {
		((ControleurI)this.connector).startBouilloire() ;		
	}

	@Override
	public void stopBouilloire() throws Exception {
		((ControleurI)this.connector).stopBouilloire() ;		
	}

	@Override
	public void startChauffage() throws Exception {
		((ControleurI)this.connector).startChauffage() ;		
	}

	@Override
	public void stopChauffage() throws Exception {
		((ControleurI)this.connector).stopChauffage() ;		
	}

	@Override
	public void startChargeur() throws Exception {
		((ControleurI)this.connector).startChargeur() ;				
	}

	@Override
	public void stopChargeur() throws Exception {
		((ControleurI)this.connector).stopChargeur() ;						
	}

	@Override
	public void startBatterie() throws Exception {
		((ControleurI)this.connector).startBatterie() ;				
	}

	@Override
	public void stopBatterie() throws Exception {
		((ControleurI)this.connector).stopBatterie() ;			
	}

	@Override
	public double getBatteryChargePercentage() throws Exception {
		return ((ControleurI)this.connector).getBatteryChargePercentage();
	}

	@Override
	public double getBatteryProduction() throws Exception {
		return ((ControleurI)this.connector).getBatteryProduction();
	}
	
	
	
	
	
}
