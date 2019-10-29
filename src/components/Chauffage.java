package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.ChauffageI;
import ports.ChauffageInboundPort;
import ports.ChauffageOutboundPort;

@RequiredInterfaces(required = {ChauffageI.class})
@OfferedInterfaces(offered = {ChauffageI.class})
public class Chauffage extends AbstractComponent {
	
	/** URI of the component (chauffage).*/
	protected final String				uri ;
	/** The inbound port URI for the chauffage.*/
	protected final String				chauffageInboundPortURI ;	
	/** The outbound port URI for the chauffage.*/
	protected final String				chauffageOutboundPortURI ;
	/** outbound port for the component (chauffage).*/
	protected ChauffageOutboundPort	chauffageOutboundPort ;
	/** inbound port for the component (chauffage).*/
	protected ChauffageInboundPort		chauffageInboundPort ;


	/** utility vars */
	protected boolean isOn=false;

	protected Chauffage(String uri,String chauffageOutboundPortURI,String chauffageInboundPortURI) throws Exception{
		super(uri, 1, 1);

		//check arguments 
		assert uri != null;
		assert chauffageOutboundPortURI != null;
		assert chauffageInboundPortURI != null;

		// init variables 
		this.uri = uri;

		chauffageInboundPort = new ChauffageInboundPort(chauffageInboundPortURI, this) ;

		// publish the port
		chauffageInboundPort.publishPort() ;

		this.chauffageInboundPortURI = chauffageInboundPortURI;
		this.chauffageOutboundPortURI = chauffageOutboundPortURI;

		this.chauffageOutboundPort =
				new ChauffageOutboundPort(chauffageOutboundPortURI, this) ;
		// publish the port (an outbound port is always local)
		this.chauffageOutboundPort.localPublishPort() ;

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	

		this.tracer.setTitle(uri) ;
		this.tracer.setRelativePosition(2, 3) ;




	}


	// ------------------------------------------------------------------------
	// Services
	// ------------------------------------------------------------------------

	public void startChauffage() throws Exception{
		this.logMessage("The chauffage is starting his job....") ;
		isOn = true;
	}



	public double sendConsommation() throws Exception {
		this.logMessage("Sending consommation....") ;

		return Math.random()*10;

	}


	public void stopChauffage() throws Exception{
		this.logMessage("The chauffage is stopping his job....") ;
		isOn =false;
	}


	public void			start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting Chauffage component.") ;
	}
	
	@Override
	public void execute() throws Exception{
		super.execute();
	}

	// ------------------------------------------------------------------------
	// FINALISE / SHUTDOWN
	// ------------------------------------------------------------------------


	@Override
	public void finalise() throws Exception {
		chauffageOutboundPort.doDisconnection();
		super.finalise();
	}

	@Override
	public void shutdown() throws ComponentShutdownException {
		try {
			chauffageInboundPort.unpublishPort();
			chauffageOutboundPort.unpublishPort();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.shutdown();
	}

}
