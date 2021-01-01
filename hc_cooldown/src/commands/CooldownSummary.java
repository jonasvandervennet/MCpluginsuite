package commands;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import deaths.Death;
import deaths.DeathStore;

public class CooldownSummary implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			long timestamp = System.currentTimeMillis();
			String baseMessage = "Currently active cooldowns:\n";
			String additionalMessage = "";
			int numActiveCooldowns = 0;
			for (Map.Entry<OfflinePlayer, ArrayList<Death>> entry : DeathStore.GetDeathStore().deaths.entrySet()) {
				Death deathOnActiveCoolDown = null;
				for (Death d : entry.getValue()) {
					if (d.IsActiveCooldown()) {
						numActiveCooldowns++;
						deathOnActiveCoolDown = d;
						break;
					}
				}
				if (deathOnActiveCoolDown != null) {
					additionalMessage += "    " + entry.getKey().getName() + "[" + entry.getValue().size() + "]" + " | "
							+ deathOnActiveCoolDown.GetRemainingCooldownWithUnit(timestamp) + " left\n";
				}
			}
			if (numActiveCooldowns > 0) {
				sender.sendMessage(baseMessage + additionalMessage);
			} else {
				sender.sendMessage(baseMessage + "    There are no active cooldowns at the moment..");
			}
		} else {
			sender.sendMessage("/" + command.getName() + " can only be called by players!");
			return true;
		}

		return true;
	}

}
