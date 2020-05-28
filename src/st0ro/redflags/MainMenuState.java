package st0ro.redflags;

import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.FontUtils;

import st0ro.redflags.gui.Button;
import st0ro.redflags.gui.Label;

public class MainMenuState extends SState {
	
	private Label lblName;
	
	public MainMenuState(RedFlags redFlags) {
		super(redFlags);
	}
		
	@Override
	public void init(GameContainer gContainer, StateBasedGame game) throws SlickException {
		backgroundFill = RedFlags.backgroundFill;
		while(redFlags.name.isEmpty()) {
			redFlags.name = JOptionPane.showInputDialog("Enter a name:");
		}
		components.add(new Button(gContainer, "Host Game", RedFlags.WIDTH/2, 400, 100, 40) {
			@Override
			public void onLeftClick() {
				redFlags.lobbyState.hostLobby();
				try {
					redFlags.lobbyState.joinParty(new Socket("localhost", 25565));
				} catch (IOException e) {
					e.printStackTrace();
				}
				redFlags.lobbyState.enterState(gContainer, game);
			}
		});
		components.add(new Button(gContainer, "Join Game", RedFlags.WIDTH/2, 450, 100, 40) {
			@Override
			public void onLeftClick() {
				redFlags.joinState.enterState(gContainer, game);
			}
		});
		components.add(new Button(gContainer, "Change name", 80, 540, 125, 40) {
			public void onLeftClick() {
				String temp = JOptionPane.showInputDialog("Enter a name:");
				if(temp != null && !temp.isEmpty()) {
					redFlags.name = temp;
					lblName.setText("Hello, "+ temp);
				}
			}
		});
		lblName = new Label(gContainer, "Hello, " + redFlags.name, 20, 510, 100);
		lblName.setAlignment(FontUtils.Alignment.LEFT);
		lblName.setWrap(false);
		components.add(lblName);
		components.add(new Label(gContainer, "Red Flags", RedFlags.WIDTH/2, 200, 100));
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		//RedFlags.playState.enterState(container, game);
	}

	@Override
	public int getID() {
		return 0;
	}

}
