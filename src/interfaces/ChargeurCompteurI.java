package interfaces;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface ChargeurCompteurI extends DataOfferedI,DataRequiredI{

	public double sendConso() throws Exception;
}
