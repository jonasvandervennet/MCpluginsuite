package plugin;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import commands.CooldownSummary;
import commands.RefreshCooldowns;
import deaths.DeathStore;
import listeners.DeathListener;
import listeners.JoinListener;

public final class HCCooldown extends JavaPlugin {

	private FileConfiguration config = getConfig();

	// Fired when plugin is first enabled
	@Override
	public void onEnable() {
		// Config
		config.addDefault("dumplocation", "plugins/HCCooldown/death_record.txt");
		config.options().copyDefaults(true);
		saveConfig();

		// make sure there is at least a file to be read from at input time
		try {
			File yourFile = new File(config.getString("dumplocation"));
			yourFile.createNewFile(); // if file already exists will do nothing
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// load known death record for all players
		DeathStore.GetDeathStore().load(config.getString("dumplocation"));

		// register triggers
		getServer().getPluginManager().registerEvents(new DeathListener(), this);
		getServer().getPluginManager().registerEvents(new JoinListener(), this);

		// Register commands
		getCommand("cooldown").setExecutor(new CooldownSummary());
		getCommand("cdrefresh").setExecutor(new RefreshCooldowns());

		// Register cooldown refresh every 5 minutes, 1 minute delay before first run
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
			public void run() {
				DeathStore.GetDeathStore().RefreshDeathStore();
			}
		}, 1 * 60 * 20, 5 * 60 * 20);
	}

	// Fired when plugin is disabled
	@Override
	public void onDisable() {
		DeathStore.GetDeathStore().dump(config.getString("dumplocation"));
	}
}