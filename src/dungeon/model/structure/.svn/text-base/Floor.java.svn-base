package dungeon.model.structure;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.w3c.dom.Node;

import dungeon.utils.Persistent;

public class Floor extends Tile implements Persistent
{
	/**
	 * Constructor
	 */
	public Floor()
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
	public Floor(double x, double y, double width, double height)
	{
		super(x, y, width, height);
	}
	
	/**
	 * Constructor
	 * 
	 * @param area The map area the tile will occupy
	 */
	public Floor(Rectangle2D area)
	{
		super(area);
	}
	
	public void draw(Graphics2D g, Rectangle2D rect)
	{
		g.setColor(Color.WHITE);
		g.fill(rect);
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
