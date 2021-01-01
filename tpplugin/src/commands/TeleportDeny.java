package commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import teleport.TeleportStore;

public class TeleportDeny implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
            Player player = (Player) sender;
            
            TeleportStore.GetTeleportStore().denyTeleport(player);
        } else {
        	sender.sendMessage("/calladmin can only be called by players!");
        	return true;
        }
		
		return true;
	}

}
