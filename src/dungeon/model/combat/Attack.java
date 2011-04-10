package dungeon.model.combat;

import org.w3c.dom.Node;

import dungeon.App;
import dungeon.model.items.mobs.Mob;
import dungeon.utils.Dice;
import dungeon.utils.Persistent;

public abstract class Attack implements Persistent
{
	/**
	 * @return Returns the attack name
	 */
	public abstract String getName();
	
	/**
	 * @return Returns the energy cost of the attack, if any
	 */
	public abstract int getEnergyCost();
	
	/**
	 * @return Returns the minimum range of the attack (0 for a melee attack)
	 */
	public abstract int getMinRange();
	
	/**
	 * @return Returns the maximum range of the attack (0 for a melee attack)
	 */
	public abstract int getMaxRange();
	
	/**
	 * A higher to-hit bonus indicates a more accurate attack
	 * @return Returns the to-hit bonus of the attack
	 */
	public abstract int getToHit();
	
	/**
	 * @return Returns the attack's damage
	 */
	public abstract int getDamage();
	
	/**
	 * Performs the attack against the specified mob
	 * 
	 * @param attacker The attacking mob
	 * @param target The target mob
	 * @return Returns true if the attack hit; false otherwise
	 */
	public boolean useAttack(Mob attacker, Mob target)
	{
		App.log(attacker + " attacks " + target + " (" + this + ")");
		
		int energy = attacker.getCurrentEnergy() - getEnergyCost();
		attacker.setCurrentEnergy(energy);
		
		// Roll to hit
		int to_hit = getToHit() + Dice.Roll(6);
		if (to_hit >= target.getDefence())
		{
			App.showLink(attacker, target);
			
			// Hit; deal damage
			int damage = getDamage() + Dice.Roll(6);
			int health = target.getCurrentHealth() - damage;
			target.setCurrentHealth(health);
			
			App.log("* hit (" + damage + " damage)");
			
			return true;
		}
		else
		{
			// Miss
			App.log("* miss");
			return false;
		}
	}

	public void load(Node node)
	{
	}

	public void save(Node node)
	{
	}
	
	public String toString()
	{
		return getName();
	}
}
