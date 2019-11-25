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

	@Override
	public double sendAllConso() throws Exception {
		return ((ControleurI)this.offering).getAllConso();
	}

	@Override
	public double getChauffageConso() throws Exception {
		return ((CompteurI)this.offering).getChauffageConso();
	}

	@Override
	public double getBouilloireConso() throws Exception {
		return ((CompteurI)this.offering).getBouilloireConso();
	}

	@Override
	public double getChargeurConso() throws Exception {
		return ((CompteurI)this.offering).getChargeurConso();
	}
	
}