package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ChauffageI;
import interfaces.ControleurI;

public class ChauffageControleurConnector extends AbstractConnector implements ChauffageI{

	@Override
	public void startChauffage() throws Exception {
		((ControleurI)this.offering).startChauffage();
	}

	@Override
	public void stopChauffage() throws Exception {
		((ControleurI)this.offering).stopChauffage();
	}
}