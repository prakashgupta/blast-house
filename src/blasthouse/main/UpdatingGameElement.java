package blasthouse.main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public abstract interface UpdatingGameElement {
	public abstract void update(GameContainer container, StateBasedGame game, int delta) throws SlickException;
	public abstract void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException;
}
