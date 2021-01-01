package plugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import commands.TeleportAccept;
import commands.TeleportDeny;
import commands.TeleportRequest;

public final class TPPlugin extends JavaPlugin {

	private FileConfiguration config = getConfig();

	// Fired when plugin is first enabled
	@Override
	public void onEnable() {
		// Config
        config.options().copyDefaults(true);
        saveConfig();

		// Register commands
        getCommand("tpa").setExecutor(new TeleportRequest());
        getCommand("tpaccept").setExecutor(new TeleportAccept());
        getCommand("tpdeny").setExecutor(new TeleportDeny());
		
	}

	// Fired when plugin is disabled
	@Override
	public void onDisable() {
		
	}
}