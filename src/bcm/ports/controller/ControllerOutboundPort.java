package bcm.ports.controller;

import bcm.interfaces.controller.ControllerI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

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
	public void getProduction(double production) throws Exception {
		((ControllerI)this.connector).getProduction(production);
		
	}

	@Override
	public void getWindSpeed(double speed) throws Exception {
		((ControllerI)this.connector).getWindSpeed(speed);
	}
	
	@Override
	public void getTemperature(double temperature) throws Exception {
		((ControllerI)this.connector).getTemperature(temperature);
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
	public void putExtraPowerInHeating(int power) throws Exception {
		((ControllerI)this.connector).putExtraPowerInHeating(power);
	}
	
	@Override
	public void slowHeating(int power) throws Exception {
		((ControllerI)this.connector).slowHeating(power);		
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
	public void getBatteryChargePercentage(double percentage) throws Exception {
		((ControllerI)this.connector).getBatteryChargePercentage(percentage);
	}

	@Override
	public void getBatteryProduction(double energy) throws Exception {
		 ((ControllerI)this.connector).getBatteryProduction(energy);
	}

	@Override
	public void getAllConsumption(double total) throws Exception {
		((ControllerI)this.connector).getAllConsumption(total);			
	}




		
}
