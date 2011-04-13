package dungeon.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

import dungeon.App;
import dungeon.model.Game;
import dungeon.model.items.Item;
import dungeon.model.items.mobs.Mob;
import dungeon.model.items.treasure.Treasure;
import dungeon.model.structure.Tile;
import dungeon.utils.Pair;

/**
 * Panel to display a dungeon map
 * <BR>
 * The panel is interactive; you can click on it to send the hero to that location
 */
public class MapPanel extends JPanel
{
	// Required
	private static final long serialVersionUID = 6804060850550179463L;
	private static List<Point2D> path = null;
	
	public static void setPath(List<Point2D> newPath){
		path = newPath;
	}
	
	/**
	 * Constructor
	 * @param game The current game state
	 */
	public MapPanel(Game game)
	{
		addMouseListener(fMouseListener);
		
		fGame = game;
	}

	/**
	 * @return Returns the current game state
	 */
	public Game getGame()
	{
		return fGame;
	}
	/**
	 * Sets the current game state
	 * @param game The game state
	 */
	public void setGame(Game game)
	{
		fGame = game;
		
		fMapBounds = null;
		fDisplayBounds = null;
		
		repaint();
	}
	Game fGame = null;
	
	/**
	 * Make a link appear briefly between two items on the map
	 * 
	 * @param item_a The first item
	 * @param item_b The second item
	 */
	public void showLink(Item item_a, Item item_b)
	{
		if (App.getSpeed() != App.NOGRAPHICS) {
			fLink = new Pair(item_a, item_b);

			repaint();
		}
	}
	
	Rectangle2D fMapBounds = null;
	Rectangle2D fDisplayBounds = null;
	
	// This 
	Pair fLink = null;
	
	// This determines whether mob goal information is drawn on the map
	boolean DISPLAY_GOALS = true;
	
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Draw black background
		Rectangle control_bounds = getBounds();
		g2.setColor(Color.BLACK);
		g2.fill(new Rectangle2D.Double(0, 0, control_bounds.width, control_bounds.height));
		
		fMapBounds = fGame.getMap().getBounds(1);
		fDisplayBounds = get_display_rectangle(fMapBounds, control_bounds);
		
		// Draw map tiles
		for (Tile t: fGame.getMap().getTiles())
		{
			Rectangle2D tile_rect = get_region(t.getArea());
			t.draw(g2, tile_rect);
		}
		
		// Draw loot
		for (Treasure treasure: fGame.getTreasure())
		{
			Rectangle2D treasure_rect = get_region(treasure.getShape());
			treasure.draw(g2, treasure_rect, false);
		}
		
		Vector<Mob> mobs = new Vector<Mob>();
		mobs.addAll(fGame.getCreatures());
		
		if (fGame.getHero() != null)
			mobs.add(fGame.getHero());
		
		// Draw mobs
		for (Mob mob: mobs)
		{
			boolean in_link = false;
			if (fLink != null)
				in_link = ((mob == fLink.getFirst()) || (mob == fLink.getSecond()));
			
			Rectangle2D mob_rect = get_region(mob.getShape());
			mob.draw(g2, mob_rect, in_link);
			
			if ((DISPLAY_GOALS) && (mob.getCurrentHealth() > 0) && (mob.getGoal() != null))
			{
				Point2D mob_pt = get_scaled_value(mob.getLocation(), fMapBounds, fDisplayBounds);
				Point2D goal_pt = get_scaled_value(mob.getGoal(), fMapBounds, fDisplayBounds);
				
				double radius = mob_rect.getWidth() / 2;
				
				Color c = new Color(0, 0, 255, 20);
				draw_line_between(g2, mob_pt, radius, goal_pt, radius, c);
				
				Ellipse2D ell = new Ellipse2D.Double(goal_pt.getX() - radius, goal_pt.getY() - radius, radius * 2, radius * 2);
				
				g2.setColor(new Color(0, 0, 255, 20));
				g2.fill(ell);
				
				g2.setColor(new Color(0, 0, 255, 20));
				g2.draw(ell);
				if(path != null){
					for(Point2D point : path){
						Point2D wayPoint = get_scaled_value(point, fMapBounds, fDisplayBounds);
						Ellipse2D marker = new Ellipse2D.Double(wayPoint.getX() - radius, wayPoint.getY() - radius, radius * 2, radius * 2);
						g2.setColor(new Color(0, 0, 255, 20));
						g2.fill(marker);
						g2.setColor(new Color(0, 0, 255, 20));
						g2.draw(marker);
					}
					path = null;
				}
			}
			
			
		}
		
