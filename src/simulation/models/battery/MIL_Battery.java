package simulation.models.battery;

import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.simulators.SimulationEngine;

public class MIL_Battery {
	public static void	main(String[] args){
		SimulationEngine se ;

		try {
			Architecture localArchitecture = BatteryCoupledModel.build() ;
			se = localArchitecture.constructSimulator() ;
			se.setDebugLevel(0) ;
			SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 500L ;
			se.doStandAloneSimulation(0.0, 50000.0) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
}
