package dungeon.model.items.treasure;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import org.w3c.dom.Node;

import dungeon.App;
import dungeon.model.items.mobs.Mob;
import dungeon.utils.Persistent;
import dungeon.utils.XMLHelper;

/**
 * A potion that affects the drinker in some way
 * <BR>
 * Could be a health potion or an energy potion
 */
public class Potion extends Treasure implements Persistent
{
	public static final int POTION_HEALTH = 0;
	public static final int POTION_ENERGY = 1;
	
	/**
	 * Constructor
	 */
	public Potion()
	{
	}
	
	/**
	 * Constructor
	 * 
	 * @param type POTION_ENERGY or POTION_HEALTH
	 */
	public Potion(int type)
	{
		fType = type;
	}
	
	public String getName()
	{
		switch (fType)
		{
		case POTION_HEALTH:
			return "Health potion";
		case POTION_ENERGY:
			return "Energy potion";
		}
		
		return "";
	}

	public double getSize()
	{
		return 1;
	}

	public double getWeight()
	{
		return 2;
	}

	public int getWorth()
	{
		return 10;
	}
	
	public boolean isConsumable()
	{
		return true;
	}

	public boolean onConsume(Mob mob)
	{
		int value = 25;
		
		switch (fType)
		{
		case POTION_HEALTH:
			{
				int current = mob.getCurrentHealth();
				if (current == mob.getMaxHealth())
				{
					App.log(mob + " is already at full health");
					return false;
				}
				else
				{
					int health = Math.min(value + current, mob.getMaxHealth());
					mob.setCurrentHealth(health);
					
					App.log(mob + " drinks a health potion");
				}
			}
			break;
		case POTION_ENERGY:
			{
				int current = mob.getCurrentEnergy();
				if (current == mob.getMaxEnergy())
				{
					App.log(mob + " is already at full energy");
					return false;
				}
				else
				{
					int energy = Math.min(value + current, mob.getMaxEnergy());
					mob.setCurrentEnergy(energy);
					
					App.log(mob + " drinks an energy potion");
				}
			}
			break;
		}
		
		return true;
	}

	public int getType()
	{
		return fType;
	}
	public void setDoorID(int type)
	{
		fType = type;
	}
	private int fType = POTION_HEALTH;

	public void draw(Graphics2D g, Rectangle2D rect, boolean highlight)
	{		
		Color c = null;
		switch (fType)
		{
		case POTION_HEALTH:
			c = Color.GREEN.darker();
			break;
		case POTION_ENERGY:
			c = Color.BLUE;
			break;
		}
		
		double stem_width = rect.getWidth() / 4;
		double stem_x = rect.getX() + ((rect.getWidth() - stem_width) / 2);
		double stem_height = rect.getHeight() / 2;
		Rectangle2D stem = new Rectangle2D.Double(stem_x, rect.getY(), stem_width, stem_height);
		
		double filled_stem_height = stem_height * 0.6;
		double filled_stem_y = stem.getY() + stem_height - filled_stem_height;
		Rectangle2D filled_stem = new Rectangle2D.Double(stem.getX(), filled_stem_y, stem_width, filled_stem_height);
		
		double bowl_size = rect.getHeight() * 0.6;
		double bowl_x = rect.getX() + ((rect.getWidth() - bowl_size) / 2);
		double bowl_y = rect.getY() + (rect.getHeight() - bowl_size);
		Ellipse2D bowl = new Ellipse2D.Double(bowl_x, bowl_y, bowl_size, bowl_size);
		
		double cork_width = stem_width * 0.8;
		double cork_x = stem_x + ((stem_width - cork_width) / 2);
		double cork_height = stem_height * 0.2;
		Rectangle2D cork = new Rectangle2D.Double(cork_x, rect.getY(), cork_width, cork_height);
		
		g.setColor(Color.ORANGE.darker());
		g.fill(cork);
		
		g.setColor(c);
		g.draw(stem);
		
		g.setColor(c);
		g.fill(bowl);
		
		g.setColor(c);
		g.fill(filled_stem);
	}

	public void load(Node node)
	{
		super.load(node);
		fType = XMLHelper.getIntValue(node, "Type");
	}

	public void save(Node node)
	{
		super.save(node);
		XMLHelper.setIntValue(node, "Type", fType);
	}
}
