package st0ro.redflags;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import st0ro.redflags.gui.Card;
import st0ro.redflags.gui.CardStack;
import st0ro.redflags.gui.Label;

public class PlayState extends SState {
	
	private Socket socket;
	private PrintWriter socketOut;
	private BufferedReader socketIn;
	
	private Card[] cards;
	private String[] names, scores;
	private Label[] lblNames, lblScores;
	private CardStack[] stacks;
	private Label lblLastWinner, lblFinalWinner;
	private boolean playing = true;
	
	public PlayState(RedFlags redFlags) {
		super(redFlags);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		backgroundFill = RedFlags.backgroundFill;
		cards = new Card[8];
		for(int i = 0; i < 8; ++i) {
			if(i < 5) {
				cards[i] = new Card(redFlags, container, 75 + 150*i, 610, Color.white);
			}
			else {
				cards[i] = new Card(redFlags, container, 75 + 150*i, 610, Color.red);
			}
			components.add(cards[i]);
		}
		lblLastWinner = new Label(container, "Last turn's winner", -1000, -1000);
		components.add(lblLastWinner);
		lblFinalWinner = new Label(container, null, -1000, -1000);
		components.add(lblFinalWinner);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		try {
			if(playing && socketIn.ready()) {
				String input = socketIn.readLine();
				switch (input) {
				case "turn":
					scores = socketIn.readLine().split(" ");
					for(int i = 0; i < scores.length; ++i) {
						lblScores[i].setText(scores[i] + " points");
					}
					for(int i = 0; i < 8; ++i) {
						cards[i].setText(socketIn.readLine());
						cards[i].setVisible(true);
						cards[i].setLocation(cards[i].getX(), 610);
					}
					int target = Integer.parseInt(socketIn.readLine());
					for(int i = 0; i < stacks.length; i++) {
						stacks[i].setTarget(false);
					}
					stacks[target].setTarget(true);
					break;
				case "play":
					int player = Integer.parseInt(socketIn.readLine());
					String cardText = socketIn.readLine();
					stacks[player].add(cardText);
					break;
				case "white":
					for(int i = 0; i < 5; ++i) {
						cards[i].setLocation(cards[i].getX(), 500);
						cards[i].setHighlight(true);
					}
					for(int i = 5; i < 8; ++i) {
						cards[i].setLocation(cards[i].getX(), 610);
						cards[i].setHighlight(false);
					}
					break;
				case "red":
					for(int j = 0; j < 5; ++j) {
						cards[j].setLocation(cards[j].getX(), 610);
						cards[j].setHighlight(false);
					}
					for(int j = 5; j < 8; ++j) {
						cards[j].setLocation(cards[j].getX(), 500);
						cards[j].setHighlight(true);
					}
					break;
				case "choice":
					for(int k = 0; k < 8; ++k) {
						cards[k].setLocation(cards[k].getX(), 500);
						cards[k].setHighlight(false);
					}
					for(int i = 0; i < stacks.length; ++i) {
						stacks[i].setHighlight(true);
					}
					break;
				case "chose":
					int lastWinner = Integer.parseInt(socketIn.readLine());
					lblLastWinner.setLocation(75 + 150*lastWinner, 0);
					for(int i = 0; i < stacks.length; ++i) {
						stacks[i].empty();
					}
					break;
				case "win":
					int finalWinner = Integer.parseInt(socketIn.readLine());
					lblFinalWinner.setText(names[finalWinner] + " is the winner!");
					lblFinalWinner.setLocation(RedFlags.WIDTH/2, RedFlags.HEIGHT/2);
					socketOut.close();
					socketIn.close();
					socket.close();
					playing = false;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start(GameContainer container, String[] nameList, int players, Socket sock, PrintWriter out, BufferedReader in) {
		names = new String[players];
		lblNames = new Label[players];
		lblScores = new Label[players];
		stacks = new CardStack[players];
		names[0] = names[0] + " (Host)";
		for(int i = 0; i < players; ++i) {
			names[i] = nameList[i];
			lblNames[i] = new Label(container, names[i], 75 + 150*i, 15);
			components.add(lblNames[i]);
			lblScores[i] = new Label(container, "0 points", 75 + 150*i, 35);
			components.add(lblScores[i]);
			stacks[i] = new CardStack(redFlags, container, 75 + 150*i, 230, i);
			components.add(stacks[i]);
		}
		socket = sock;
		socketOut = out;
		socketIn = in;
	}
	
	public void cardClicked(String choice) {
		socketOut.println(choice);
		for(int i = 0; i < 8; ++i) {
			cards[i].setLocation(75 + 150*i, 610);
			cards[i].setHighlight(false);
		}
	}
	
	public void stackClicked(int id) {
		socketOut.println(id);
		for(int i = 0; i < stacks.length; ++i) {
			stacks[i].setHighlight(false);
		}
	}

	@Override
	public int getID() {
		return 3;
	}
}
