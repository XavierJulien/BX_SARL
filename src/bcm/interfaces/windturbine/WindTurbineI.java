package bcm.interfaces.windturbine;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

public interface WindTurbineI extends OfferedI,RequiredI{
	
	/**
	 * start the wind turbine component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @throws Exception		<i>todo.</i>
	 */
	public void startWindTurbine() throws Exception;
	
	/**
	 * stop the wind turbine component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @throws Exception		<i>todo.</i>
	 */
	public void stopWindTurbine() throws Exception;
	
	/**
	 * send <strong>production</strong> energy to the controller component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	b != null
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param production	the amount of energy send.
	 * @throws Exception		<i>todo.</i>
	 */
	public void sendProduction(double production) throws Exception;
	
	/**
	 * receive the <strong>speed</strong> of the wind from the wind sensor component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	speed != null
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param speed 		the amount of wind speed received.
	 * @throws Exception		<i>todo.</i>
	 */
	public void getWindSpeed(double speed) throws Exception;
}
