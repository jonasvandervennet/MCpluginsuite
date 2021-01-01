package listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import deaths.DeathStore;

public class DeathListener implements Listener {
	public DeathListener() {

	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		player.setGameMode(GameMode.SPECTATOR); // make spectator
		
		long timestamp = System.currentTimeMillis();
		
		// process new death
		int multiplier = DeathStore.GetDeathStore().RegisterDeath(player, timestamp);
		event.setDeathMessage(event.getDeathMessage() + formatColorMessage(" (they now have a " + multiplier + " hour cooldown)"));
	}

	private String formatColorMessage(String msg) {
		return ChatColor.YELLOW + msg + ChatColor.RESET;
	}
}
