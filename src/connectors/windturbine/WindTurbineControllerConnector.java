package connectors.windturbine;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.controller.ControllerI;
import interfaces.windturbine.WindTurbineI;

public class WindTurbineControllerConnector extends AbstractConnector implements WindTurbineI{
 
	@Override
	public void startWindTurbine() throws Exception {
		((ControllerI)this.offering).startWindTurbine();
	}

	@Override
	public void stopWindTurbine() throws Exception {
		((ControllerI)this.offering).stopWindTurbine();
	}

	//not used
	@Override
	public void sendProduction(double production) throws Exception {
		((ControllerI)this.offering).getProduction(production);
	}

	@Override
	public void getWindSpeed(double speed) throws Exception {
		//unused
		
	}
}