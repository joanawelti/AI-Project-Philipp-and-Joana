package dungeon.model.items.mobs;

/**
 * Holds keyboard movement requests
 */
public class Movement
{
	public boolean Up = false;
	public boolean Down = false;
	public boolean Left = false;
	public boolean Right = false;
		
	/**
	 * Calculates the angle specifying the direction the user wants the hero to travel in
	 * 
	 * @return Returns the direction of motion as an angle (in radians)
	 */
	public double getAngle()
	{
		int dx = 0;
		int dy = 0;
		
		if (Up)
			dy -= 1;
		if (Down)
			dy += 1;
		if (Left)
			dx -= 1;
		if (Right)
			dx += 1;
		
		if ((dx == 0) && (dy == 0))
			return Double.NaN;
		
		return Math.atan2(dx, dy);
	}
}
