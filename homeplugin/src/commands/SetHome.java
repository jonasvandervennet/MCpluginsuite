package commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import homelocations.LocationStore;

public class SetHome implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (args.length != 1) {
				sender.sendMessage("Did not provide appropriate argument: <LocationName>.");
				return true;
			}

			if (args[0].contains(":::")) {
				sender.sendMessage("Your homelocation cannot contain ':::', why did you even use that?");
				return true;
			}
			
			String name = args[0].toLowerCase();

			Location location = LocationStore.getStore().RegisterLocation(player, player.getLocation(), name);
			if (location != null) {
				sender.sendMessage("Successfully registered homelocation " + name + " at "
						+ LocationStore.printLocation(location));
			} else {
				sender.sendMessage(
						"Could not register this location, you probably have too many homelocations set (max "
								+ LocationStore.getStore().maxhomes + " allowed).");
			}

		} else {
			sender.sendMessage("/sethome can only be called by players!");
			return true;
		}

		return true;
	}

}
