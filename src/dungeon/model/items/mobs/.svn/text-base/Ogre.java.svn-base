package dungeon.model.items.mobs;

import org.w3c.dom.Node;

import dungeon.model.combat.Smash;
import dungeon.utils.Persistent;

/**
 * An ogre; stronger and tougher than an orc, but slower
 */
public class Ogre extends Creature implements Persistent
{
	/**
	 * Constructor
	 */
	public Ogre()
	{
		set_up();
	}
	
	/**
	 * Sets the creature's attacks and amount of carried gold
	 */
	void set_up()
	{
		fAttacks.add(new Smash());
		fGold = 50;
	}
	
	public String getName()
	{
		return "Ogre";
	}

	public double getSize()
	{
		return 2;
	}
	
	public double getSpeed()
	{
		return 0.5;
	}
	
	public int getDefence()
	{
		return 5;
	}
	
	public int getStrength()
	{
		return 50;
	}
	
	public int getMaxHealth()
	{
		return 100;
	}
	
	public int getMaxEnergy()
	{
		return 100;
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
