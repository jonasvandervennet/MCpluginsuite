package plugin;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import commands.DelHome;
import commands.HomeList;
import commands.HomeTeleport;
import commands.SetHome;
import homelocations.LocationStore;

public final class HomePlugin extends JavaPlugin {

	private FileConfiguration config = getConfig();

	// Fired when plugin is first enabled
	@Override
	public void onEnable() {
		// Config
		config.addDefault("dumplocation", "plugins/HomePlugin/homelocations.txt");
		config.addDefault("maxhomes", 2);
		config.options().copyDefaults(true);
		saveConfig();

		// make sure there is at least a file to be read from at input time
		try {
			File yourFile = new File(config.getString("dumplocation"));
			yourFile.createNewFile(); // if file already exists will do nothing
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Load in config parameters
		LocationStore.getStore().maxhomes = config.getInt("maxhomes");
		LocationStore.getStore().load(config.getString("dumplocation"));

		// Register commands
		getCommand("home").setExecutor(new HomeTeleport());
		getCommand("sethome").setExecutor(new SetHome());
		getCommand("homelist").setExecutor(new HomeList());
		getCommand("delhome").setExecutor(new DelHome());

	}

	// Fired when plugin is disabled
	@Override
	public void onDisable() {
		LocationStore.getStore().dump(config.getString("dumplocation"));
	}
}