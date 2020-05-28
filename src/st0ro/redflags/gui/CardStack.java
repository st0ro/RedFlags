package st0ro.redflags.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.util.FontUtils;

import st0ro.redflags.RedFlags;

public class CardStack extends AbstractComponent {
	
	private RoundedRectangle hitbox;
	private Card white1, white2, red;
	private boolean highlight, target;
	private int id;
	
	private RedFlags redFlags;

	public CardStack(RedFlags redFlags, GUIContext container, int x, int y, int id) {
		super(container);
		this.redFlags = redFlags;
		this.id = id;
		hitbox = new RoundedRectangle(x - 70, y - 170, 140, 340, 8);
		empty();
	}
	
	public void add(String text) {
		if(white1 == null) {
			white1 = new Card(redFlags, container, (int) hitbox.getCenterX(), (int) hitbox.getCenterY() - 80, Color.white, text);
			white1.setVisible(true);
		}
		else if(white2 == null) {
			white2 = new Card(redFlags, container, (int) hitbox.getCenterX(), (int) hitbox.getCenterY(), Color.white, text);
			white2.setVisible(true);
		}
		else {
			red = new Card(redFlags, container, (int) hitbox.getCenterX(), (int) hitbox.getCenterY() + 80, Color.red, text);
			red.setVisible(true);
		}
	}
	
	public void empty() {
		white1 = null;
		white2 = null;
		red = null;
		target = false;
		highlight = false;
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int times) {
		{
			if(red != null && button == 0 && times == 1 && highlight && isAcceptingInput() && hitbox.contains(x, y)) {
				onLeftClick();
				consumeEvent();
			}
		}
	}
	
	public void onLeftClick() {
		redFlags.playState.stackClicked(id);
	}
	
	public void setHighlight(boolean set) {
		highlight = set;
	}

	@Override
	public int getHeight() {
		return 340;
	}

	@Override
	public int getWidth() {
		return 140;
	}

	@Override
	public int getX() {
		return (int) hitbox.getCenterX();
	}

	@Override
	public int getY() {
		return (int) hitbox.getCenterY();
	}
	
	public void setTarget(boolean h) {
		target = h;
	}

	@Override
	public void render(GUIContext container, Graphics g2d) throws SlickException {
		if(target) {
			g2d.setColor(Color.gray);
			g2d.fill(hitbox);
			FontUtils.drawString(RedFlags.fontSegoe14, "Target", FontUtils.Alignment.CENTER, getX(), getY(), 0, Color.black);
		}
		else if(white1 != null) {
			white1.render(container, g2d);
			if(white2 != null) {
				white2.render(container, g2d);
				if(red != null) {
					red.render(container, g2d);
					if(highlight && hitbox.contains(container.getInput().getMouseX(), container.getInput().getMouseY())) {
						g2d.setLineWidth(5);
						g2d.setColor(Color.cyan);
						g2d.draw(hitbox);
						g2d.setLineWidth(1);
					}
				}
			}
		}
	}

	@Override
	public void setLocation(int x, int y) {
		if(hitbox != null) {
			hitbox.setCenterX(x);
			hitbox.setCenterY(y);
		}
	}

}
