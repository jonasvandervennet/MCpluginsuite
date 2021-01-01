package commands;

import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import homelocations.LocationStore;

public class HomeTeleport implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			
			if (args.length != 1) {
				sender.sendMessage("Did not provide appropriate argument: <LocationName>.");
				return true;
			}
			
			String name = args[0].toLowerCase();
					
			// get homelocations of player
			TreeMap<String, Location> homelocations = LocationStore.getStore().GetHomeLocations(player);
			if (homelocations == null) {
				sender.sendMessage("You did not register any homelocations yet!");
				return true;
			}

			// grab correct name
			if (!homelocations.containsKey(name)) {
				sender.sendMessage("You did not register any homelocations matching '"+ name +"'");
				return true;
			}
			Location destination = homelocations.get(name);
			
			// teleport
			player.teleport(destination);
			
		} else {
			sender.sendMessage("/home can only be called by players!");
			return true;
		}

		return true;
	}

}
