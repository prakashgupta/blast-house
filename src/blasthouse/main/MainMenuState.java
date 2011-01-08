package blasthouse.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
 
@SuppressWarnings("unused")
public class MainMenuState extends BasicGameState {
 
    int stateID = -1, bwX = -250, bwY = 100, hwX = 1250, hwY = 145, scaleTracker = 0, imageDelayInMS = 75;
    Image mainMenu = null;
    Image blastWord = null, blastImage = null, houseWord = null, houseImage = null;
    float blastScale = 0, houseScale = 0, timer = 0, lastTimer = 0;
    float [] scaleValues = {0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 0.9f, 0.8f, 0.7f, 0.6f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 0.9f, 0.8f, 0.7f};
    Audio sound = null, music = null;
    boolean played = false;
    
    MainMenuState( int stateID ) {
       this.stateID = stateID;
    }
 
    @Override
    public int getID() { return 0; }
 
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    	mainMenu = new Image("mainMenu.jpg");
    	blastWord = new Image("blastWord.png");
    	blastImage = new Image("blastImage.png");
    	houseWord = new Image("houseWord.png");
    	houseImage = new Image("houseImage.png");
    	
    	
		try {
			sound = AudioLoader.getAudio("WAV",
			        new FileInputStream("src/blasthouse/sounds/bomb-02.wav"));
			//music = AudioLoader.getAudio("OGG", 
				//	new FileInputStream("src/blasthouse/sounds/metalman_goes_clubbing.ogg"));
		} catch (FileNotFoundException e) { e.printStackTrace();
		} catch (IOException e) { e.printStackTrace(); }
   
    	blastImage.draw(50, 100, blastScale);
    	houseImage.draw(150, 100, houseScale);    	  	
    }
 
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
    	mainMenu.draw(0, 0); 
    	blastImage.draw(225 - blastImage.getWidth()*blastScale/2, 150 - blastImage.getHeight()*blastScale/2, blastScale);
    	houseImage.draw(550 - houseImage.getWidth()*houseScale/2, 250 - houseImage.getHeight()*houseScale/2, houseScale); 
    	blastWord.draw(bwX, bwY);
    	houseWord.draw(hwX, hwY);    	
    }
 
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
    	lastTimer = timer;
    	timer += delta;
    	
    	Input input = gc.getInput();
    	int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();

    	if( bwX < 100 ){ bwX += 1; }
    	if( hwX > 325 ){ hwX -= 1; }
    	
    	if( bwX == 100 && hwX == 325){
    		if(played == false){ 
    			playSounds();
    			played = true;
    		}
    		loopImages();
    	}    	
    	
    	// Input responses
    	if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
            sbg.enterState(BlastHouse.GAMEPLAYSTATE);
        }

    } 
    
    public void playSounds(){
    	sound.playAsSoundEffect(1.0f, 1.0f, false);   
    	//music.playAsMusic(1f, 1f, true);
    }
    
    public void loopImages(){    	
		if(scaleTracker > scaleValues.length - 1){ scaleTracker = scaleValues.length - 1; }    		
		
		if(timer > imageDelayInMS){
			houseScale = scaleValues[scaleTracker];
			blastScale = scaleValues[scaleTracker];
	
			scaleTracker++;
			timer = 0;
		}
	}
}
