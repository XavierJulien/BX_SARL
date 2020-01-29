package bcm.interfaces.heating;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

/**
 * This class defines the interface HeatingI used in the Heating behaviour
 * @author Julien Xavier et Alexis Belanger
 *
 */
public interface HeatingI extends DataOfferedI,DataRequiredI{

	/**
	 * this method is called to start the heating
	 * @throws Exception
	 */
	public void startHeating() throws Exception;
	
	/**
	 * this method is called to stop the heating
	 * @throws Exception
	 */
	public void stopHeating() throws Exception;
	
	/**
	 * this method is called to up the heating power
	 * @param power the percentage of augmentation
	 * @throws Exception
	 */
	public void putExtraPowerInHeating(int power) throws Exception;
	
	/**
	 * this method is called to slow the heating power
	 * @param power the percentage of decrease
	 * @throws Exception
	 */
	public void slowHeating(int power) throws Exception;
}
