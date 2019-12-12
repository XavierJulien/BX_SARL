package interfaces.electricmeter;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface ElectricMeterControllerI extends DataOfferedI,DataRequiredI{

	public void sendConso(double conso) throws Exception;

}
