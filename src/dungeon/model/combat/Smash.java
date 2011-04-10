package dungeon.model.combat;

import org.w3c.dom.Node;

import dungeon.utils.Persistent;

/**
 * Melee creature attack
 */
public class Smash extends Attack implements Persistent
{
	public String getName()
	{
		return "Smash";
	}
	
	public int getEnergyCost()
	{
		return 0;
	}
	
	public int getMinRange()
	{
		return 0;
	}
	
	public int getMaxRange()
	{
		return 0;
	}
	
	public int getToHit()
	{
		return 5;
	}
	
	public int getDamage()
	{
		return 5;
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
