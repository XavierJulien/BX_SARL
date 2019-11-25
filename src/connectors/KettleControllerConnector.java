package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.BouilloireI;
import interfaces.ControleurI;

public class BouilloireControleurConnector extends	AbstractConnector implements BouilloireI{

	@Override
	public void startBouilloire() throws Exception {
		((ControleurI)this.offering).startBouilloire();
	}

	@Override
	public void stopBouilloire() throws Exception {
		((ControleurI)this.offering).stopBouilloire();
	}

	@Override
	public double sendConso() throws Exception {
		return 0;
	}
}
