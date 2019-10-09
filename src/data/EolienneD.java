package data;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public class EolienneD implements DataOfferedI.DataI,DataRequiredI.DataI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int watt_genere;
	boolean stop;
	
	public EolienneD(int watt_genere) {
		this.watt_genere = watt_genere;
		this.stop = false;
	}
	
	public EolienneD(boolean stop) {
		this.watt_genere = 0;
		this.stop = true;
	}
	
	
}
