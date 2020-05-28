package st0ro.redflags;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import st0ro.redflags.gui.Button;
import st0ro.redflags.gui.Label;
import st0ro.redflags.lobby.Lobby;

public class LobbyState extends SState {
	
	private Lobby lobby;
	private Socket socket;
	private PrintWriter socketOut;
	private BufferedReader socketIn;
	private String[] names;
	
	private Label[] lblPlayers;
	private Button btnStart;
	
	public LobbyState(RedFlags redFlags) {
		super(redFlags);
	}
	
	@Override
	public void init(GameContainer gContainer, StateBasedGame game) throws SlickException {
		backgroundFill = RedFlags.backgroundFill;
		btnStart = new Button(gContainer, "Start Game", -1000, -1000, 120, 40) {
			@Override
			public void onLeftClick() {
				if(lobby != null) {
					ExecutorService thread = Executors.newSingleThreadExecutor();
					thread.execute(lobby.startGame());
				}
			}
		};
		components.add(btnStart);
		components.add(new Label(gContainer, "Lobby", RedFlags.WIDTH/2, 100, 100));
		lblPlayers = new Label[8];
		for(int i = 0; i < 8; ++i) {
			lblPlayers[i] = new Label(gContainer, "Waiting for player...", RedFlags.WIDTH/2, 180 + 40*i, 200);
			components.add(lblPlayers[i]);
		}
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException{
		try {
			if(socketIn.ready()) {
				String in = socketIn.readLine();
				switch(in) {
				case "refresh":
					refreshNames();
					break;
				case "start":
					redFlags.playState.start(container, names, Integer.parseInt(socketIn.readLine()), socket, socketOut, socketIn);
					redFlags.playState.enterState(container, game);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void hostLobby() {
		lobby = new Lobby(redFlags);
	}
	
	public void joinParty(Socket sock) {
		try {
			socket = sock;
			socketOut = new PrintWriter(socket.getOutputStream(), true);
			socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			socketOut.println(redFlags.name);
			socketIn.readLine(); //remove "refresh" from buffer
			refreshNames();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void refreshNames() {
		try {
			int lines = Integer.parseInt(socketIn.readLine());
			names = new String[lines];
			for(int i = 0; i < lines; ++i) {
				names[i] = socketIn.readLine();
			}
			for(int i = 0; i < 8; ++i) {
				if(i < lines) {
					lblPlayers[i].setText(names[i]);
				}
				else {
					lblPlayers[i].setText("Waiting for player...");
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updatePlayerCount(int count) {
		if(count >= 3) {
			btnStart.setLocation(1000, 500);
		}
		else {
			btnStart.setLocation(-1000, -1000);
		}
	}
	
	@Override
	public int getID() {
		return 2;
	}
}
