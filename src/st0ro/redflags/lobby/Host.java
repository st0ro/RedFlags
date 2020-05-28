package st0ro.redflags.lobby;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

import st0ro.redflags.Player;

public class Host implements Runnable {
	
	private static final int MAX_WINS = 5;
	
	private ArrayList<Player> players;
	private ArrayList<String> whiteCards, redCards;
	private Iterator<String> whiteIter, redIter;
	private Player target, playing;

	public Host(ArrayList<Player> playerList) {
		players = playerList;
		for(int i = 0; i < players.size() - 1; ++i) {
			players.get(i).setTarget(players.get(i + 1));
		}
		players.get(players.size() - 1).setTarget(players.get(0));
		
		whiteCards = new ArrayList<String>();
		redCards = new ArrayList<String>();
		try {
			Scanner scanWhite = new Scanner(new File("cards/whitecards.txt"));
			while(scanWhite.hasNextLine()) {
				whiteCards.add(scanWhite.nextLine());
			}
			scanWhite.close();
			Scanner scanRed = new Scanner(new File("cards/redcards.txt"));
			while(scanRed.hasNextLine()) {
				redCards.add(scanRed.nextLine());
			}
			scanRed.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Collections.shuffle(whiteCards);
		Collections.shuffle(redCards);
		whiteIter = whiteCards.iterator();
		redIter = redCards.iterator();
		target = players.get(0);
		playing = players.get(1);
	}

	@Override
	public void run() {
		while(!isGameOver()) {
			dealCards();
			sendTurnUpdate();
			playing = target.getTarget();
			while(playing != target) {
				playing.sendMessage("white");
				String[] card = {"play", Integer.toString(players.indexOf(playing)), playing.getMessage()};
				boolean isWildCard = false;
				if(card[2].startsWith("<?>")) {
					isWildCard = true;
					card[2] = card[2].substring(3);
				}
				broadcast(card);
				playing.removeWhite(card[2], isWildCard);
				playing.sendMessage("white");
				card[2] = playing.getMessage();
				isWildCard = false;
				if(card[2].startsWith("<?>")) {
					isWildCard = true;
					card[2] = card[2].substring(3);
				}
				broadcast(card);
				playing.removeWhite(card[2], isWildCard);
				playing = playing.getTarget();
			}
			playing = target.getTarget();
			while(playing != target) {
				playing.sendMessage("red");
				String[] card = {"play", Integer.toString(players.indexOf(playing.getTarget())), playing.getMessage()};
				if(playing.getTarget() == target) {
					card[1] = Integer.toString(players.indexOf(target.getTarget()));
				}
				boolean isWildCard = false;
				if(card[2].startsWith("<?>")) {
					isWildCard = true;
					card[2] = card[2].substring(3);
				}
				broadcast(card);
				playing.removeRed(card[2], isWildCard);
				playing = playing.getTarget();
			}
			target.sendMessage("choice");
			String[] preParsed = {"chose", target.getMessage()};
			broadcast(preParsed);
			int choice = Integer.parseInt(preParsed[1]);
			players.get(choice).incrementScore();
			target = target.getTarget();
		}
		dealCards();
		sendTurnUpdate();
		String[] winner = {"win", null};
		for(Player p:players) {
			if(p.getScore() == MAX_WINS) {
				winner[1] = Integer.toString(players.indexOf(p));
			}
		}
		broadcast(winner);
		System.out.println("Game Over!");
	}
	
	private void dealCards() {
		for(Player p:players) {
			ArrayList<String> whites = p.getWhites();
			while(whites.size() < 5) {
				if(!whiteIter.hasNext()) {
					Collections.shuffle(whiteCards);
					whiteIter = whiteCards.iterator();
				}
				whites.add(whiteIter.next());
			}
			ArrayList<String> reds = p.getReds();
			while(reds.size() < 3) {
				if(!redIter.hasNext()) {
					Collections.shuffle(redCards);
					redIter = redCards.iterator();
				}
				reds.add(redIter.next());
			}
		}
	}
	
	private void sendTurnUpdate() {
		StringBuilder scores = new StringBuilder();
		for(Player p:players) {
			scores.append(p.getScore());
			scores.append(" ");
		}
		for(Player p:players) {
			p.sendTurnUpdate(scores.toString(), players.indexOf(target));
		}
	}
	
	private void broadcast(String[] message) {
		for(Player p:players) {
			for(int i = 0; i < message.length; ++i) {
				p.sendMessage(message[i]);
			}
		}
	}
	
	private boolean isGameOver() {
		for(Player p:players) {
			if(p.getScore() == MAX_WINS) {
				return true;
			}
		}
		return false;
	}
}
