package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ChargeurI;
import interfaces.ControleurI;
public class ChargeurControleurConnector extends AbstractConnector implements ChargeurI{

	@Override
	public void startChargeur() throws Exception {
		((ControleurI)this.offering).startBouilloire();
	}

	@Override
	public void stopChargeur() throws Exception {
		((ControleurI)this.offering).stopBouilloire();
	}

	@Override
	public double sendConso() throws Exception {
		//shouldn't be used
		return 0;
	}	
}
