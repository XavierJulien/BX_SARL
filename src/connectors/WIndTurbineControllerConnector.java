package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ControllerI;
import interfaces.WindTurbineI;

public class WIndTurbineControllerConnector extends AbstractConnector implements WindTurbineI{
 
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
	public double sendProduction() throws Exception {
		return ((WindTurbineI)this.offering).sendProduction();
	}
}