package commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import homelocations.LocationStore;

public class DelHome implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (args.length != 1) {
				sender.sendMessage("Did not provide appropriate argument: <LocationName>.");
				return true;
			}

			String name = args[0].toLowerCase();

			Boolean locationFound = LocationStore.getStore().DeleteLocation(player, name);
			if (locationFound) {
				sender.sendMessage("Successfully deleted '" + name + "' from your list of home locations.");
			} else {
				sender.sendMessage("Could not find '" + name
						+ "' in your list of home locations, are you sure it was spelled correctly?");
			}

		} else {
			sender.sendMessage("/delhome can only be called by players!");
			return true;
		}

		return true;
	}

}
