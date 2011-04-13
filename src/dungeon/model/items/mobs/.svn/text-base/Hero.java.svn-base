package dungeon.model.items.mobs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.w3c.dom.Node;

import dungeon.ai.Behaviour;
import dungeon.ai.HeroBehaviour;
import dungeon.model.Game;
import dungeon.model.combat.Longbow;
import dungeon.model.combat.Sword;
import dungeon.utils.Persistent;

/**
 * The user-controlled hero mob
 */
public class Hero extends Mob implements Persistent
{
	/**
	 * Constructor
	 */
	public Hero()
	{
		set_up();
	}
	
	/**
	 * Sets the attacks available to the hero
	 */
	void set_up()
	{
		fAttacks.add(new Sword());
		fAttacks.add(new Longbow());
	}
	
	public String getName()
	{
		return "Hero";
	}

	public double getSize()
	{
		return 1;
	}
	
	public String getFaction()
	{
		return "Hero";
	}
	
	public double getSpeed()
	{
		return 1;
	}

	public int getDefence()
	{
		return 5;
	}
	
	public int getStrength()
	{
		return 100;
	}
	
	public int getMaxHealth()
	{
		return 100;
	}
	
	public int getMaxEnergy()
	{
		return 100;
	}
	
	public Behaviour getBehaviour()
	{
		return fBehaviour;
	}
	Behaviour fBehaviour = new HeroBehaviour(this);
	
	/**
	 * @return Returns the keyboard movement request
	 */
	public Movement getMovement()
	{
		return fMovement;
	}
	/**
	 * Sets the keyboard movement request
	 * @param m The object containing the user's arrow key presses
	 */
	public void setMovement(Movement m)
	{
		fMovement = m;
	}
	Movement fMovement = new Movement();
	
	public void draw(Graphics2D g, Rectangle2D rect, boolean highlight)
	{
		defaultDraw(g, rect, Color.GREEN, highlight);
	}

	public void onTick(Game game)
	{
		boolean acted = fBehaviour.onTick(game);
		if (!acted)
		{
			int energy = Math.min(getCurrentEnergy() + 1, getMaxEnergy());
			setCurrentEnergy(energy);
		}
	}
	
	

	public void gameOverTick(Game game)
	{
	}

	public void deathTick(Game game)
	{
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
