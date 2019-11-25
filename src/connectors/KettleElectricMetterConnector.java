package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.BouilloireCompteurI;
import interfaces.CompteurI;

public class BouilloireCompteurConnector extends AbstractConnector implements BouilloireCompteurI{

	@Override
	public double sendConso() throws Exception {
		return ((CompteurI)this.offering).getBouilloireConso();
	}

}