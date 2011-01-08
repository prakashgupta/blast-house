package blasthouse.characters;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import blasthouse.main.UpdatingGameElement;

public class Mob implements UpdatingGameElement{
	private String name = null;
	private int currHP, maxHP, spriteLocX, spriteLocY, currSpriteRow, currSpriteCol;
	private SpriteSheet spriteSheet;
	private boolean spriteHFlip = false;
	private Rectangle bounds;
	
	public Mob(){ }
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		spriteSheet.getSprite(currSpriteCol, currSpriteRow).getFlippedCopy(spriteHFlip, false).draw(spriteLocX, spriteLocY);		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		bounds.setBounds(spriteLocX, spriteLocY, spriteSheet.getSprite(currSpriteCol, currSpriteRow).getWidth(), spriteSheet.getSprite(currSpriteCol, currSpriteRow).getHeight());		
	}

}
