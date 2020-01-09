package simulation.models.windSensor;


import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.simulators.SimulationEngine;

public class			MIL_WindSensor
{
	public static void	main(String[] args)
	{
		SimulationEngine se ;

		try {
			Architecture localArchitecture = WindSensorCoupledModel.build() ;
			se = localArchitecture.constructSimulator() ;
			se.setDebugLevel(0) ;
			//System.out.println(se.simulatorAsString()) ;
			SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 10L ;
			se.doStandAloneSimulation(0.0, 5000.0) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
}
//-----------------------------------------------------------------------------
