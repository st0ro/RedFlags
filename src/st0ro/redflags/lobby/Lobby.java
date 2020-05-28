package st0ro.redflags.lobby;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import st0ro.redflags.Player;
import st0ro.redflags.RedFlags;

public class Lobby {
	
	private ServerSocket server;
	private ArrayList<Player> players;
	private ExecutorService pool;
	private ArrayList<AddPlayer> threadList;
	
	private RedFlags redFlags;

	public Lobby(RedFlags redFlags) {
		this.redFlags = redFlags;
		threadList = new ArrayList<AddPlayer>(8);
		players = new ArrayList<Player>(8);
		try {
			server = new ServerSocket(25565);
			pool = Executors.newCachedThreadPool();
			for(int i = 0; i < 8; ++i) {
				pool.execute(new AddPlayer(server, this));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void playerJoined(Player player) {
		players.add(player);
		for(Player p:players) {
			p.refreshPlayers(players);
		}
		redFlags.lobbyState.updatePlayerCount(players.size());
	}
	
	public synchronized void playerQuit(Player player, AddPlayer thread) {
		players.remove(player);
		threadList.remove(thread);
		for(Player p:players) {
			p.refreshPlayers(players);
		}
		redFlags.lobbyState.updatePlayerCount(players.size());
		AddPlayer newThread = new AddPlayer(server, this);
		threadList.add(newThread);
		pool.execute(newThread);
	}
	
	public Host startGame() {
		try {
			server.close();
		} catch (IOException e) {
			System.out.println("error stopping server socket");
		}
		for(AddPlayer p:threadList) {
			p.stop();
		}
		pool.shutdownNow();
		players.trimToSize();
		for(Player p:players) {
			p.sendMessage("start");
			p.sendMessage(players.size() + "");
		}
		return new Host(players);
	}
	
	public void close() {
		pool.shutdownNow();
		try {
			server.close();
		} catch (IOException e) {
			System.out.println("Error closing server socket.");
		}
		players = null;
	}
}
