package connectors;

//Copyright Jacques Malenfant, Sorbonne Universite.
//
//Jacques.Malenfant@lip6.fr
//
//This software is a computer program whose purpose is to provide a
//basic component programming model to program with components
//distributed applications in the Java programming language.
//
//This software is governed by the CeCILL-C license under French law and
//abiding by the rules of distribution of free software.  You can use,
//modify and/ or redistribute the software under the terms of the
//CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
//URL "http://www.cecill.info".
//
//As a counterpart to the access to the source code and  rights to copy,
//modify and redistribute granted by the license, users are provided only
//with a limited warranty  and the software's author,  the holder of the
//economic rights,  and the successive licensors  have only  limited
//liability. 
//
//In this respect, the user's attention is drawn to the risks associated
//with loading,  using,  modifying and/or developing or reproducing the
//software by the user in light of its specific status of free software,
//that may mean  that it is complicated to manipulate,  and  that  also
//therefore means  that it is reserved for developers  and  experienced
//professionals having in-depth computer knowledge. Users are therefore
//encouraged to load and test the software's suitability as regards their
//requirements in conditions enabling the security of their systems and/or 
//data to be ensured and,  more generally, to use and operate it in the 
//same conditions as regards security. 
//
//The fact that you are presently reading this means that you have had
//knowledge of the CeCILL-C license and that you accept its terms.

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.BouilloireI;
import interfaces.CapteurVentI;
import interfaces.ChargeurI;
import interfaces.ChauffageI;
import interfaces.ControleurI;
import interfaces.EolienneI;

//-----------------------------------------------------------------------------
/**
 * The class <code>URIServiceConnector</code> implements a connector between
 * the <code>URIConsumerI</code> and the <code>URIProviderI</code> interfaces.
 *
 * <p><strong>Description</strong></p>
 * 
 * It implements the required interface <code>URIConsumerI</code> and in the
 * method <code>getURI</code>, it calls the corresponding offered method
 * <code>provideURI</code>.
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2014-01-22</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class				ControleurConnector
extends		AbstractConnector
implements ControleurI
{

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
	//--------------------CAPTEUR------------------------
	//---------------------------------------------------
	@Override
	public double getVent() throws Exception {
		return ((CapteurVentI)this.offering).sendWind();
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
	//--------------------BOUILLOIRE---------------------
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
	//---------------------Chargeur----------------------
	//---------------------------------------------------
	@Override
	public void startChargeur() throws Exception {
		((ChargeurI)this.offering).startChargeur();
		
	}

	@Override
	public void stopChargeur() throws Exception {
		((ChargeurI)this.offering).stopChargeur();
		
	}
}
//-----------------------------------------------------------------------------
