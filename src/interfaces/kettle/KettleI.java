package interfaces.kettle;

import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataRequiredI;

public interface KettleI extends DataOfferedI,DataRequiredI{

	public void startKettle() throws Exception;
	public void stopKettle() throws Exception;
	public double sendConsumption() throws Exception;
}
