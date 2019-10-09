package data;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public class BatterieD implements DataOfferedI.DataI,DataRequiredI.DataI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	double taux_de_charge;
	double charge_max;
	double limite_utilisation;
	
	//message recurrent
	public BatterieD(double taux_de_charge) {
		this.taux_de_charge = taux_de_charge;
		this.charge_max = -1.;
		this.limite_utilisation = -1.;
	}
	
	//premier message
	public BatterieD(double taux_de_charge,double charge_max, double limite_utilisation) {
		this.taux_de_charge = taux_de_charge;
		this.charge_max = charge_max;
		this.limite_utilisation = limite_utilisation;
	}
}
