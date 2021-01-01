package plugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import listeners.SleepListener;

public final class SleepPlugin extends JavaPlugin {

	private FileConfiguration config = getConfig();

	// Fired when plugin is first enabled
	@Override
	public void onEnable() {
		// Config
		config.addDefault("percentage_needed", 0.5);
		config.options().copyDefaults(true);
		saveConfig();
		
		// Listeners
		getServer().getPluginManager().registerEvents(new SleepListener(config.getDouble("percentage_needed")), this);
	}

	// Fired when plugin is disabled
	@Override
	public void onDisable() {

	}
}