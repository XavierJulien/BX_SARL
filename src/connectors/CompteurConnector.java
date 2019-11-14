package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.BouilloireCompteurI;
import interfaces.ChargeurCompteurI;
import interfaces.ChauffageCompteurI;
import interfaces.CompteurI;

public class CompteurConnector extends AbstractConnector implements CompteurI{


	@Override
	public void startCompteur() throws Exception {
		((CompteurI)this.offering).startCompteur();
	}

	@Override
	public void stopCompteur() throws Exception {
		((CompteurI)this.offering).stopCompteur();
	}
	
	@Override
	public double sendAllConso() throws Exception {
		return ((CompteurI)this.offering).sendAllConso();
	}

	//---------------------------------------------------
	//--------------------BOUILLOIRE---------------------
	//---------------------------------------------------

	@Override
	public double getBouilloireConso() throws Exception {
		return ((BouilloireCompteurI)this.offering).sendConso();
	}
	
	//---------------------------------------------------
	//--------------------CHAUFFAGE----------------------
	//---------------------------------------------------

	@Override
	public double getChauffageConso() throws Exception {
		return ((ChauffageCompteurI)this.offering).sendConso();
	}

	//---------------------------------------------------
	//---------------------CHARGEUR----------------------
	//---------------------------------------------------

	@Override
	public double getChargeurConso() throws Exception {
		return ((ChargeurCompteurI)this.offering).sendConso();
	}
}