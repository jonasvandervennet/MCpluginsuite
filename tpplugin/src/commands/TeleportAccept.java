package commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import teleport.TeleportStore;

public class TeleportAccept implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
            Player player = (Player) sender;
            
            TeleportStore.GetTeleportStore().acceptTeleport(player);
        } else {
        	sender.sendMessage("/tpaccept can only be called by players!");
        	return true;
        }
		
		return true;
	}

}
