package teleport;

import org.bukkit.entity.Player;

public class Teleport {
	// 1 minute in milliseconds
	public static long timeOffset = 1*60*1000;

	private Player _source, _destination;
	private long _timestamp;
	private Boolean valid = true;
	
	public Teleport(Player source, Player destination, long timestamp) {
		_source = source;
		_destination = destination;
		_timestamp = timestamp;
	}
	
	public Boolean isValid(long cur_timestamp) {
		return valid && cur_timestamp - _timestamp <= timeOffset;
	}
	
	public void invalidate() {
		valid = false;
	}
	
	public void accept() {
		_destination.sendMessage("Accepted tp invite");
		_source.teleport(_destination);
		_source.sendMessage("Tp invite accepted");
	}
	
	public String toString() {
		return "["+ _source.getDisplayName() +"]->["+ _destination.getDisplayName() +"]";
	}
}
