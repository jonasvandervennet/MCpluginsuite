package commands;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import homelocations.LocationStore;

public class HomeList implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			TreeMap<String, Location> locations = LocationStore.getStore().GetHomeLocations(player);
			if (locations == null) {
				sender.sendMessage("You have no homes set!");
				return true;
			}
			String message = "Your home locations:        (max. " + LocationStore.getStore().maxhomes + " allowed)\n";

			int counter = 0;
			for (Map.Entry<String, Location> entry : locations.entrySet()) {
				counter++;
				String name = entry.getKey();
				Location location = entry.getValue();
				message += counter + ") " + name + ": " + LocationStore.printLocation(location) + "\n";
			}
			sender.sendMessage(message);

		} else {
			sender.sendMessage("/homelist can only be called by players!");
			return true;
		}

		return true;
	}

}
