package blasthouse.main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameSelectState extends BasicGameState{
	Image mainMenu = null;
	
	@Override
	public int getID() { return 1; }

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		mainMenu = new Image("mainMenu.jpg");
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics g)
			throws SlickException {
		mainMenu.draw(0, 0);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame sbg, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		
	}

}
