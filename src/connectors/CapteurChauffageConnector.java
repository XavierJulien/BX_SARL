package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.CapteurChaleurChauffageI;
import interfaces.ChauffageCapteurChaleurI;

public class CapteurChauffageConnector extends AbstractConnector implements CapteurChaleurChauffageI {

	@Override
	public double getHeating() throws Exception {
		return ((ChauffageCapteurChaleurI)this.offering).sendHeating() ;
	}

}
