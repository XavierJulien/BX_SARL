package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ChargeurCompteurI;
import interfaces.CompteurI;

public class ChargeurCompteurConnector extends AbstractConnector implements ChargeurCompteurI{

	@Override
	public double sendConso() throws Exception {
		return ((CompteurI)this.offering).getChargeurConso();
	}

}