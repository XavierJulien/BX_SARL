package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.CapteurChaleurToChauffageI;
import interfaces.ChauffageToCapteurChaleurI;

public class CapteurChauffageConnector extends AbstractConnector implements CapteurChaleurToChauffageI {

	@Override
	public double getHeating() throws Exception {
		return ((ChauffageToCapteurChaleurI)this.offering).sendHeating() ;
	}

}
