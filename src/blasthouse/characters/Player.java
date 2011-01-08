package blasthouse.characters;

import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import blasthouse.main.Ability;
import blasthouse.main.CollisionObject;
import blasthouse.main.GamePlayState;
import blasthouse.main.UpdatingGameElement;
import blasthoust.effects.SpellEffect;

public class Player implements UpdatingGameElement{
	private String name;
	private SpriteSheet punchExplosionImage = null, playerSpriteSheet = null;
	private int currHealth, maxHealth, currSpriteRow, currSpriteCol, timer = 0;
	private float playerSpriteX, playerSpriteY, currJumpCount = 0f;
	private Vector<Ability> Abilities = new Vector<Ability>();
	private Rectangle playerBounds = new Rectangle(0, 0, 0, 0);
	private boolean spriteHorizontalFlip = false, spriteVerticalFlip = false, moveLeft = false, moveRight = false, onGround = true, dead = false;
	private HashMap<String, Float> playerStats = new HashMap<String, Float>();
	private Vector2f spriteDirection = new Vector2f(0, 0);
	private Ability gorillaMoving = new Ability("gorillamoving", 1, 0, 5), gorillaIdle = new Ability("gorillaidle", 0, 0, 6);
	private Logger logger = Logger.getLogger("player logger");
	private Vector<UpdatingGameElement> associatedEffects = new Vector<UpdatingGameElement>();
	
