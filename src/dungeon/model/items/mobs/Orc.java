package dungeon.model.items.mobs;

import org.w3c.dom.Node;

import dungeon.model.combat.Dagger;
import dungeon.model.combat.Smash;
import dungeon.utils.Persistent;

/**
 * An orc; relatively easy to kill
 */
public class Orc extends Creature implements Persistent
{
	/**
	 * Constructor
	 */
	public Orc()
	{
		set_up();
	}
	
	/**
	 * Sets the creature's attacks and amount of carried gold
	 */
	void set_up()
	{
		fAttacks.add(new Smash());
		fAttacks.add(new Dagger());
		
		fGold = 20;
	}
	
	
	public String getName()
	{
		return "Orc";
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
