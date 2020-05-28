package st0ro.redflags.gui;

import javax.swing.JOptionPane;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.util.FontUtils;

import st0ro.redflags.RedFlags;

public class Card extends AbstractComponent {
	
	private RoundedRectangle hitbox;
	private Color color;
	private Label label;
	private boolean highlight, visible;
	private String text;
	
	private RedFlags redFlags;

	public Card(RedFlags redFlags, GUIContext container, int x, int y, Color color) {
		super(container);
		this.redFlags = redFlags;
		hitbox = new RoundedRectangle(x - 70, y - 90, 140, 180, 8);
		this.color = color;
		label = new Label(container, "", x - 65, y - 85, 130);
		label.setAlignment(FontUtils.Alignment.LEFT);
		highlight = false;
	}
	
	public Card(RedFlags redFlags, GUIContext container, int x, int y, Color color, String text) {
		super(container);
		this.redFlags = redFlags;
		hitbox = new RoundedRectangle(x - 70, y - 90, 140, 180, 8);
		this.color = color;
		this.text = text;
		label = new Label(container, text, x - 65, y - 85, 130);
		label.setAlignment(FontUtils.Alignment.LEFT);
		highlight = false;
	}
	
	public void setHighlight(boolean set) {
		highlight = set;
	}
	
	public void setText(String text) {
		this.text = text;
		label.setText(text);
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int times) {
		{
			if(visible && button == 0 && times == 1 && highlight && isAcceptingInput() && hitbox.contains(x, y)) {
				onLeftClick();
				consumeEvent();
			}
		}
	}
	
	public void onLeftClick() {
		if(text.equals("<?>")) {
			String input = JOptionPane.showInputDialog("Enter your wildcard:");
			if(input == null || input.isEmpty()) {
				return;
			}
			redFlags.playState.cardClicked("<?>" + input);
			visible = false;
			highlight = false;
			return;
		}
		redFlags.playState.cardClicked(text);
		visible = false;
		highlight = false;
	}
	
	public void setVisible(boolean vis) {
		visible = vis;
	}

	@Override
	public int getHeight() {
		return (int) hitbox.getHeight();
	}

	@Override
	public int getWidth() {
		return (int) hitbox.getWidth();
	}

	@Override
	public int getX() {
		return (int) hitbox.getCenterX();
	}

	@Override
	public int getY() {
		return (int) hitbox.getCenterY();
	}

	@Override
	public void render(GUIContext container, Graphics g2d) throws SlickException {
		if(visible) {
			g2d.setColor(color);
			g2d.fill(hitbox);
			g2d.setColor(Color.black);
			g2d.draw(hitbox);
			if(highlight && hitbox.contains(container.getInput().getMouseX(), container.getInput().getMouseY())) {
				g2d.setLineWidth(5);
				g2d.setColor(Color.cyan);
				g2d.draw(hitbox);
				g2d.setLineWidth(1);
			}
			label.render(container, g2d);
		}
	}

	@Override
	public void setLocation(int x, int y) {
		if(hitbox != null) {
			hitbox.setCenterX(x);
			hitbox.setCenterY(y);
			label.setLocation(x - 65, y - 85);
		}
	}
}
