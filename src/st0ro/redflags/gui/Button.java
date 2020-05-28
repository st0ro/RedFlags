package st0ro.redflags.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.util.FontUtils;

public abstract class Button extends AbstractComponent {

	protected Rectangle hitbox;
	protected Label text;
	
	public Button(GUIContext container, String text, int x, int y, int width, int height) {
		super(container);
		this.text = new Label(container, text, x, y + 7, width);
		this.text.setAlignment(FontUtils.Alignment.CENTER);
		hitbox = new Rectangle(x - width/2, y, width, height);
		setAcceptingInput(false);
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
		return (int) hitbox.getX();
	}

	@Override
	public int getY() {
		return (int) hitbox.getY();
	}

	@Override
	public void render(GUIContext container, Graphics g2d) throws SlickException {
		g2d.setColor(Color.white);
		g2d.fill(hitbox);
		g2d.setColor(Color.red);
		g2d.setLineWidth(5);
		g2d.draw(hitbox);
		text.render(container, g2d);
	}

	@Override
	public void setLocation(int x, int y) {
		if(hitbox != null) {
			hitbox.setCenterX(x);
			hitbox.setY(y);
			text.setLocation(x, y + 7);
		}
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int times) {
		{
			if(button == 0 && times == 1 && isAcceptingInput() && hitbox.contains(x, y)) {
				onLeftClick();
				consumeEvent();
			}
		}
	}
	
	public abstract void onLeftClick();
}
