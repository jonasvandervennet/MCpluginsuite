package deaths;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class DeathStore {

	private static DeathStore store = new DeathStore();

	public HashMap<OfflinePlayer, ArrayList<Death>> deaths = new HashMap<OfflinePlayer, ArrayList<Death>>();

	private DeathStore() {

	}

	public static DeathStore GetDeathStore() {
		return store;
	}

	public void RefreshDeathStore() {
		// called every so often, or by manually invoking a command
		// Checks if there are cooldowns that are expired and flags them as such
		long timestamp = System.currentTimeMillis();
		for (Map.Entry<OfflinePlayer, ArrayList<Death>> entry : deaths.entrySet()) {
			// only update cooldowns for online players
			// offline players will get refreshed on joinEvent
			Player p = entry.getKey().getPlayer();
			if (p != null) {
				for (Death d : entry.getValue()) {
					boolean updated = d.UpdateCooldown(timestamp);
					if (updated) {
						p.setGameMode(GameMode.SURVIVAL);
					}
				}
			}
		}
	}

	// Returns the multiplier (# hours cooldown) applied
	public int RegisterDeath(OfflinePlayer player, long timestamp) {
		int multiplier;
		if (!deaths.containsKey(player)) {
			multiplier = 1; // start at 1x penalty
			deaths.put(player, new ArrayList<Death>());
		} else {
			ArrayList<Death> curr_deaths = deaths.get(player);
			multiplier = curr_deaths.size() + 1;

			// there shouldn't be an existing death,
			// but just to be sure we'll deactivate the cooldown
			curr_deaths.get(curr_deaths.size() - 1).DeactivateCooldown();
		}
		Death d = new Death(player, multiplier, timestamp);
		deaths.get(player).add(d);
		return multiplier;
	}

	public void load(String filename) {
		HashMap<UUID, ArrayList<Death>> serializedDeathStore;
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
			serializedDeathStore = (HashMap<UUID, ArrayList<Death>>) in.readObject();
			if (serializedDeathStore == null) {
				System.out.println("    Stored death records returned a null cast.");
				return; // nothing to be loaded in, keep default initialization
			}
			// Convert back to useful values
			deaths = deserialize(serializedDeathStore);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (EOFException e) {
			System.out.println("    HCCooldown datafile was empty, no data loaded!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void dump(String filename) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
			out.writeObject(serialize());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private HashMap<UUID, ArrayList<Death>> serialize() {
		HashMap<UUID, ArrayList<Death>> new_deaths = new HashMap<>();
		for (Map.Entry<OfflinePlayer, ArrayList<Death>> entry : deaths.entrySet()) {
			System.out.println(entry.getKey().getName() + "->" + entry.getKey().getUniqueId());
			new_deaths.put(entry.getKey().getUniqueId(), entry.getValue());
		}
		return new_deaths;
	}

	private HashMap<OfflinePlayer, ArrayList<Death>> deserialize(HashMap<UUID, ArrayList<Death>> ser) {
		HashMap<OfflinePlayer, ArrayList<Death>> new_deaths = new HashMap<>();
		for (Map.Entry<UUID, ArrayList<Death>> entry : ser.entrySet()) {
			new_deaths.put(Bukkit.getOfflinePlayer(entry.getKey()), entry.getValue());
		}
		return new_deaths;
	}

}
