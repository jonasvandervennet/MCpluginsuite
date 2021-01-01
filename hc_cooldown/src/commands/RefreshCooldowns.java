package commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import deaths.DeathStore;

public class RefreshCooldowns implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		DeathStore.GetDeathStore().RefreshDeathStore();
		sender.sendMessage("Refreshed active cooldowns");
		return true;
	}

}
