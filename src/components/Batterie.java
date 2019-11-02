package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.BatterieI;
import launcher.CVM;
import ports.BatterieInboundPort;
import ports.BatterieOutboundPort;

@RequiredInterfaces(required = {BatterieI.class})
@OfferedInterfaces(offered = {BatterieI.class})
public class Batterie extends AbstractComponent {

	/** URI of the component (batterie).*/
	protected final String				uri ;
	/** The inbound port URI for the batterie.*/
	protected final String				batterieInboundPortURI ;	
	/** The outbound port URI for the batterie.*/
	protected final String				batterieOutboundPortURI ;
	/** outbound port for the component (batterie).*/
	protected BatterieOutboundPort	batterieOutboundPort ;
	/** inbound port for the component (batterie).*/
	protected BatterieInboundPort	batterieInboundPort ;


	/** utility vars */
	protected double prod;
	protected boolean isOn=false;

	protected Batterie(String uri,String batterieOutboundPortURI,String batterieInboundPortURI) throws Exception{
		super(uri, 1, 1);

		//check arguments 
		assert uri != null;
		assert batterieOutboundPortURI != null;
		assert batterieInboundPortURI != null;

		// init variables 
		this.uri = uri;

		batterieInboundPort = new BatterieInboundPort(batterieInboundPortURI, this) ;

		// publish the port
		batterieInboundPort.publishPort() ;


		this.batterieInboundPortURI = batterieInboundPortURI;
		this.batterieOutboundPortURI = batterieOutboundPortURI;

		batterieOutboundPort =
				new BatterieOutboundPort(batterieOutboundPortURI, this) ;
		// publish the port (an outbound port is always local)
		batterieOutboundPort.localPublishPort() ;

		//		
		//		// The  interfaces and ports.
		//		//ici on n'est producteur du coup on garde que les OfferedI
		//		this.addOfferedInterface(BatterieI.class) ;
		//		this.addRequiredInterface(BatterieI.class) ;
		//		
		//		
		//		// init des ports avec les uri et de la donnée qui circule
		//		this.batterieInboundPortURI = new batterieInboundPortURI(this.batterieInboundPortURIURI, this) ;
		//		this.batterieOutboundPort = new BatterieOutboundPort(this.batterieOutboundPortURI, this) ;
		//		
		//		//publish des ports (voir avec le prof ce que ça fait)
		//		this.batterieOutboundPort.publishPort() ;
		//		this.batterieInboundPortURI.publishPort() ;
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

	public void startBatterie() throws Exception{
		this.logMessage("The batterie is starting his job....") ;
		isOn = true;
	}



	public double sendChargePercentage() throws Exception {
		this.logMessage("Sending charge percentage....") ;

		return Math.random()*10;

	}
	
	
	public double sendEnergy() throws Exception {
		this.logMessage("Sending energy....") ;

		return Math.random()*10;

	}


	public void stopBatterie() throws Exception{
		this.logMessage("The batterie is stopping his job....") ;
		isOn =false;
	}


	public void			start() throws ComponentStartException
	{
		super.start() ;
		this.logMessage("starting Batterie component.") ;
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
		batterieOutboundPort.doDisconnection();
		super.finalise();
	}

	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			batterieInboundPort.unpublishPort();
			batterieOutboundPort.unpublishPort();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.shutdown();
	}
}
