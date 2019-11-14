package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ChauffageCompteurI;
import interfaces.CompteurI;

public class ChauffageCompteurConnector extends AbstractConnector implements ChauffageCompteurI{

	@Override
	public double sendConso() throws Exception {
		return ((CompteurI)this.offering).getChauffageConso();
	}

}