package dungeon.model.structure;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import java.util.Vector;

import org.w3c.dom.Node;

import dungeon.App;
import dungeon.model.Game;
import dungeon.model.items.mobs.Mob;
import dungeon.utils.Persistent;

public class FlameTrap extends Tile implements Persistent
{
	/**
	 * Constructor
	 */
	public FlameTrap()
	{
		set_up();
	}
	
	/**
	 * Constructor
	 * 
	 * @param x The tile's x co-ordinate
	 * @param y The tile's y co-ordinate
	 * @param width The tile's width
	 * @param height The tile's height
	 */
	public FlameTrap(double x, double y, double width, double height)
	{
		super(x, y, width, height);
		set_up();
	}
	
	/**
	 * Constructor
	 * 
	 * @param area The map area the tile will occupy
	 */
	public FlameTrap(Rectangle2D area)
	{
		super(area);
		set_up();
	}
	
	/**
	 * Sets up the colours we can use when we're activated
	 */
	void set_up()
	{
		fColours = new Vector<Color>();
		
		fColours.add(Color.YELLOW);
		fColours.add(Color.ORANGE);
		fColours.add(Color.RED);
		
		fColours.add(Color.YELLOW.brighter());
		fColours.add(Color.ORANGE.brighter());
		fColours.add(Color.RED.brighter());
		
		fColours.add(Color.YELLOW.darker());
		fColours.add(Color.ORANGE.darker());
		fColours.add(Color.RED.darker());
	}
	
	Vector<Color> fColours = null;
	int fColourIndex = 0;
	
	Vector<Mob> fVictims = new Vector<Mob>();
	
	public void draw(Graphics2D g, Rectangle2D rect)
	{
		Color c = (fVictims.size() != 0) ? fColours.get(fColourIndex) : Color.WHITE;
		
		g.setColor(c);
		g.fill(rect);
	}
	
	public void onTick(Game game)
	{
		Vector<Mob> previous_victims = new Vector<Mob>();
		previous_victims.addAll(fVictims);
		
		fVictims.clear();
		
		Vector<Mob> mobs = new Vector<Mob>();
		mobs.addAll(game.getCreatures());
		mobs.add(game.getHero());
		
		for (Mob mob: mobs)
		{
			if (fArea.contains(mob.getLocation()))
			{
				if (!previous_victims.contains(mob))
					App.log(mob + " sets off a flame trap");
				
				fVictims.add(mob);
			}
		}
		
		if (fVictims.size() != 0)
		{
			Random rnd = new Random();
			fColourIndex = rnd.nextInt(fColours.size());
		}
	}
	
	public void affect(Mob mob)
	{
		int health = mob.getCurrentHealth() - 1;
		mob.setCurrentHealth(health);
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
