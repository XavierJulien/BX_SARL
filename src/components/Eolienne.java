package components;

import data.EolienneD;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.connectors.DataConnector;
import fr.sorbonne_u.components.examples.pingpong.components.Ball;
import fr.sorbonne_u.components.examples.pingpong.components.PingPongPlayer;
import fr.sorbonne_u.components.examples.pingpong.connectors.PingPongConnector;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.interfaces.DataOfferedI;
import interfaces.EolienneI;
import launcher.CVM;
import ports.EolienneDataInboundPort;
import ports.EolienneDataOutboundPort;

public class Eolienne extends AbstractComponent implements EolienneI{

	/** URI of the component (eolienne).									*/
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
	
	@Override
	public void			start() throws ComponentStartException
	{
		super.start();
		
		try {
			this.doPortConnection(
					this.eolienneDataInboundPort.getPortURI(),
					this.eolienneDataOutboundPortURI,
					DataConnector.class.getCanonicalName()) ;	
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	
	public void execute() throws Exception{
		super.execute() ;

		// The player that has the service starts the exchanges.
		this.traceMessage(this.uri + " Hello.\n") ;
	}
	
	
	
	@Override
	public void			finalise() throws Exception
	{
		
		this.doPortDisconnection(this.eolienneDataInboundPort.getPortURI()) ;
		super.finalise();
	}

	
	
	@Override
	public void			shutdown() throws ComponentShutdownException
	{
		// Before shutting down (super call) unpublish the ports so that they
		// can be destroyed during the shut down process.
		try {
			this.doShutdownWork() ;
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		
		super.shutdown();
	}
	
	/**
	 * do the shutdown work for this component; allow to share it between
	 * <code>shutdown</code> and <code>shutdownNow</code>.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @throws Exception		<i>todo.</i>
	 */
	protected void		doShutdownWork() throws Exception
	{
		this.eolienneDataOutboundPort.unpublishPort() ;
		this.eolienneDataInboundPort.unpublishPort() ;
	}
	
	
	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdownNow()
	 */
	@Override
	public void			shutdownNow() throws ComponentShutdownException
	{
		// Before shutting down (super call) unpublish the ports so that they
		// can be destroyed during the shut down process.
		try {
			this.doShutdownWork() ;
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		
		super.shutdownNow();
	}

	
	
	
	// ------------------------------------------------------------------------
	// Services
	// ------------------------------------------------------------------------

	
	
	public void dataPull()  throws Exception{
		
		assert eolienneD != null;
		
		this.traceMessage(this.uri +" veut envoyer des donnees");
		
//		//demande l'envoi de la donnée au composant de l'autre coté
//		this.eolienneD = (EolienneD) this.eolienneDataOutboundPort.request();
		
		
	}
	
	
	
	
	
	
	


	public void shutdownEolienne(EolienneD d) {
		// TODO
	}

	public EolienneD getEolienneD() {
		// TODO Auto-generated method stub
		return eolienneD;
	}
}
