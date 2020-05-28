package st0ro.redflags;

import java.awt.Font;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public class RedFlags extends StateBasedGame {

	public MainMenuState mainMenuState;
	public JoinState joinState;
	public LobbyState lobbyState;
	public PlayState playState;
	
	public static Rectangle background;
	public static GradientFill backgroundFill;
	
	public String name;
	
	public static UnicodeFont fontSegoe14;
	
	public static final int WIDTH = 1200, HEIGHT = 600;
	public static boolean debug_mode = false;
	
	public RedFlags() {
		super("Red Flags");
		mainMenuState = new MainMenuState(this);
		joinState = new JoinState(this);
		lobbyState = new LobbyState(this);
		playState = new PlayState(this);
		background = new Rectangle(0, 0, WIDTH, HEIGHT);
		backgroundFill = new GradientFill(WIDTH/2, 0, Color.red, WIDTH/2, HEIGHT/2, Color.white);
		name = "";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		if(debug_mode) {
			name = "user" + (int)(Math.random()*1000);
		}
		addState(mainMenuState);
		addState(joinState);
		addState(lobbyState);
		addState(playState);
		container.setVSync(true);
		fontSegoe14 = new UnicodeFont(new java.awt.Font("Segoe UI", Font.PLAIN, 14));
		fontSegoe14.addAsciiGlyphs();
		fontSegoe14.getEffects().add(new ColorEffect(java.awt.Color.BLACK));
		fontSegoe14.loadGlyphs();
		container.setDefaultFont(fontSegoe14);
		container.setShowFPS(false);
		container.setAlwaysRender(true);
	}

	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new RedFlags()); //create game in container
			app.setDisplayMode(WIDTH, HEIGHT, false); //set window size and if fullscreen
			app.start();
		} catch (SlickException e) {
			e.printStackTrace(); //in case of failure to start game, will print error
		}
	}

}
