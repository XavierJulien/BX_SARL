package interfaces;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface CompteurControleurI extends DataOfferedI,DataRequiredI{

	public void sendConso(double conso) throws Exception;

}
