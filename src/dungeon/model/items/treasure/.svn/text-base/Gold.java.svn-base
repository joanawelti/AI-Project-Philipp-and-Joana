package dungeon.model.items.treasure;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import org.w3c.dom.Node;

import dungeon.utils.Persistent;
import dungeon.utils.XMLHelper;

/**
 * A pile of coins
 */
public class Gold extends Treasure implements Persistent
{
	/**
	 * Constructor
	 */
	public Gold()
	{
	}
	
	/**
	 * Constructor
	 * @param worth The value of the coins
	 */
	public Gold(int worth)
	{
		fWorth = worth;
	}

	public String getName()
	{
		return "Gold";
	}

	public double getSize()
	{
		return Math.sqrt(fWorth) / 10;
	}

	public double getWeight()
	{
		return getWeight(fWorth);
	}

	/**
	 * Calculates the weight of a given number of gold coins
	 * 
	 * @param worth The number of coins
	 * @return Returns the weight of the coins
	 */
	public static double getWeight(int worth)
	{
		return (double)worth / 10;
	}
	
	public int getWorth()
	{
		return fWorth;
	}
	public void setWorth(int worth)
	{
		fWorth = worth;
	}
	int fWorth = 100;
	
	public void draw(Graphics2D g, Rectangle2D rect, boolean highlight)
	{
		Random rnd = new Random(hashCode());
		double radius = rect.getWidth() / getSize() / 10;

		int coins = fWorth / 4;
		for (int index = 0; index != coins; ++index)
		{
			double x = (rnd.nextDouble() * rect.getWidth()) + rect.getX() - radius;
			double y = (rnd.nextDouble() * rect.getHeight()) + rect.getY() - radius;
			
			Ellipse2D coin_rect = new Ellipse2D.Double(x, y, radius * 2, radius * 2);
			
			Color c = null;
			int n = rnd.nextInt(10);
			switch (n)
			{
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
				c = Color.YELLOW;
				break;
			case 7:
			case 8:
				c = Color.LIGHT_GRAY;
				break;
			case 9:
				c = Color.ORANGE.darker();
				break;
			}
			
			g.setColor(c);
			g.fill(coin_rect);
			
			g.setColor(c.darker());
			g.draw(coin_rect);
		}
	}

	public void load(Node node)
	{
		super.load(node);
		fWorth = XMLHelper.getIntValue(node, "Worth");
	}

	public void save(Node node)
	{
		super.save(node);
		XMLHelper.setIntValue(node, "Worth", fWorth);
	}
	
	public String toString()
	{
		return getName() + " (" + fWorth + ")";
	}
}
