package dungeon.model.combat;

import org.w3c.dom.Node;

import dungeon.utils.Persistent;

/**
 * Ranged hero attack
 */
public class Longbow extends Attack implements Persistent
{
	public String getName()
	{
		return "Longbow";
	}
	
	public int getEnergyCost()
	{
		return 50;
	}
	
	public int getMinRange()
	{
		return 3;
	}
	
	public int getMaxRange()
	{
		return 10;
	}
	
	public int getToHit()
	{
		return 3;
	}
	
	public int getDamage()
	{
		return 20;
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
