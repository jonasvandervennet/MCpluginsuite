package deaths;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Death implements Serializable {
	// 1 hour in milliseconds
	public static long minuteOffset = 60 * 1000;
	// 1 hour in milliseconds
	public static long hourOffset = 60 * minuteOffset;

	private UUID _player;
	private int _multiplier; // Each death incurs an additional multiplier
	private long _timestamp;
	private boolean _active;

	public Death(OfflinePlayer player, int multiplier, long timestamp) {
		_player = player.getUniqueId();
		_multiplier = multiplier;
		_timestamp = timestamp;
		_active = true;
	}

	public Player GetPlayer() {
		return Bukkit.getPlayer(_player);
	}

	private long GetRemainingCooldown(long cur_timestamp) {
		long expiredSinceDeath = cur_timestamp - _timestamp;
		long totalSentence = _multiplier * hourOffset;

		long remainingSentence = totalSentence - expiredSinceDeath;
		if (remainingSentence / hourOffset == 0) {
			return remainingSentence / minuteOffset;
		}
		return remainingSentence / hourOffset;
	}

	public String GetRemainingCooldownWithUnit(long cur_timestamp) {
		long expiredSinceDeath = cur_timestamp - _timestamp;
		long totalSentence = _multiplier * hourOffset;

		long remainingSentence = totalSentence - expiredSinceDeath;
		if (remainingSentence / hourOffset == 0) {
			return remainingSentence / minuteOffset +"m";
		}
		return remainingSentence / hourOffset +"h";
	}

	public boolean UpdateCooldown(long cur_timestamp) {
		long remainingSentence = GetRemainingCooldown(cur_timestamp);
		if (IsActiveCooldown() && remainingSentence <= 0) {
			this.DeactivateCooldown();
			return true;
		}
		return false;
	}

	public boolean IsActiveCooldown() {
		return this._active;
	}

	public void DeactivateCooldown() {
		this._active = false;
	}
	
	// some auto-generated serialID
	private static final long serialVersionUID = 5328018943811754803L;
}
