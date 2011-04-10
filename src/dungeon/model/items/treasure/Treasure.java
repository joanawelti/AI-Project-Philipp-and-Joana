package dungeon.model.items.treasure;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.w3c.dom.Node;

import dungeon.model.Game;
import dungeon.model.items.Item;
import dungeon.model.items.mobs.Mob;
import dungeon.utils.Persistent;

/**
 * Class representing an item of treasure in the dungeon
 */
public abstract class Treasure extends Item implements Persistent
{
	/**
	 * Mobs are limited in the amount of treasure they can carry
	 * @return Returns the weight of the treasure item
	 */
	public abstract double getWeight();
	
	/**
	 * @return Returns the value of the item (in units of gold)
	 */
	public abstract int getWorth();

	public void draw(Graphics2D g, Rectangle2D rect, boolean highlight)
	{
		g.setColor(Color.BLUE);
		g.fill(rect);
		
		g.setColor(Color.BLUE.darker());
		g.draw(rect);
	}
	
	public void onTick(Game game)
	{
	}

	public void gameOverTick(Game game)
	{
	}

	public void deathTick(Game game)
	{
	}
	/**
	 * Some treasure items can be consumed (such as potions)
	 * 
	 * @return Returns true if the item is consumable; false otherwise
	 */
	public boolean isConsumable()
	{
		return false;
	}
	
	/**
	 * This is called when a mob attempts to consume the treasure item
	 * <BR>
	 * The effect of consumption depends on the type of item
	 * 
	 * @param mob The consuming mob
	 * @return Returns true if the item was consumed; false otherwise
	 */
	public boolean onConsume(Mob mob)
	{
		return isConsumable();
	}

	public void load(Node node)
	{
		super.load(node);
	}

	public void save(Node node)
	{
		super.save(node);
	}
}
