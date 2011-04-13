package dungeon.model.structure;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.w3c.dom.Node;

import dungeon.App;
import dungeon.model.items.mobs.Mob;
import dungeon.utils.Persistent;

public class Pit extends Tile implements Persistent
{
	/**
	 * Constructor
	 */
	public Pit()
	{
	}
	
	/**
	 * Constructor
	 * 
	 * @param x The tile's x co-ordinate
	 * @param y The tile's y co-ordinate
	 * @param width The tile's width
	 * @param height The tile's height
	 */
	public Pit(double x, double y, double width, double height)
	{
		super(x, y, width, height);
	}
	
	/**
	 * Constructor
	 * 
	 * @param area The map area the tile will occupy
	 */
	public Pit(Rectangle2D area)
	{
		super(area);
	}
	
	public void draw(Graphics2D g, Rectangle2D rect)
	{
		g.setColor(Color.DARK_GRAY);
		g.fill(rect);
		
		double delta = Math.min(rect.getWidth(), rect.getHeight()) / 4;
		Rectangle2D inner = new Rectangle2D.Double(rect.getX() + delta, rect.getY() + delta, rect.getWidth() - (2 * delta), rect.getHeight() - (2 * delta));
		
		g.setColor(Color.BLACK);
		g.fill(inner);
		
		Line2D nw = new Line2D.Double(rect.getX(), rect.getY(), inner.getX(), inner.getY());
		Line2D ne = new Line2D.Double(rect.getX() + rect.getWidth(), rect.getY(), inner.getX() + inner.getWidth(), inner.getY());
		Line2D sw = new Line2D.Double(rect.getX(), rect.getY() + rect.getHeight(), inner.getX(), inner.getY() + inner.getHeight());
		Line2D se = new Line2D.Double(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight(), inner.getX() + inner.getWidth(), inner.getY() + inner.getHeight());
		
		g.draw(nw);
		g.draw(ne);
		g.draw(sw);
		g.draw(se);
	}
	
	public void affect(Mob mob)
	{
		if (mob.getCurrentHealth() > 0)
		{
			mob.setCurrentHealth(0);
			App.log(mob + " falls into a pit");
		}
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
