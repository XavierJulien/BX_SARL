package interfaces;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface CompteurI extends DataOfferedI,DataRequiredI{

	public void startCompteur() throws Exception;
	public void stopCompteur() throws Exception;
	public double sendAllConso() throws Exception;
	public double getChauffageConso() throws Exception;
	public double getBouilloireConso() throws Exception;
	public double getChargeurConso() throws Exception;

}
