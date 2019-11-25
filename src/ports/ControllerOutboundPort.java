package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ControllerI;

public class ControllerOutboundPort extends AbstractOutboundPort implements	ControllerI {
	
	private static final long serialVersionUID = 1L;

	public ControllerOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ControllerI.class, owner);
	}
	
	public ControllerOutboundPort(ComponentI owner) throws Exception {
		super(ControllerI.class, owner);
	}

	@Override
	public void startWindTurbine() throws Exception {
		((ControllerI)this.connector).startWindTurbine() ;
	}

	@Override
	public void stopWindTurbine() throws Exception {
		((ControllerI)this.connector).stopWindTurbine() ;
	}

	@Override
	public double getProduction() throws Exception {
		return ((ControllerI)this.connector).getProduction();
		
	}

	@Override
	public double getWind() throws Exception {
		return ((ControllerI)this.connector).getWind();
	}
	
	@Override
	public double getTemperature() throws Exception {
		return ((ControllerI)this.connector).getTemperature();
	}

	@Override
	public void startKettle() throws Exception {
		((ControllerI)this.connector).startKettle() ;		
	}

	@Override
	public void stopKettle() throws Exception {
		((ControllerI)this.connector).stopKettle() ;		
	}

	@Override
	public void startHeating() throws Exception {
		((ControllerI)this.connector).startHeating() ;		
	}

	@Override
	public void stopHeating() throws Exception {
		((ControllerI)this.connector).stopHeating() ;		
	}

	@Override
	public void startElectricMeter() throws Exception {
		((ControllerI)this.connector).startElectricMeter() ;		
	}

	@Override
	public void stopElectricMeter() throws Exception {
		((ControllerI)this.connector).stopElectricMeter() ;		
	}
	
	@Override
	public void startCharger() throws Exception {
		((ControllerI)this.connector).startCharger() ;				
	}

	@Override
	public void stopCharger() throws Exception {
		((ControllerI)this.connector).stopCharger() ;						
	}

	@Override
	public void startBattery() throws Exception {
		((ControllerI)this.connector).startBattery() ;				
	}

	@Override
	public void stopBattery() throws Exception {
		((ControllerI)this.connector).stopBattery() ;			
	}

	@Override
	public double getBatteryChargePercentage() throws Exception {
		return ((ControllerI)this.connector).getBatteryChargePercentage();
	}

	@Override
	public double getBatteryProduction() throws Exception {
		return ((ControllerI)this.connector).getBatteryProduction();
	}

	@Override
	public double getAllConsumption() throws Exception {
		return ((ControllerI)this.connector).getAllConsumption();			
	}
		
}
