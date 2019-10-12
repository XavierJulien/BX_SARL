package components;

import data.EolienneD;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.interfaces.DataOfferedI;
import fr.sorbonne_u.components.interfaces.DataOfferedI.DataI;
import interfaces.ControleurI;
import launcher.CVM;
import ports.ControleurDataInboundPort;
import ports.ControleurDataOutboundPort;
import ports.EolienneDataInboundPort;
import ports.EolienneDataOutboundPort;

public class Controleur extends AbstractComponent implements ControleurI {

	
	
	/** URI of the component (controleur).									*/
	protected final String				uri ;
	/** The data inbound port URI for the controleur.		*/
	protected final String				controleurDataInboundPortURI ;	
	/** The data outbound port URI for the eolienne.		*/
	protected final String				controleurDataOutboundPortURI ;
	/** Data outbound port for the component (controleur).						*/
	protected ControleurDataOutboundPort	controleurDataOutboundPort ;
	/** Data inbound port for the component (controleur).						*/
	protected ControleurDataInboundPort	controleurDataInboundPort ;

	/** 	The data, used in the pull parts.								*/
	
	/** 	The data, used in the pull parts.								*/
	protected EolienneD					eolienneD ;

	
	
	public Controleur(String uri,String eolienneDataOutboundPortURI,String eolienneDataInboundPortURI) throws Exception{
		super(uri, 1, 1);

		//check arguments 
		assert uri != null;
		assert eolienneDataOutboundPortURI != null;
		assert eolienneDataOutboundPortURI != null;

		// init variables 
		this.uri = uri;
		this.controleurDataInboundPortURI = eolienneDataInboundPortURI;
		this.controleurDataOutboundPortURI = eolienneDataOutboundPortURI;
		
		// The data interfaces and ports.
		//ici on n'est producteur du coup on garde que les OfferedI
		this.addOfferedInterface(DataOfferedI.PullI.class) ;
		this.addRequiredInterface(DataOfferedI.PushI.class) ;
		
		// init des ports avec les uri et de la donnée qui circule
		this.eolienneD = new EolienneD(0);
		this.controleurDataInboundPort = new ControleurDataInboundPort(this.controleurDataInboundPortURI, this) ;
		this.controleurDataOutboundPort = new ControleurDataOutboundPort(this.controleurDataOutboundPortURI, this) ;
		
		//publish des ports (voir avec le prof ce que ça fait)
		this.controleurDataOutboundPort.publishPort() ;
		this.controleurDataInboundPort.publishPort() ;
		
		this.tracer.setTitle(uri) ;
		if (uri.equals(CVM.EOLIENNE_COMPONENT_URI)) {
			this.tracer.setRelativePosition(1, 0) ;
		} else {
			this.tracer.setRelativePosition(1, 1) ;
		}
	}

	public DataI getEolienneD() {
		return eolienneD;
	}


	public void shutdownEolienne(EolienneD d) {
		
		
	}
	
	
}
