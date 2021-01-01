package teleport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class TeleportStore {

	private static TeleportStore tpstore = new TeleportStore();

	// Destination: open teleports in order
	public HashMap<Player, ArrayList<Teleport>> teleports = new HashMap<Player, ArrayList<Teleport>>();

	private TeleportStore() {

	}

	public static TeleportStore GetTeleportStore() {
		return tpstore;
	}

	public void refreshStore() {
		long timestamp = System.currentTimeMillis();
		for (Map.Entry<Player, ArrayList<Teleport>> entry : teleports.entrySet()) {
			ArrayList<Teleport> newTeleports = new ArrayList<Teleport>();
			for (Teleport tp : entry.getValue()) {
				if (!tp.isValid(timestamp)) {
					System.out.println("Invalidated teleport: " + tp);
				} else {
					newTeleports.add(tp);
				}
			}
			teleports.replace(entry.getKey(), newTeleports);
		}
	}

	public void requestTeleport(Player requester, Player destination) {
		long timestamp = System.currentTimeMillis();
		if (!teleports.containsKey(destination)) {
			teleports.put(destination, new ArrayList<Teleport>());
		}
		Teleport tp = new Teleport(requester, destination, timestamp);
		teleports.get(destination).add(tp);
		System.out.println("Teleport added: " + tp);

		destination.sendMessage("" + requester.getDisplayName()
				+ " requested to be teleported to you.\n>To accept, please type /tpaccept\n>To deny, please type /tpdeny\n(request valid for 1 minute)");
	}

	public void printAll() {
		for (Map.Entry<Player, ArrayList<Teleport>> entry : teleports.entrySet()) {
			System.out.println(entry.getKey().getDisplayName() + ":");
			for (Teleport tp : entry.getValue()) {
				System.out.println("\t" + tp);
			}
		}
	}

	public Teleport getTeleportToAccept(Player accepter) {
		refreshStore();
		printAll();
		if (teleports.containsKey(accepter) && teleports.get(accepter).size() > 0) {
			Teleport tp = teleports.get(accepter).get(0);
			tp.invalidate();
			return tp;
		}
		return null;
	}

	public void acceptTeleport(Player accepter) {
		Teleport tp = getTeleportToAccept(accepter);
		if (tp == null) {
			accepter.sendMessage("No teleport was found to accept.");
			return;
		}
		tp.accept();
	}

	public void denyTeleport(Player accepter) {
		// this already invalidates the teleport, so do nothing
		Teleport tp = getTeleportToAccept(accepter);
		if (tp == null) {
			accepter.sendMessage("No teleport was found to deny.");
		}
		accepter.sendMessage("Successfully denied teleport request");
	}
}
