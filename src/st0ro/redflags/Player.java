package st0ro.redflags;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Player {

	private String name;
	private Socket socket;
	private PrintWriter socketOut;
	private BufferedReader socketIn;
	private Player redCardTarget;
	
	private ArrayList<String> whiteCards, redCards;
	private int score;
	
	public Player(Socket socket) {
		try {
			this.socket = socket;
			socketOut = new PrintWriter(socket.getOutputStream(), true);
			socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			name = socketIn.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		redCards = new ArrayList<String>(3);
		whiteCards = new ArrayList<String>(5);
		score = 0;
	}
	
	public void refreshPlayers(ArrayList<Player> players) {
		socketOut.println("refresh");
		socketOut.println(players.size());
		for(Player p:players) {
			socketOut.println(p.name);
		}
	}
	
	public String getMessage() {
		try {
			return socketIn.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return "Error";
		}
	}
	
	public boolean isReady() {
		try {
			return socketIn.ready();
		} catch (IOException e) {
			return false;
		}
	}
	
	public void sendMessage(String message) {
		socketOut.println(message);
	}
	
	public void sendTurnUpdate(String scores, int target) {
		socketOut.println("turn");
		socketOut.println(scores); //send scores in one line
		for(String w:whiteCards) {
			socketOut.println(w); //send white cards
		}
		for(String r:redCards) {
			socketOut.println(r); //send red cards
		}
		socketOut.println(target); //send this turn's target
	}
	
	public void close() {
		try {
			socketOut.close();
			socketIn.close();
			socket.close();
		} catch (IOException e) {
			
		}
	}
	
	public void removeWhite(String card, boolean isWildCard) {
		if(isWildCard) {
			card = "<?>";
		}
		for(int i = 0; i < whiteCards.size(); i++) {
			if(whiteCards.get(i).equals(card)) {
				whiteCards.remove(i);
				break;
			}
		}
	}
	
	public void removeRed(String card, boolean isWildCard) {
		if(isWildCard) {
			card = "<?>";
		}
		for(int i = 0; i < redCards.size(); i++) {
			if(redCards.get(i).equals(card)) {
				redCards.remove(i);
				break;
			}
		}
	}

	public Player getTarget() {
		return redCardTarget;
	}

	public void setTarget(Player redCardTarget) {
		this.redCardTarget = redCardTarget;
	}

	public ArrayList<String> getReds() {
		return redCards;
	}

	public ArrayList<String> getWhites() {
		return whiteCards;
	}

	public int getScore() {
		return score;
	}

	public void incrementScore() {
		++score;
	}
}
