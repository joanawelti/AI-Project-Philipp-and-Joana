package dungeon.model.structure;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.w3c.dom.Node;

import dungeon.App;
import dungeon.model.items.mobs.FastOgre;
import dungeon.model.items.mobs.Hero;
import dungeon.model.items.mobs.Mob;
import dungeon.utils.Persistent;

/**
 * The finish tile
 * <BR>
 * If the hero enters this tile, the user wins the game
 */
public class Finish extends Tile implements Persistent
{
	/**
	 * Constructor
	 */
	public Finish()
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
	public Finish(double x, double y, double width, double height)
	{
		super(x, y, width, height);
	}
	
	/**
	 * Constructor
	 * 
	 * @param area The map area the tile will occupy
	 */
	public Finish(Rectangle2D area)
	{
		super(area);
	}
	
	public int getLevelChange()
	{
		return fLevelChange;
	}
	public void setLevelChange(int level)
	{
		fLevelChange = level;
	}
	int fLevelChange = 0;
	
	public void draw(Graphics2D g, Rectangle2D rect)
	{
		Color c = new Color(0, 100, 0);
		
		g.setColor(c);
		g.fill(rect);
		
		g.setColor(c.brighter());
		g.draw(rect);
		
		if (fLevelChange != 0)
		{
			int level = App.getGame().getLevel() + fLevelChange;
			String str = "(lvl " + level + ")";
			
			Rectangle2D bounds = g.getFontMetrics().getStringBounds(str, g);
			
			double dx = (rect.getWidth() - bounds.getWidth()) / 2;
			double dy = (rect.getHeight() - bounds.getHeight()) / 2;
			
			double x = rect.getX() + dx;
			double y = rect.getY() + rect.getHeight() - dy;
			
			g.setColor(Color.WHITE);
			g.drawString(str, (float)x, (float)y);
		}
	}
	
	public void affect(Mob mob)
	{
		if (mob instanceof Hero || mob instanceof FastOgre)
		{
			if (fLevelChange == 0)
			{
				// Won the game
				App.finishGame(true);
			}
			else
			{
				// Move to new level
				App.changeMapLevel(fLevelChange);
			}
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
