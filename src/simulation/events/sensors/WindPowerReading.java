package simulation.events.sensors;

import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

public class			WindPowerReading
extends		Event
{

	public static class	Reading
	implements	EventInformationI
	{
		private static final long serialVersionUID = 1L;
		public final double	value ;

		public			Reading(double value)
		{
			super();
			this.value = value;
		}
	}

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L ;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public				WindPowerReading(
		Time timeOfOccurrence,
		double windPowerReading
		)
	{
		super(timeOfOccurrence, new Reading(windPowerReading)) ;
		assert	windPowerReading >= 0.0 ;
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	@Override
	public String		eventAsString()
	{
		return "WindPowerReading(" + this.eventContentAsString() + ")" ;
	}

	@Override
	public String		eventContentAsString()
	{
		return	"time = " + this.getTimeOfOccurrence() + ", " +
				"wind power = " + ((Reading)this.getEventInformation()).value
											+ " m/s" ;
	}
}
