package st0ro.redflags;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import javax.swing.JOptionPane;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.FontUtils;

import st0ro.redflags.gui.Button;
import st0ro.redflags.gui.Label;

public class JoinState extends SState {
	
	private TextField serverField;
	
	public JoinState(RedFlags redFlags) {
		super(redFlags);
	}

	@Override
	public void init(GameContainer gContainer, StateBasedGame game) throws SlickException {
		backgroundFill = RedFlags.backgroundFill;
		components.add(new Button(gContainer, "Back", RedFlags.WIDTH/2, 450, 100, 40) {
			@Override
			public void onLeftClick() {
				redFlags.mainMenuState.enterState(gContainer, game);;
			}
		});
		components.add(new Button(gContainer, "Join", RedFlags.WIDTH/2, 400, 100, 40) {
			@Override
			public void onLeftClick() {
				if(serverField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please enter a server.");
				}
				else {
					try {
						Socket socket = new Socket(serverField.getText(), 25565);
						redFlags.lobbyState.joinParty(socket);
						redFlags.lobbyState.enterState(gContainer, game);
					} catch(ConnectException e) {
						JOptionPane.showMessageDialog(null, "No lobby found! Check the IP, or the lobby might be full.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		serverField = new TextField(gContainer, gContainer.getDefaultFont(), RedFlags.WIDTH/2 + 15, 275, 200, 25);
		serverField.setBackgroundColor(null);
		serverField.setTextColor(Color.black);
		serverField.setAcceptingInput(false);
		components.add(serverField);
		Label lblHost = new Label(gContainer, "Host address:", RedFlags.WIDTH/2 - 15, 275, 100);
		lblHost.setAlignment(FontUtils.Alignment.RIGHT);
		components.add(lblHost);
		components.add(new Label(gContainer, "Join Game", RedFlags.WIDTH/2, 180, 100));
	}

	@Override
	public int getID() {
		return 1;
	}

}
