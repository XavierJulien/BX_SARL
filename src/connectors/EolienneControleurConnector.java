package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ControleurI;
import interfaces.EolienneI;

public class EolienneControleurConnector extends AbstractConnector implements EolienneI{
 
	@Override
	public void startEolienne() throws Exception {
		((ControleurI)this.offering).startEolienne();
	}

	@Override
	public void stopEolienne() throws Exception {
		((ControleurI)this.offering).stopEolienne();
	}

	@Override
	public double sendProduction() throws Exception {
		return 0;
	}
}