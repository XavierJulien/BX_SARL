package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.BatterieI;
import interfaces.BouilloireI;
import interfaces.CapteurChaleurI;
import interfaces.CapteurVentI;
import interfaces.ChargeurI;
import interfaces.ChauffageI;
import interfaces.CompteurI;
import interfaces.ControleurI;
import interfaces.EolienneI;

public class ControleurConnector extends AbstractConnector implements ControleurI{

	//---------------------------------------------------
	//--------------------EOLIENNE-----------------------
	//---------------------------------------------------
	@Override
	public void startEolienne() throws Exception {
		((EolienneI)this.offering).startEolienne();
	}
	@Override
	public void stopEolienne() throws Exception {
		((EolienneI)this.offering).stopEolienne();
	}
	@Override
	public double getProd() throws Exception {
		return ((EolienneI)this.offering).sendProduction() ;	
	}

	//---------------------------------------------------
	//--------------------BOUILLOIRE---------------------
	//---------------------------------------------------
	@Override
	public void startBouilloire() throws Exception {
		((BouilloireI)this.offering).startBouilloire();
	}
	@Override
	public void stopBouilloire() throws Exception {
		((BouilloireI)this.offering).stopBouilloire();
	}

	//---------------------------------------------------
	//--------------------CHAUFFAGE----------------------
	//---------------------------------------------------
	@Override
	public void startChauffage() throws Exception {
		((ChauffageI)this.offering).startChauffage();
	}
	@Override
	public void stopChauffage() throws Exception {
		((ChauffageI)this.offering).stopChauffage();
	}
	
	//---------------------------------------------------
	//--------------------COMPTEUR-----------------------
	//---------------------------------------------------
	@Override
	public void startCompteur() throws Exception {
		((CompteurI)this.offering).startCompteur();
	}
	@Override
	public void stopCompteur() throws Exception {
		((CompteurI)this.offering).stopCompteur();
	}

	//---------------------------------------------------
	//---------------------CHARGEUR----------------------
	//---------------------------------------------------
	@Override
	public void startChargeur() throws Exception {
		((ChargeurI)this.offering).startChargeur();	
	}
	@Override
	public void stopChargeur() throws Exception {
		((ChargeurI)this.offering).stopChargeur();
	}
	
	//---------------------------------------------------
	//---------------------BATTERIE----------------------
	//---------------------------------------------------
	@Override
	public void startBatterie() throws Exception {
		((BatterieI)this.offering).startBatterie();
	}
	@Override
	public void stopBatterie() throws Exception {
		((BatterieI)this.offering).stopBatterie();
	}
	@Override
	public double getBatteryChargePercentage() throws Exception {
		return ((BatterieI)this.offering).sendChargePercentage();
	}
	@Override
	public double getBatteryProduction() throws Exception {
		return ((BatterieI)this.offering).sendEnergy();
	}
	
	//---------------------------------------------------
	//--------------------CAPTEUR------------------------
	//---------------------------------------------------
	@Override
	public double getVent() throws Exception {
		return ((CapteurVentI)this.offering).sendWind();
	}
	@Override
	public double getChaleur() throws Exception {
		return ((CapteurChaleurI)this.offering).sendChaleur();
	}
}