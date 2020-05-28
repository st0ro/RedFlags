package st0ro.redflags.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.util.FontUtils;

import st0ro.redflags.RedFlags;

public class Label extends AbstractComponent {
	
	private String text;
	private int x, y, width;
	private boolean wrapText;
	private int alignment;
	private Font font;
	
	public Label(GUIContext container, String text, int x, int y) {
		super(container);
		this.x = x;
		this.y = y;
		wrapText = false;
		setText(text);
		alignment = FontUtils.Alignment.CENTER;
		font = RedFlags.fontSegoe14;
	}

	public Label(GUIContext container, String text, int x, int y, int width) {
		super(container);
		this.x = x;
		this.y = y;
		this.width = width;
		wrapText = true;
		setText(text);
		alignment = FontUtils.Alignment.CENTER;
		font = RedFlags.fontSegoe14;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void render(GUIContext container, Graphics g2d) throws SlickException {
		if(text != null && !text.isEmpty()) {
			FontUtils.drawString(font, text, alignment, x, y, 0, Color.black);
		}
	}

	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setText(String newText) {
		this.text = newText;
		if(wrapText) {
			text = "";
			String[] words = newText.split(" ");
			String line = "";
			int index;
			for(index = 0; index < words.length; index++) {
				for(line = words[index]; index + 1 < words.length && RedFlags.fontSegoe14.getWidth(line + " " +words[index + 1]) < width; index++) {
					line += " " + words[index + 1];
				}
				text += line + "\n";
			}
		}
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public void setAlignment(int align) {
		this.alignment = align;
	}
	public void setWrap(boolean wrap) {
		this.wrapText = false;
		setText(text);
	}
}
