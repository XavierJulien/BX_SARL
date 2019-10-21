package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentState;
import fr.sorbonne_u.components.ComponentStateI;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import fr.sorbonne_u.components.ports.PortI;
import interfaces.CapteurVentI;
import ports.CapteurVentInboundPort;

public class CapteurVent extends AbstractComponent implements CapteurVentI {

	protected final String				uri ;
	/** The inbound port URI for the eolienne.*/
	protected final String				capteurVentInboundPortURI ;
	
	
	protected double power = 0;
	
	
	
	protected CapteurVent(String uri, String capteurVentInboundPortURI) throws Exception{
		
		super(uri, 1, 1);
		this.uri = uri;
		this.capteurVentInboundPortURI = capteurVentInboundPortURI;
			
		PortI p = new CapteurVentInboundPort(capteurVentInboundPortURI, this) ;
		p.publishPort() ;
		
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}	
		
		this.tracer.setTitle("CapteurVent") ;
		this.tracer.setRelativePosition(2, 1) ;
	}

	@Override
	public double sendWind() throws Exception {
		this.logMessage("Sending wind power....") ;
		power+=0.2;
		return Math.abs(Math.sin(power));
//		return 0.3;
		
	}
	
	@Override
	public void			start() throws ComponentStartException
	{
		super.start() ;
		this.logMessage("starting CapteurVent component.") ;
		// Schedule the first service method invocation in one second.
		this.scheduleTask(
			new AbstractComponent.AbstractTask() {
				@Override
				public void run() {
					try {
						((CapteurVent)this.getTaskOwner()).sendWind() ;
						
					} catch (Exception e) {
						throw new RuntimeException(e) ;
					}
				}
			},
			1000, TimeUnit.MILLISECONDS);
	}
}
