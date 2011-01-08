package blasthouse.main;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

// Blast House 2D multi-player side-scroller
// Begun December 1st, 2010
// Jan 5th - Started AI
public class BlastHouse extends StateBasedGame{
	public static final int MAINMENUSTATE          = 0;
	public static final int GAMESELECTSTATE        = 1;
	public static final int CONNECTSTATE           = 2;
	public static final int GAMEPLAYSTATE          = 3;
        
	public BlastHouse() {
		super("Blast House - Verison 0.2");
		
		this.addState(new MainMenuState(MAINMENUSTATE));
        this.addState(new GamePlayState(GAMEPLAYSTATE));
        this.enterState(MAINMENUSTATE);
	}

	@Override
    public void initStatesList(GameContainer gameContainer) throws SlickException { 
        //this.getState(MAINMENUSTATE).init(gameContainer, this);
        //this.getState(GAMEPLAYSTATE).init(gameContainer, this);
    }
	
	public static void main(String[] args) throws SlickException{
		AppGameContainer app = 
			new AppGameContainer(new BlastHouse());
 
         app.setDisplayMode(800, 600, false);
         app.start();
	}

}
