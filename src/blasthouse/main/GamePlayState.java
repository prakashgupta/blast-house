package blasthouse.main;

import blasthouse.characters.Mob;
import blasthouse.characters.Player;
import blasthouse.main.Ability;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.ShapeRenderer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
 
public class GamePlayState extends BasicGameState implements KeyListener{
	// Hardcoded variables for player movement
	public static final Vector2f GRAVITY = new Vector2f(0, 0.03f);
	public static final float FRICTION = 0.006f;
	// Should contain all collidables
	public static Vector<UpdatingGameElement> GameObjects = new Vector<UpdatingGameElement>();
	
	private String[] mapArray;	// Holds the mapfile chars
	private Image background = null, floorImage = null, wallImage = null, spikesImage = null; // Image holders	
	private Vector<UpdatingGameElement> Actors = new Vector<UpdatingGameElement>();	// Holds the players (only 1 for now)
	private Player myPlayer;	// Created object to hold stats, utilize controls, movements, player info
	int stateID = -1, timer = 0;
    private Logger logger = Logger.getLogger("BlastHouse Logger");	// Unused so far
    
    
    GamePlayState( int stateID ) {
       this.stateID = stateID;      
    }
 
    @Override
    public int getID() { return 3; }
 
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    	background = new Image("snwmountain.jpg");	
    	floorImage = new Image("IceBlock.png");			// Set up the images we use for terrain
    	wallImage = new Image("IceWall.png");
    	spikesImage = new Image("IceSpikes.png");
    	
    	
    	// Setup initial player
    	myPlayer = new Player();
    	myPlayer.setPlayerSpriteSheet(new SpriteSheet( new Image("Full-Idle.png"), 190, 160, 1, 1) );
    	myPlayer.setPlayerBounds(190, 160);
    	myPlayer.addAbility( new Ability("GorillaYell", 2, 0, 4, 200) );
    	myPlayer.addAbility( new Ability("GorillaPunch", 4, 0, 5, 200) );
    	myPlayer.addAbility( new Ability("GorillaJump", 1, 0, 5, 200) );
    	myPlayer.addPlayerStat("jumpstrength", -3f);
    	myPlayer.addPlayerStat("jumpcount", 2f);
    	myPlayer.addPlayerStat("hp", 100f);
    	myPlayer.addPlayerStat("speed", 0.1f);
    	myPlayer.addPlayerStat("strength", 14f);		
		
    	Actors.add(myPlayer);
		
		loadMap();
    }
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
    	// Background first, farthest back ZOrder, doesn't move with level
    	background.draw(0, 0);	
    	
    	// Keeps "camera" centered on character
    	gc.getGraphics().translate( -1*(myPlayer.getPlayerSpriteX() - gc.getScreenWidth()/4), -1*(myPlayer.getPlayerSpriteY() - gc.getScreenHeight()/4));    	
		
    	// Render all shapes, blocks, etc
    	for( UpdatingGameElement prop : GameObjects){ 
    		if(prop instanceof CollisionObject){
    			((CollisionObject)prop ).render(gc, sbg, g); 
    			//ShapeRenderer.draw(((CollisionObject)prop ).getBounds());
    		}
    	}
    	// Render for all players
    	// TODO: use similar implementation for mobs?
    	for( UpdatingGameElement p : Actors ){ p.render(gc, sbg, g); }
    	
    	//ShapeRenderer.draw(myPlayer.getBounds());
    	
    }
    
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {    	
    	timer += delta;
    	// Update all players involved
  
    	for( UpdatingGameElement p : Actors ){ p.update(gc, sbg, delta); }
 
    }     
   
    public void loadMap(){
    	//TODO: read each char in textfile into array
    	BufferedReader in;
    	int mapWidth = 0, mapHeight = 0;
    	
    	try{
    		in = new BufferedReader(new FileReader("src/LevelOne.txt"));
    		mapWidth = Integer.parseInt(in.readLine());
    		mapHeight = Integer.parseInt(in.readLine());
    		
    		mapArray = new String[mapHeight];
    		logger.info("Loaded map: ");
    		for(int n = 0; n < mapHeight; n++){
    			mapArray[n] = in.readLine();  
    			System.out.println(mapArray[n]);
    		}    		
    		
    	}catch(NumberFormatException e){ logger.log(Level.SEVERE, "Map File Format Problem", e); }
    	catch(FileNotFoundException e){ logger.log(Level.SEVERE, "Map File Not Found", e); }
    	catch(IOException e){ logger.log(Level.SEVERE, "Map File IO Error", e); }
    	
    	//TODO: build level from that array
    	for(int i = 0; i < mapWidth; i++){
    		for(int n = 0; n < mapHeight; n++){
    			switch(mapArray[n].charAt(i)){
    				case 'X':
    					GameObjects.add(new CollisionObject("wall", new Rectangle(i*120, n*120, 120, 120), wallImage));
    					break;
    				case '_':
    					GameObjects.add(new CollisionObject("platform", new Rectangle(i*120, n*120+80, 120, 40), floorImage));
    					break;
    				case '0':
    					myPlayer.setPlayerSpriteX(i*120 + 60);
    					myPlayer.setPlayerSpriteY(n*120 + 60);
    					break;
    				case '*':
    					GameObjects.add(new CollisionObject("spikes", new Rectangle(i*120, n*120, 120, 120), spikesImage));
    					break;
    				case '1':
    					GameObjects.add(new Mob());
    					break;
    				default:
    					break;
    			}
    		}
    	}
    }
    
    @Override
    public void keyPressed( int key, char c ){ myPlayer.keyPressed(key, c); }
    
    @Override
    public void keyReleased( int key, char c ){ myPlayer.keyReleased(key, c); }
}