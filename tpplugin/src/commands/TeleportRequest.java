package commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import teleport.TeleportStore;

public class TeleportRequest implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			
			if (args.length != 1) {
				sender.sendMessage("Did not provide appropriate argument: <Playername>.");
				return true;
			}

			Player destination = Bukkit.getServer().getPlayer(args[0]);
			if (destination == null) {
				sender.sendMessage("Could not find player specified. Try again please.");
				return true;
			}

			TeleportStore.GetTeleportStore().requestTeleport(player, destination);
			sender.sendMessage("Sent a teleport request to " + destination.getDisplayName());
		} else {
			sender.sendMessage("/tpa can only be called by players!");
			return true;
		}

		return true;
	}

}
