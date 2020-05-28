package st0ro.redflags.lobby;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import st0ro.redflags.Player;

public class AddPlayer implements Runnable {

	private ServerSocket server;
	private Lobby lobby;
	
	private boolean running;
	
	public AddPlayer(ServerSocket serverSocket, Lobby invoker) {
		server = serverSocket;
		lobby = invoker;
		running = false;
	}

	@Override
	public void run() {
		try {
			Socket connection = server.accept();
			System.out.println("Found connection");
			Player p = new Player(connection);
			lobby.playerJoined(p);
			
			while(running) {
				if(p.isReady()) {
					String read = p.getMessage();
					if(read == null) {
						lobby.playerQuit(p, this);
						return;
					}
					switch (read) {
					case "message":
						//message in the rest
						//lobby.playerMessage(p.checkMessages());
						break;
					case "quit":
						lobby.playerQuit(p, this);
						return;
					}
				}
				Thread.sleep(5);
			}
		} catch (IOException e) {
			System.out.println("Failure making connection, lobby closed?");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		running = false;
	}

}
