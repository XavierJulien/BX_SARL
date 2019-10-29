package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.EolienneI;
import launcher.CVM;
import ports.EolienneInboundPort;
import ports.EolienneOutboundPort;

@RequiredInterfaces(required = {EolienneI.class})
@OfferedInterfaces(offered = {EolienneI.class})
public class Eolienne extends AbstractComponent {

	/** URI of the component (eolienne).*/
	protected final String				uri ;
	/** The inbound port URI for the eolienne.*/
	protected final String				eolienneInboundPortURI ;	
	/** The outbound port URI for the eolienne.*/
	protected final String				eolienneOutboundPortURI ;
	/** outbound port for the component (eolienne).*/
	protected EolienneOutboundPort	eolienneOutboundPort ;
	/** inbound port for the component (eolienne).*/
	protected EolienneInboundPort	eolienneInboundPort ;


	/** utility vars */
	protected double prod;
	protected boolean isOn=false;

	protected Eolienne(String uri,String eolienneOutboundPortURI,String eolienneInboundPortURI) throws Exception{
		super(uri, 1, 1);

		//check arguments 
		assert uri != null;
		assert eolienneOutboundPortURI != null;
		assert eolienneInboundPortURI != null;

		// init variables 
		this.uri = uri;

		eolienneInboundPort = new EolienneInboundPort(eolienneInboundPortURI, this) ;

		// publish the port
		eolienneInboundPort.publishPort() ;


		this.eolienneInboundPortURI = eolienneInboundPortURI;
		this.eolienneOutboundPortURI = eolienneOutboundPortURI;

		eolienneOutboundPort =
				new EolienneOutboundPort(eolienneOutboundPortURI, this) ;
		// publish the port (an outbound port is always local)
		eolienneOutboundPort.localPublishPort() ;

		//		
		//		// The  interfaces and ports.
		//		//ici on n'est producteur du coup on garde que les OfferedI
		//		this.addOfferedInterface(EolienneI.class) ;
		//		this.addRequiredInterface(EolienneI.class) ;
		//		
		//		
		//		// init des ports avec les uri et de la donnée qui circule
		//		this.eolienneInboundPortURI = new eolienneInboundPortURI(this.eolienneInboundPortURIURI, this) ;
		//		this.eolienneOutboundPort = new EolienneOutboundPort(this.eolienneOutboundPortURI, this) ;
		//		
		//		//publish des ports (voir avec le prof ce que ça fait)
		//		this.eolienneOutboundPort.publishPort() ;
		//		this.eolienneInboundPortURI.publishPort() ;
		//		
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		this.tracer.setTitle(uri) ;
		if (uri.equals(CVM.EOLIENNE_COMPONENT_URI)) {
			this.tracer.setRelativePosition(1, 2) ;
		} else {
			this.tracer.setRelativePosition(1, 0) ;
		}


		this.prod = 0;


	}


	// ------------------------------------------------------------------------
	// Services
	// ------------------------------------------------------------------------

	public void startEolienne() throws Exception{
		this.logMessage("The eolienne is starting his job....") ;
		isOn = true;
	}



	public double sendProduction() throws Exception {
		this.logMessage("Sending energy....") ;

		return Math.random()*10;

	}


	public void stopEolienne() throws Exception{
		this.logMessage("The eolienne is stopping his job....") ;
		isOn =false;
	}


	public void			start() throws ComponentStartException
	{
		super.start() ;
		this.logMessage("starting Eolienne component.") ;
	}
	
	@Override
	public void execute() throws Exception {
		super.execute();
	}

	// ------------------------------------------------------------------------
	// FINALISE / SHUTDOWN
	// ------------------------------------------------------------------------


	@Override
	public void finalise() throws Exception {
		eolienneOutboundPort.doDisconnection();
		super.finalise();
	}

	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			eolienneInboundPort.unpublishPort();
			eolienneOutboundPort.unpublishPort();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.shutdown();
	}
}
