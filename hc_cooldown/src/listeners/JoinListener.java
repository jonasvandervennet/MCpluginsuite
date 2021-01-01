package listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import deaths.DeathStore;

public class JoinListener implements Listener {
	public JoinListener() {

	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		// reload the player cooldowns whenever someone joins
		DeathStore.GetDeathStore().RefreshDeathStore();
		System.out.println("Refreshed active cooldowns due to player join");
	}
}
