package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.BatterieI;
import interfaces.ControleurI;

public class BatterieControleurConnector extends AbstractConnector implements BatterieI {

	@Override
	public void startBatterie() throws Exception {
		((ControleurI)this.offering).startBatterie();
	}
 
	@Override
	public void stopBatterie() throws Exception {
		((ControleurI)this.offering).stopBatterie();
	}

	@Override
	public double sendChargePercentage() throws Exception {
		return 0;
	}

	@Override
	public double sendEnergy() throws Exception {
		return 0;
	}
}