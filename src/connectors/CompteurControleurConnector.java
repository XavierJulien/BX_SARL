package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.CompteurI;
import interfaces.ControleurI;

public class CompteurControleurConnector extends AbstractConnector implements CompteurI{

	@Override
	public void startCompteur() throws Exception {
		((ControleurI)this.offering).startCompteur();
	}

	@Override
	public void stopCompteur() throws Exception {
		((ControleurI)this.offering).stopCompteur();
	}
}