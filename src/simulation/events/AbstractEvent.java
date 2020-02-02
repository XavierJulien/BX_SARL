package simulation.events;

import fr.sorbonne_u.devs_simulation.es.events.ES_Event;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

//-----------------------------------------------------------------------------
/**
* The abstract class <code>AbstractEvent</code> enforces a common
* type for all events.
*
* <p><strong>Description</strong></p>
* 
* <p><strong>Invariant</strong></p>
* 
* <pre>
* invariant		true
* </pre>
* 
* 
* @author	Julien Xavier  Alexis Belanger
*/
public class AbstractEvent extends ES_Event{

	private static final long serialVersionUID = 1L;

	public AbstractEvent(Time timeOfOccurrence,EventInformationI content) {
		super(timeOfOccurrence, content);
	}
}
