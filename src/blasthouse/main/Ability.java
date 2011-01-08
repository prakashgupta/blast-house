package blasthouse.main;

public class Ability {
	private String name;
	private boolean isActive;
	private int animationRow, speed, animColStart, animColStop;
	
	public Ability(String n, int row, int start, int stop){ this(n, row, start, stop, 200); }
	public Ability(String n, int row, int start, int stop, int speed){ 
		this.setName(n);
		this.setActive(false);
		this.setAnimationRow(row);
		this.setAnimColStart(start);
		this.setAnimColStop(stop);
		this.setSpeed(speed);
	}
	
	public void setName(String name) {
		if(name != null){ this.name = name; }		
	}
	public String getName() { return name; }
	
	/** Sets the ability to active or not. 
	 * @param state The state you wish it to be. */
	public void setActive(boolean state) { this.isActive = state; }	
	/** Returns whether or not the ability is currently in use. */	 
	public boolean isActive() { return isActive; }

	/** Sets the row of the sprite sheet to use for the animation.
	 *  @param row The row from 0 to # of rows - 1.*/ 
	public void setAnimationRow(int row) { if( row >= 0 ){ this.animationRow = row; } }
	/** Returns the row of the animation for this ability on the sprite sheet.
	 * @return animationRow The row number from 0 to # of rows - 1.*/	 
	public int getAnimationRow() { return animationRow; }

	/** Sets the speed of this ability's animation. Lower is faster.
	 * @param speed Integer of milliseconds between each frame.*/
	public void setSpeed(int speed) { if( speed > 0 ){ this.speed = speed; } }		

	/** Returns the speed of this ability's animation. Lower is faster. */
	public int getSpeed() { return speed; }
	/**
	 * @param animColStart the animColStart to set
	 */
	public void setAnimColStart(int animColStart) {
		this.animColStart = animColStart;
	}
	/**
	 * @return the animColStart
	 */
	public int getAnimColStart() {
		return animColStart;
	}
	/**
	 * @param animColStop the animColStop to set
	 */
	public void setAnimColStop(int animColStop) {
		this.animColStop = animColStop;
	}
	/**
	 * @return the animColStop
	 */
	public int getAnimColStop() {
		return animColStop;
	}
}
