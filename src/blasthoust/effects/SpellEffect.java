package blasthoust.effects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.StateBasedGame;

import blasthouse.characters.Player;
import blasthouse.main.UpdatingGameElement;

public class SpellEffect implements UpdatingGameElement{
	private SpriteSheet effect;
	private Player parent;
	public float x, y, scale;
	private int timer = 0;
	private int animationRow, currAnimRow, currAnimCol, animStartCol, animStopCol, speed = 200;
	private boolean active = true, repeating = false, visible = true, collidable = false, 
		impeding = false, kill = false, animHFlip = false; 
	
	/** Constructs an effect that is;
	* 1-time, non-impeding, non-collidable, active, default speed of every 200ms frame-update
	*/
	public SpellEffect(Player parent, SpriteSheet sheet, float xFromSprite, float yFromSprite, int row, int startCol, int stopCol, int speed, boolean flip, float scale){
		this.parent = parent;
		this.setSpriteSheet(sheet);
		this.setAnimationRow(row);
		this.setCurrAnimRow(0);
		this.setCurrAnimCol(0);
		this.setAnimStartCol(startCol);
		this.setAnimStopCol(stopCol);
		this.setSpeed(speed);
		this.setAnimHFlip(flip);
		this.scale = scale;
		this.x = xFromSprite;
		this.y = yFromSprite;
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		// Renders the appropriate frame of the animation, if active
		if( isActive() ){ 
			effect.getSprite(getCurrAnimCol(), getCurrAnimRow())
			.getFlippedCopy(this.animHFlip, false)
			.draw((animHFlip ? parent.getPlayerSpriteX()+this.x : parent.getPlayerSpriteX()-(this.x-60)), 
					parent.getPlayerSpriteY()-this.y, scale); }
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		timer += delta;
		
		if( isActive() ){
			// Checks if it reached the end of the animation
			if( getCurrAnimCol() >= getAnimStopCol() ){
				if( isRepeating() ){	// Cycles incase it's a repeating animation
					setCurrAnimCol(0);
				}else{					// If it's not repeating, resets, and sets inactive
					setActive(false);	
					setCurrAnimCol(0);
				}
			}
			// Increment frame each speed-interval
			if( timer >= getSpeed() ){
				setCurrAnimCol( getCurrAnimCol() + 1 );
				timer =0;
			}
		}		
	}
	
	/////////////////////////
	// Getters and Setters //
	/////////////////////////
	
	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}
	/**
	 * @param repeating the repeating to set
	 */
	public void setRepeating(boolean repeating) {
		this.repeating = repeating;
	}
	/**
	 * @return the repeating
	 */
	public boolean isRepeating() {
		return repeating;
	}
	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}
	/**
	 * @param collidable the collidable to set
	 */
	public void setCollidable(boolean collidable) {
		this.collidable = collidable;
	}
	/**
	 * @return the collidable
	 */
	public boolean isCollidable() {
		return collidable;
	}
	/**
	 * @param impeding the impeding to set
	 */
	public void setImpeding(boolean impeding) {
		this.impeding = impeding;
	}
	/**
	 * @return the impeding
	 */
	public boolean isImpeding() {
		return impeding;
	}
	/**
	 * @param animationRow the animationRow to set
	 */
	public void setAnimationRow(int animationRow) {
		this.animationRow = animationRow;
	}
	/**
	 * @return the animationRow
	 */
	public int getAnimationRow() {
		return animationRow;
	}
	/**
	 * @param animStartCol the animStartCol to set
	 */
	public void setAnimStartCol(int animStartCol) {
		this.animStartCol = animStartCol;
	}
	/**
	 * @return the animStartCol
	 */
	public int getAnimStartCol() {
		return animStartCol;
	}
	/**
	 * @param animStopCol the animStopCol to set
	 */
	public void setAnimStopCol(int animStopCol) {
		this.animStopCol = animStopCol;
	}
	/**
	 * @return the animStopCol
	 */
	public int getAnimStopCol() {
		return animStopCol;
	}
	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	/**
	 * @return the speed
	 */
	public int getSpeed() {
		return speed;
	}
	/**
	 * @param currAnimCol the currAnimCol to set
	 */
	public void setCurrAnimCol(int currAnimCol) {
		this.currAnimCol = currAnimCol;
	}
	/**
	 * @return the currAnimCol
	 */
	public int getCurrAnimCol() {
		return currAnimCol;
	}
	/**
	 * @param currAnimRow the currAnimRow to set
	 */
	public void setCurrAnimRow(int currAnimRow) {
		this.currAnimRow = currAnimRow;
	}
	/**
	 * @return the currAnimRow
	 */
	public int getCurrAnimRow() {
		return currAnimRow;
	}
	/**
	 * @param effect the effect to set
	 */
	public void setSpriteSheet(SpriteSheet effect) {
		this.effect = effect;
	}
	/**
	 * @return the effect
	 */
	public SpriteSheet getSpriteSheet() {
		return effect;
	}

	/**
	 * @param animHFlip the animHFlip to set
	 */
	public void setAnimHFlip(boolean animHFlip) {
		this.animHFlip = animHFlip;
	}

	/**
	 * @return the animHFlip
	 */
	public boolean isAnimHFlip() {
		return animHFlip;
	}

}
