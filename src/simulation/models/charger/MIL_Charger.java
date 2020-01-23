package simulation.models.charger;

import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.simulators.SimulationEngine;

public class MIL_Charger {
	public static void	main(String[] args){
		SimulationEngine se ;
		try {
			Architecture localArchitecture = ChargerCoupledModel.build() ;
			se = localArchitecture.constructSimulator() ;
			SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 0L ;
			se.doStandAloneSimulation(0.0, 500.0) ;
		} catch (Exception e) {throw new RuntimeException(e);}
	}
}
