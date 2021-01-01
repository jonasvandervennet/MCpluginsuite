package homelocations;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class LocationStore {
	private static LocationStore store = new LocationStore();

	public HashMap<UUID, TreeMap<String, Location>> locations = new HashMap<>();
	private Boolean dirty = false;
	public int maxhomes;

	private LocationStore() {

	}

	public static LocationStore getStore() {
		return store;
	}

	public static String printLocation(Location location) {
		return printLocation(location, false);
	}

	public static String printLocation(Location location, Boolean detailed) {
		if (detailed)
			return "[" + location.getX() + ", " + location.getY() + ", " + location.getZ() + "]";
		return "[" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + "]";
	}

	public TreeMap<String, Location> GetHomeLocations(Player player) {
		UUID id = player.getUniqueId();
		if (!locations.containsKey(id)) {
			return null;
		}
		return locations.get(id);
	}

	public Location RegisterLocation(Player player, Location homelocation, String name) {
		dirty = true;
		TreeMap<String, Location> homelocations = GetHomeLocations(player);
		if (homelocations == null) {
			if (!(maxhomes > 0)) {
				return null;
			}
			homelocations = new TreeMap<String, Location>();
			locations.put(player.getUniqueId(), homelocations);
		}
		if (homelocations.keySet().size() > maxhomes) {
			return null;
		}
		if (homelocations.containsKey(name)) {
			homelocations.replace(name, homelocation);
		} else {
			if (homelocations.keySet().size() < maxhomes) {
				homelocations.put(name, homelocation);
			} else {
				return null;
			}
		}
		return homelocation;
	}

	public Boolean DeleteLocation(Player player, String name) {
		dirty = true;
		TreeMap<String, Location> homelocations = GetHomeLocations(player);
		if (homelocations == null) {
			return false;
		}
		return homelocations.remove(name) != null;
	}

	public void load(String filename) {
		HashMap<UUID, TreeMap<String, String>> serializedLocations;
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
			serializedLocations = (HashMap<UUID, TreeMap<String, String>>) in.readObject();
			if (serializedLocations == null) {
				System.out.println("\tStored homelocations returned a null cast.");
				return; // nothing to be loaded in, keep default initialization
			}
			// Convert back to useful values
			locations = convertFromSerializableForm(serializedLocations);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (EOFException e) {
			System.out.println("\tHomePlugin datafile was empty, no data loaded!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void dump(String filename) {
		if (!dirty)
			return;
		HashMap<UUID, TreeMap<String, String>> serializableLocations = convertToSerializableForm(locations);
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
			out.writeObject(serializableLocations);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// https://www.spigotmc.org/wiki/serializing-location-objects/
	public static String serializeLocation(Location loc) {
		if (loc == null) {
			return "";
		}
		return loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getYaw()
				+ ":" + loc.getPitch();
	}

	// https://www.spigotmc.org/wiki/serializing-location-objects/
	public static Location deserializeLocation(String s) {
		if (s == null || s.trim() == "") {
			return null;
		}
		final String[] parts = s.split(":");
		if (parts.length == 6) {
			World w = Bukkit.getServer().getWorld(parts[0]);
			double x = Double.parseDouble(parts[1]);
			double y = Double.parseDouble(parts[2]);
			double z = Double.parseDouble(parts[3]);
			float yaw = Float.parseFloat(parts[4]);
			float pitch = Float.parseFloat(parts[5]);
			return new Location(w, x, y, z, yaw, pitch);
		}
		return null;
	}

	public static HashMap<UUID, TreeMap<String, String>> convertToSerializableForm(
			HashMap<UUID, TreeMap<String, Location>> locations) {
		HashMap<UUID, TreeMap<String, String>> serializable = new HashMap<>();
		for (HashMap.Entry<UUID, TreeMap<String, Location>> entry : locations.entrySet()) {
			TreeMap<String, Location> homelocations = entry.getValue();
			TreeMap<String, String> serializableLocations = new TreeMap<>();
			for (Map.Entry<String, Location> location : homelocations.entrySet()) {
				serializableLocations.put(location.getKey(), serializeLocation(location.getValue()));
			}
			serializable.put(entry.getKey(), serializableLocations);
		}
		return serializable;
	}

	public static HashMap<UUID, TreeMap<String, Location>> convertFromSerializableForm(
			HashMap<UUID, TreeMap<String, String>> locations) {
		HashMap<UUID, TreeMap<String, Location>> deserialized = new HashMap<>();
		for (HashMap.Entry<UUID, TreeMap<String, String>> entry : locations.entrySet()) {
			TreeMap<String, Location> homelocations = new TreeMap<>();
			TreeMap<String, String> serializedLocations = entry.getValue();
			for (Map.Entry<String, String> location : serializedLocations.entrySet()) {
				homelocations.put(location.getKey(), deserializeLocation(location.getValue()));
			}
			deserialized.put(entry.getKey(), homelocations);
		}
		return deserialized;
	}
}
