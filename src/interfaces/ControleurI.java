package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

public interface ControleurI extends OfferedI,RequiredI{

	//------------EOLIENNE------------
	public void startEolienne() throws Exception;
	public void stopEolienne() throws Exception;
	public double getProd() throws Exception;
	
	//------------BOUILLOIRE------------
	public void startBouilloire() throws Exception;
	public void stopBouilloire() throws Exception;
	
	//------------CHAUFFAGE------------
	public void startChauffage() throws Exception;
	public void stopChauffage() throws Exception;
	
	//------------COMPTEUR------------
	public void startCompteur() throws Exception;
	public void stopCompteur() throws Exception;
	
	//------------CHARGEUR------------
	public void startChargeur() throws Exception;
	public void stopChargeur() throws Exception;
	
	//------------BATTERIE------------
	public void startBatterie() throws Exception;
	public void stopBatterie() throws Exception;
	public double getBatteryChargePercentage() throws Exception;
	public double getBatteryProduction() throws Exception;
	
	//------------CAPTEURS------------
	public double getVent() throws Exception;
	public double getChaleur() throws Exception;
	
	
}
