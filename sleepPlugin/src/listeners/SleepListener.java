package listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class SleepListener implements Listener {

	private int amountSleeping = 0;
	private double boundary;

	// Colors from:
	// https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
	private final String ANSI_RESET = "\u001B[0m";
	private final String ANSI_YELLOW = "\u001B[33m";

	public SleepListener(double boundary) {
		this.boundary = boundary;
	}

	@EventHandler
	public void onPlayerEnterBed(PlayerBedEnterEvent event) {
		if (!(event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK))
			return;
		int amountOnline = Bukkit.getServer().getOnlinePlayers().size();
		int amountNeeded = (int) Math.ceil(amountOnline * boundary);
		Bukkit.broadcastMessage(printMessage("Player is now sleeping: " + event.getPlayer().getDisplayName() + " ("
				+ (++amountSleeping) + "/" + amountOnline + ")"));
		if (amountSleeping >= amountNeeded) {
			// Allow sleep to go through if majority wants to sleep
			// Get total world ticks (time)
			World w = Bukkit.getServer().getWorld("world");
			long currentTicks = w.getTime();
			// A day is exactly 24000 ticks, so determine difference
			long ticks_to_add = 24000 - currentTicks;
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "time add " + ticks_to_add);
			amountSleeping = 0;
		}
	}

	@EventHandler
	public void onPlayerLeaveBed(PlayerBedLeaveEvent event) {
		if (amountSleeping == 0)
			return;
		Bukkit.broadcastMessage(printMessage("Player stopped sleeping: " + event.getPlayer().getDisplayName() + " ("
				+ (--amountSleeping) + "/" + Bukkit.getServer().getOnlinePlayers().size() + ")"));
	}

	private String printMessage(String msg) {
		return ChatColor.YELLOW + msg + ChatColor.RESET;
	}
}
