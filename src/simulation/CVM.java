package simulation;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.devs_simulation.simulators.SimulationEngine;
import simulation.components.battery.Battery;

public class			CVM
extends		AbstractCVM
{
	public				CVM() throws Exception
	{
		super() ;
		SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 10L ;
	}

	@Override
	public void			deploy() throws Exception
	{
		
		@SuppressWarnings("unused")
		String windTurbineURI =
				AbstractComponent.createComponent(
						Battery.class.getCanonicalName(),
						new Object[]{}) ;

		super.deploy();
	}

	public static void	main(String[] args)
	{
		try {
			CVM c = new CVM();
			c.startStandardLifeCycle(50000L) ;
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
}
//------------------------------------------------------------------------------
