package components;

import data.EolienneD;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.interfaces.DataOfferedI;
import interfaces.EolienneI;
import launcher.CVM;
import ports.EolienneDataInboundPort;
import ports.EolienneDataOutboundPort;

public class Eolienne extends AbstractComponent implements EolienneI{

	/** URI of the component (player).									*/
	protected final String				uri ;
	/** The data inbound port URI for the eolienne.		*/
	protected final String				eolienneDataInboundPortURI ;	
	/** The data outbound port URI for the eolienne.		*/
	protected final String				eolienneDataOutboundPortURI ;
	/** Data outbound port for the component (eolienne).						*/
	protected EolienneDataOutboundPort	eolienneDataOutboundPort ;
	/** Data inbound port for the component (eolienne).						*/
	protected EolienneDataInboundPort	eolienneDataInboundPort ;

	/** 	The data, used in the pull parts.								*/
	protected EolienneD					eolienneD ;


	public Eolienne(String uri,String eolienneDataOutboundPortURI,String eolienneDataInboundPortURI) throws Exception{
		super(uri, 1, 1);

		//check arguments 
		assert uri != null;
		assert eolienneDataOutboundPortURI != null;
		assert eolienneDataOutboundPortURI != null;

		// init variables 
		this.uri = uri;
		this.eolienneDataInboundPortURI = eolienneDataInboundPortURI;
		this.eolienneDataOutboundPortURI = eolienneDataOutboundPortURI;
		
		// The data interfaces and ports.
		//ici on n'est producteur du coup on garde que les OfferedI
		this.addOfferedInterface(DataOfferedI.PullI.class) ;
		this.addRequiredInterface(DataOfferedI.PushI.class) ;
		
		// init des ports avec les uri et de la donnée qui circule
		this.eolienneD = new EolienneD(0);
		this.eolienneDataInboundPort = new EolienneDataInboundPort(this.eolienneDataInboundPortURI, this) ;
		this.eolienneDataOutboundPort = new EolienneDataOutboundPort(this.eolienneDataOutboundPortURI, this) ;
		
		//publish des ports (voir avec le prof ce que ça fait)
		this.eolienneDataOutboundPort.publishPort() ;
		this.eolienneDataInboundPort.publishPort() ;
		
		this.tracer.setTitle(uri) ;
		if (uri.equals(CVM.EOLIENNE_COMPONENT_URI)) {
			this.tracer.setRelativePosition(1, 0) ;
		} else {
			this.tracer.setRelativePosition(1, 1) ;
		}
	}


	public void shutdownEolienne(EolienneD d) {
		// TODO Auto-generated method stub
		
	}

	public EolienneD getEolienneD() {
		// TODO Auto-generated method stub
		return null;
	}
}
