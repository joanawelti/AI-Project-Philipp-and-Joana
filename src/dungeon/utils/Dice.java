package dungeon.utils;

import java.util.Random;

/**
 * Utility class to represent dice rolling
 */
public class Dice
{
	// The random number generator
	private static Random fRandom = new Random();
	
	/**
	 * Rolls a number of dice and returns the result
	 * <br>
	 * For example, to roll 1d6, set <b>count</b> to 1 and <b>sides</b> to 6
	 * 
	 * @param count The number of dice to roll
	 * @param sides The number of sides on each die
	 * 
	 * @return Returns the sum of the rolls
	 */
	public static int Roll(int count, int sides)
	{
		int total = 0;
		
		for (int n = 0; n != count; ++n)
			total += Roll(sides);
		
		return total;
	}
	
	/**
	 * Rolls a single die with the given number of sides
	 * <br>
	 * For example, for a standard cube die, set <b>sides</b> to 6
	 * 
	 * @param sides The number of sides on the die
	 * @return Returns the result of the roll
	 */
	public static int Roll(int sides)
	{
		return 1 + fRandom.nextInt(sides);
	}
}