	public Player(){
		// Defaulted values SHOULD BE SET IN GAME INIT!
		playerStats.put("jumpstrength", -2.0f);
		playerStats.put("jumpcount", 1.0f);
		playerStats.put("hp", 100f);
		playerStats.put("speed", 0.5f);
		playerStats.put("strength", 14f);
		
		try {
			punchExplosionImage = new SpriteSheet( new Image("SmallExplosion.png"), 52, 52 );
			System.out.println("punch image loaded!");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException{
		timer += delta;
		Input input = container.getInput();
		
		gorillaMoving.setActive(false);	// Resets moving pending inputs  
		
		playerBounds.setBounds(playerSpriteX + (spriteHorizontalFlip ? 40 : 40), playerSpriteY, 110, 135);

		// These make sure you can resume walking after an ability without repressing a key    	
		if( input.isKeyDown(Input.KEY_A) ){ keyPressed(Input.KEY_A, new Character('a')); }
    	if( input.isKeyDown(Input.KEY_D) ){ keyPressed(Input.KEY_D, new Character('d')); } 
    	
    	if( !input.isKeyDown(Input.KEY_A) ){ keyReleased(Input.KEY_A, new Character('a')); }
    	if( !input.isKeyDown(Input.KEY_D) ){ keyReleased(Input.KEY_D, new Character('d')); }
    	
    	// Sets animation to idle or moving when the player is doing nothing
    	if( playerAvailable() ){ currSpriteRow = (gorillaMoving.isActive() ? 1 : 0); }
    	else{
	    	for( Ability a : Abilities){
				if( a.isActive() ){ currSpriteRow = a.getAnimationRow(); }
			}
    	}
    	
    	// Increments the sprite animation based on the speed of the ability
    	if( timer > getCurrentAction().getSpeed() ){
        	logger.info("INCREMENT SPRITE COL");
        	currSpriteCol++;    
        	// TODO: Check effects to trigger? Create "particle" class? create new one and pass it 
        	// the image to animate, # of frames?, cycles? some duration?
        	if( getCurrentAction().getName().equalsIgnoreCase("gorillapunch") && currSpriteCol == 3 ){
        		this.associatedEffects.add(new SpellEffect( this, punchExplosionImage, 120, 20, 0, 0, 
        				punchExplosionImage.getHorizontalCount() -  1, 30, this.getSpriteFlip(), 
        				2.0f));
        	}
    		timer = 0;
        } 
    	
    	// Resets loops based on animation row
    	System.out.println("Current sprite row: " + currSpriteRow + " current column " + currSpriteCol);
    	if( currSpriteCol >= getCurrentAction().getAnimColStop() ){ 
    		if( getCurrentAction().getName().equalsIgnoreCase("dead") ){ currSpriteCol =2; }
    		else{ 
    			currSpriteCol = 0;
    			setAbility( getCurrentAction().getName(), false );
    		} 
    		
    	}
    	
    	// Calculate movement amount and jump "thrust"  	
    	if( moveLeft && playerAvailable() ){ spriteDirection.add(new Vector2f(-playerStats.get("speed"), 0)); }
    	if( moveRight && playerAvailable() ){ spriteDirection.add(new Vector2f(playerStats.get("speed"), 0)); }    	
    	if( !playerAvailable() && !dead ){
    		if( getCurrentAction().getName().equalsIgnoreCase("gorillajump") && currJumpCount < playerStats.get("jumpcount")){
    			//spriteDirection.add( new Vector2f( 0, (spriteDirection.y == 0 ? ( onGround ? playerStats.get("jumpstrength") : 0) : 0) ) );
    			spriteDirection.set(new Vector2f(spriteDirection.getX(), playerStats.get("jumpstrength")));
    			//spriteDirection.add( new Vector2f( 0, playerStats.get("jumpstrength")));
    			setAbility("gorillajump", false);    			
    		}
    	}

    	//spriteLocX += spriteDirection.getX()*gorillaSpeed;	// old way to increase player speed
    	
    	// Collides with floor/map
    	onGround = false;
    	for( UpdatingGameElement floor : GamePlayState.GameObjects ){    		
    		if(floor instanceof CollisionObject){ 
    			// Check vertical collisioning (read: falling)
    			if( !onGround && Math.abs(playerBounds.getMaxY() - ((CollisionObject)floor).getBounds().getMinY()) < 2 && 
    				( (playerBounds.getMinX() > ((CollisionObject)floor).getBounds().getMinX() && playerBounds.getMinX() < ((CollisionObject)floor).getBounds().getMaxX()) 
    						|| (playerBounds.getMaxX() < ((CollisionObject)floor).getBounds().getMaxX() && playerBounds.getMaxX() > ((CollisionObject)floor).getBounds().getMinX()))){    			
    				onGround = true;
    				setAbility("gorillajump", false);
    				currJumpCount = 0;
    				
    				// If the player is on something, and that something is spikes... kill him
    				if(onGround && ((CollisionObject) floor).getName().equalsIgnoreCase("spikes") ){
    					spriteDirection.set(new Vector2f(0,0));
    					dead = true;
    				}
    			}
    			// TODO: check horizontal collisioning
    			if( ((CollisionObject) floor).getBounds().intersects(playerBounds) ){
    				Vector2f temp = new Vector2f();
    				temp.set(((CollisionObject) floor).getBounds().getCenterX(), ((CollisionObject) floor).getBounds().getCenterY());    				
    				temp.sub(new Vector2f(playerBounds.getCenterX(), playerBounds.getCenterY()));
    				if( Math.abs(temp.x) > Math.abs(temp.y)){ 
    					spriteDirection.x = 0;
    					playerSpriteX += (temp.x >= 0 ? -1:1); 
    				}
    				else{ 
    					spriteDirection.y = 0; 
    					playerSpriteY += (temp.y >= 0 ? -1:1); }
    			}
    			
    		}
    	}
    	
    	// Very loaded process going on below to simulate inertia on ice
       	spriteDirection.add( new Vector2f( 
    			spriteHorizontalFlip ? 	// Check which way the gorilla faces
    					((spriteDirection.getX() < 0) ? GamePlayState.FRICTION : -GamePlayState.FRICTION) : 	// if he's going left slow his movement unless he's stopped
    						((spriteDirection.getX() > 0) ? -GamePlayState.FRICTION : GamePlayState.FRICTION) , 	// likewise incase he's going right
    							0 ) );	// lastly don't touch the Y thrust
       	// Apply gravity
       	if(!onGround){ spriteDirection.add( GamePlayState.GRAVITY ); }	
       	
       	// Check for top speed and restrict to it, both vertical and horizontal
       	spriteDirection.set( ( spriteDirection.getX() > 2.0f ? 2.0f : ( spriteDirection.getX() < -2.0f ? -2.0f : spriteDirection.getX() ) ),	// Limits horizontal movement to 0.5f and determines directions before limiting speed 
       			( onGround ? // Vertical restraint checks is onGround first, then limits fall speed to 2 while leaving jump speed alone
       					(spriteDirection.getY() > 0 ? 0 : spriteDirection.getY()) : 
       						( spriteDirection.getY() > 4 ? 4f : spriteDirection.getY() ) ) );
       	
       	if(dead){	// If he's dead he can fall, but no more moving left/right
       		spriteDirection.set(0, spriteDirection.getY());
       		currSpriteRow = 5;
       	}
       	// Apply the speed of gorilla to the location of gorilla? 	
    	playerSpriteY += spriteDirection.getY();
    	playerSpriteX += spriteDirection.getX();    
    	
    	logger.info("Current row going out: " + getCurrentAction().getAnimationRow());
    	
    	for( UpdatingGameElement e : this.associatedEffects ){
			e.update(container, game, delta);
		}
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{		
		for( UpdatingGameElement e : this.associatedEffects ){
			e.render(gc, sbg, g);
		}
		
		playerSpriteSheet.getSprite(currSpriteCol, currSpriteRow).getFlippedCopy(spriteHorizontalFlip, spriteVerticalFlip).draw(playerSpriteX, playerSpriteY);
	}
	
	public void keyPressed(int key, char c) {
		if( key == Input.KEY_D && playerAvailable() ){
			gorillaMoving.setActive(true);
    		spriteHorizontalFlip = false;
    		moveRight = true;
    	}
    	if( key == Input.KEY_A && playerAvailable() ){
    		gorillaMoving.setActive(true);
    		spriteHorizontalFlip = true;
    		moveLeft = true;
    	}
    	if( key == Input.KEY_1 && playerAvailable() ){ 
    		setAbility("gorillayell", true);
    		//TODO: do efffect, hitbox, hits, etc
    	}
    	if( key == Input.KEY_2 && playerAvailable() ){ setAbility("gorillapunch", true); }
    	if( key == Input.KEY_ADD ){ this.addPlayerStat("speed", this.getPlayerStat("speed")+0.1f); }
    	if( key == Input.KEY_SUBTRACT ){ this.addPlayerStat("speed", this.getPlayerStat("speed")-0.1f); }
    	if( key == Input.KEY_SPACE && playerAvailable() && currJumpCount < playerStats.get("jumpcount")){ 
    		setAbility("gorillajump", true);
    		currJumpCount++;
    	}
		
	}
	
	public void keyReleased(int key, char c) {
		if( key == Input.KEY_D ){ moveRight = false; }
		if( key == Input.KEY_A ){ moveLeft = false; }
	}
	
	public boolean playerAvailable(){
    	if(dead){ return false; }
    	boolean Available = true;
    	for(Ability a : Abilities){
    		if(a.isActive()){
    			Available = false;
    			break;
    		}
    	}    	
    	return Available;
    }
	
	public Ability getCurrentAction(){
    	if( playerAvailable() ){ return (gorillaMoving.isActive() ? gorillaMoving : gorillaIdle); }
    	else{
	    	for( Ability a : Abilities){
				if( a.isActive() ){ return a; }
			}
    	}
    	return new Ability("dead", 5, 0, 2, 200);	// Returns when the player is dead
    }
	
    public void setAbility(String abilityName, boolean on){
    	for( Ability ability : Abilities ){
    		if(ability.getName().equalsIgnoreCase(abilityName)){ 
    			ability.setActive(on);
    			if( on ){ currSpriteCol = 0; }
    		}
    	}
    }
	////////////////////////
	// Getter and Setters //
	////////////////////////
	/*** @param playerSpriteX the player's sprite's X position to set */
	public void setPlayerSpriteX(float playerSpriteX) { this.playerSpriteX = playerSpriteX; }
	/*** @return the player's sprite's X position */
	public float getPlayerSpriteX() { return playerSpriteX; }
	
	/*** @param playerSpriteY the playerSpriteY to set */
	public void setPlayerSpriteY(float playerSpriteY) { this.playerSpriteY = playerSpriteY; }
	/*** @return the playerSpriteY */
	public float getPlayerSpriteY() { return playerSpriteY; }
	
	/*** @param currHealth the currHealth to set */
	public void setCurrHealth(int currHealth) { this.currHealth = currHealth; }
	/*** @return the currHealth */
	public int getCurrHealth() { return currHealth; }
	
	/*** @param maxHealth the maxHealth to set */
	public void setMaxHealth(int maxHealth) { if( maxHealth > 0 ){ this.maxHealth = maxHealth; } }
	/*** @return the maxHealth */
	public int getMaxHealth() { return maxHealth; }
	
	/*** @param name the name to set */
	public void setName(String name) { if( name != null ){ this.name = name; } }
	/*** @return the name */
	public String getName() { return name; }
	
	/*** @param playerSpriteSheet the playerSpriteSheet to set */
	public void setPlayerSpriteSheet(SpriteSheet playerSpriteSheet) { this.playerSpriteSheet = playerSpriteSheet; }
	/*** @return the playerSpriteSheet */
	public SpriteSheet getPlayerSpriteSheet() { return playerSpriteSheet; }
	
	/** @param currSpriteRow the currSpriteRow to set */
	public void setCurrSpriteRow(int currSpriteRow) { this.currSpriteRow = currSpriteRow; }
	/*** @return the currSpriteRow */
	public int getCurrSpriteRow() { return currSpriteRow; }
	
	/*** @param currSpriteCol the currSpriteCol to set */	
	public void setCurrSpriteCol(int currSpriteCol) { if( currSpriteCol >= 0 ){ this.currSpriteCol = currSpriteCol; }	}
	/*** @return the current sprite column on the sheet */
	public int getCurrSpriteCol() { return currSpriteCol; }	
	
	/*** @param faceRight Pass true to face sprite to the Left, false to face to the Right */
	public void setSpriteFlip(boolean faceLeft){ this.spriteHorizontalFlip = faceLeft; }
	/*** Returns whether of not the sprite is facing left */
	public boolean getSpriteFlip(){ return spriteHorizontalFlip; }
	
	public void setPlayerBounds(int width, int height){	playerBounds.setBounds(playerSpriteX, playerSpriteY, width, height); }
	public void movePlayerBounds(float playerSpriteX2, float playerSpriteY2){ playerBounds.setLocation(playerSpriteX2, playerSpriteY2); }
	public Rectangle getBounds(){ return playerBounds; }
	
	/*** @param ability The ability to add to the player's set */
	public void addAbility(Ability ability){ Abilities.add(ability); }
	/*** @param ability The ability to remove from the player's list */
	public void removeAbility(Ability ability){ Abilities.remove(ability); }
	/*** @effect Clears the entire ability list */
	public void clearAbilities(){ Abilities.clear(); }
	
	//TODO: methods to add new stats, adjust stats, remove stats?
	public float getPlayerStat(String stat){
		if( stat != null ){ return playerStats.get(stat); }
		return 0f;
	}
	public void addPlayerStat(String stat, float v){
		if( stat != null ){ playerStats.put(stat, v); }
	}
	/**
	 * @param s The name of the stat to remove
	 * @return True if the stat was found and removed, null if no stat existed (may return false if stat was set to null but no stat should have a null value)
	 * @author Eric
	 */
	public boolean removePlayerStat(String stat){
		if( playerStats.remove(stat) == null ){
			return false;
		}
		return true;
	}
}