		if (fLink != null)
		{
			Item item_a = (Item)fLink.getFirst();
			Item item_b = (Item)fLink.getSecond();
			
			Rectangle2D rect_a = get_region(item_a.getShape());
			Rectangle2D rect_b = get_region(item_b.getShape());
			
			Point2D pt_a = new Point2D.Double(rect_a.getCenterX(), rect_a.getCenterY());
			Point2D pt_b = new Point2D.Double(rect_b.getCenterX(), rect_b.getCenterY());
			
			double radius_a = rect_a.getWidth() / 2;
			double radius_b = rect_b.getWidth() / 2;
			
			draw_line_between(g2, pt_a, radius_a, pt_b, radius_b, Color.BLACK);
		}
		
		fLink = null;
	}

	/**
	 * Calculates the drawable region for the map
	 * 
	 * @param map_bounds The bounds of the map
	 * @param control_bounds The bounds of the control
	 * @return Returns the screen rectangle in which the map will be drawn
	 */
	Rectangle2D get_display_rectangle(Rectangle2D map_bounds, Rectangle control_bounds)
	{
		// Determine the minimum dimension of a 1x1 tile
		double x = (double)control_bounds.width / map_bounds.getWidth();
		double y = (double)control_bounds.height / map_bounds.getHeight();
		double min = Math.min(x, y);
		
		// Determine the dimensions of the map
		double used_width = map_bounds.getWidth() * min;
		double used_height = map_bounds.getHeight() * min;
		
		// Determine the dimensions of the unused control space
		double unused_width = control_bounds.width - used_width;
		double unused_height = control_bounds.height - used_height;
		
		return new Rectangle2D.Double(unused_width / 2, unused_height / 2, used_width, used_height);
	}
	
	/**
	 * Translates a rectangle from map co-ordinates to display co-ordinates
	 * 
	 * @param rect The rectangle in map co-ordinates
	 * @return Returns the translated rectangle 
	 */
	Rectangle2D get_region(Rectangle2D rect)
	{
		Point2D top_left = new Point2D.Double(rect.getX(), rect.getY());
		Point2D bottom_right = new Point2D.Double(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());
		
		top_left = get_scaled_value(top_left, fMapBounds, fDisplayBounds);
		bottom_right = get_scaled_value(bottom_right, fMapBounds, fDisplayBounds);

		double width = bottom_right.getX() - top_left.getX();
		double height = bottom_right.getY() - top_left.getY();
		
		return new Rectangle2D.Double(top_left.getX(), top_left.getY(), width, height);
	}
	
	/**
	 * Translates a point from one rectangle into it relative location in another
	 * 
	 * @param pt The point to be translated
	 * @param source_rect The rectangle we are translating from
	 * @param output_rect The rectangle we are translating into
	 * @return Returns the location of the point in output_rect, relative to its location in source-rect
	 */
	Point2D get_scaled_value(Point2D pt, Rectangle2D source_rect, Rectangle2D output_rect)
	{
		double x = pt.getX() - source_rect.getX();
		double y = pt.getY() - source_rect.getY();
		
		double prop_x = (double)x / source_rect.getWidth();
		double prop_y = (double)y / source_rect.getHeight();
		
		double new_x = output_rect.getX() + (prop_x * output_rect.getWidth());
		double new_y = output_rect.getY() + (prop_y * output_rect.getHeight());
		
		return new Point2D.Double(new_x, new_y);
	}
	
	/**
	 * Draws a line of the given colour between two Item objects
	 * 
	 * @param g The graphics object
	 * @param pt_a The location of the first item
	 * @param radius_a The radius of the first item
	 * @param pt_b The location of the second item
	 * @param radius_b The radius of the second item
	 * @param c The colour to draw the line with
	 */
	void draw_line_between(Graphics2D g, Point2D pt_a, double radius_a, Point2D pt_b, double radius_b, Color c)
	{
		double dx = pt_b.getX() - pt_a.getX();
		double dy = pt_b.getY() - pt_a.getY();
		
		double theta = Math.atan2(dx, dy);
		
		double x_a = radius_a * Math.sin(theta);
		double y_a = radius_a * Math.cos(theta);
		Point2D from_pt = new Point2D.Double(pt_a.getX() + x_a, pt_a.getY() + y_a);
		
		double x_b = radius_b * Math.sin(theta);
		double y_b = radius_b * Math.cos(theta);
		Point2D to_pt = new Point2D.Double(pt_b.getX() - x_b, pt_b.getY() - y_b);
		
		// Draw line
		g.setColor(c);
		Line2D line = new Line2D.Double(from_pt, to_pt);
		g.draw(line);
	}
	
	/**
	 * This handles mouse clicks on the control
	 */
	MouseListener fMouseListener = new MouseAdapter()
	{
		public void mouseClicked(MouseEvent e)
		{
			if (fGame.getHero() == null)
				return;
			
			Point2D pt = get_scaled_value(e.getPoint(), fDisplayBounds, fMapBounds);
			fGame.getHero().setGoal(pt, fGame);
		}
	};
	
	
}
