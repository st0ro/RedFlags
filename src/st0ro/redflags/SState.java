package st0ro.redflags;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public abstract class SState extends BasicGameState {
	
	protected RedFlags redFlags;
	
	protected ArrayList<AbstractComponent> components = new ArrayList<AbstractComponent>();
	protected ShapeFill backgroundFill;
	
	protected SState(RedFlags redFlags) {
		this.redFlags = redFlags;
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g2d) throws SlickException {
		g2d.fill(RedFlags.background, backgroundFill);
		g2d.setAntiAlias(true);
		for(AbstractComponent c:components) {
			c.render(container, g2d);
		}
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		//empty so event based child classes don't need to have
	}
	
	public void enterState(GameContainer container, StateBasedGame game) {
		game.enterState(getID(), new FadeOutTransition(Color.black, 200), new FadeInTransition(Color.black, 200));
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		for(AbstractComponent ac: components) {
			ac.setAcceptingInput(true);
		}
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game) {
		for(AbstractComponent ac: components) {
			ac.setAcceptingInput(false);
		}
	}
}
