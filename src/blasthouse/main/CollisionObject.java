package blasthouse.main;

import javax.vecmath.Vector2f;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public class CollisionObject extends Image implements UpdatingGameElement{
	private Rectangle bounds = null;
	
	public CollisionObject(){ this(new Rectangle(0,0,120,120)); }
	public CollisionObject(String name){ this(name, new Rectangle(0,0,120,120), null); }
	public CollisionObject(Rectangle rec){ this(null, rec, null); }
	public CollisionObject(Rectangle rec, Image image){ this(null, rec, image); }
	public CollisionObject(String name, Rectangle rec, Image image){
		super(image);
		this.bounds = rec;
		this.setName(name);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		this.draw(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
	}
	
	/*** Sets the upper left corner of the image to this Vector
	 * @param vec The X,Y vector to set the corner to */
	public void setLocation(Vector2f vec){ setLocation(vec.x, vec.y); }	
	/*** Sets the upper left corner of the image to X, Y
	 * @param x The horizontal location of the new corner
	 * @param y The vertical location of the new corner */
	public void setLocation(float x, float y){
		bounds.setX(x);
		bounds.setY(y);
	}
	
	public void setBounds(Rectangle bnds){ setBounds(bnds.getX(), bnds.getY(), bnds.getWidth(), bnds.getHeight()); }
	public void setBounds(float x, float y, float w, float h){
		bounds.setX(x);
		bounds.setY(y);
		bounds.setWidth(w);
		bounds.setHeight(h);
	}
	
	public Rectangle getBounds(){ return bounds; }
}
