package dungeon.model.items.mobs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import java.util.Vector;

import org.w3c.dom.Node;

import dungeon.App;
import dungeon.ai.Behaviour;
import dungeon.ai.CollisionDetection;
import dungeon.ai.LineOfSight;
import dungeon.collections.AttackList;
import dungeon.collections.TreasureList;
import dungeon.model.Game;
import dungeon.model.combat.Attack;
import dungeon.model.items.Item;
import dungeon.model.items.treasure.Gold;
import dungeon.model.items.treasure.Key;
import dungeon.model.items.treasure.Potion;
import dungeon.model.items.treasure.Treasure;
import dungeon.model.structure.Door;
import dungeon.model.structure.Tile;
import dungeon.utils.Persistent;
import dungeon.utils.XMLHelper;

/**
 * Abstract class to represent any mobile item in the dungeon
 * <BR>
 * Derived classes are <B>Hero</B> and <B>Creature</B>
 */
public abstract class Mob extends Item implements Persistent
{
	/**
	 * Computer-controlled mobs will not attack other mobs if they are in the same faction
	 * @return Returns the mob's faction name
	 */
	public abstract String getFaction();
	
	/**
	 * @return Returns the mob's speed
	 */
	public abstract double getSpeed();
	
	/**
	 * Mobs with higher defence values (perhaps due to armour) are more difficult to hit in combat
	 * @return Returns the mob's defence value
	 */
	public abstract int getDefence();
	
	/**
	 * Mobs with higher strength can carry more treasure
	 * @return Returns the mob's strength
	 */
	public abstract int getStrength();
	
	/**
	 * @return Returns the maximum health for the mob
	 */
	public abstract int getMaxHealth();
	
	/**
	 * @return Returns the maximum energy for the mob
	 */
	public abstract int getMaxEnergy();
	
	/**
	 * @return Returns the object responsible for determining the mob's actions
	 */
	public abstract Behaviour getBehaviour();
	
	/**
	 * @return Returns the distance the mob can reach
	 */
	public double getReach()
	{
		return 1.5;// * getSpeed();
	}
	
	/**
	 * @return Returns the mob's current health
	 */
	public int getCurrentHealth()
	{
		return fCurrentHealth;
	}
	/**
	 * Sets the mob's current health
	 * @param current_health The new health level
	 */
	public void setCurrentHealth(int current_health)
	{
		fCurrentHealth = current_health;
	}
	protected int fCurrentHealth = getMaxHealth();
	
	/**
	 * @return Returns the mob's current energy
	 */
	public int getCurrentEnergy()
	{
		return fCurrentEnergy;
	}
	/**
	 * Sets the mob's current energy
	 * @param current_energy The new energy level
	 */
	public void setCurrentEnergy(int current_energy)
	{
		fCurrentEnergy = current_energy;
	}
	protected int fCurrentEnergy = getMaxEnergy();
	
	/**
	 * @return Returns the list of attacks which the mob can perform
	 */
	public AttackList getAttacks()
	{
		return fAttacks;
	}
	protected AttackList fAttacks = new AttackList();
	
	/**
	 * @return Returns the list of treasure items carried by the mob
	 */
	public TreasureList getInventory()
	{
		return fInventory;
	}
	protected TreasureList fInventory = new TreasureList();

	/**
	 * @return Returns the mob's goal point
	 */
	public Point2D getGoal()
	{
		return fGoal;
	}
	/**
	 * Sets the mob's goal point
	 * 
	 * @param pt The point (or null to clear the current goal)
	 * @param game The game state
	 */
	public void setGoal(Point2D pt, Game game)
	{
		if (pt != null)
		{
	//		if (CollisionDetection.canOccupy(game, this, pt))
				fGoal = pt;
		}
		else
		{
			fGoal = null;
		}
	}
	Point2D fGoal = null;
	
	/**
	 * @return Returns the amount of gold carried by the mob
	 */
	public int getGold()
	{
		return fGold;
	}
	/**
	 * Sets the amount of gold carried by the mob
	 * @param value The amount of gold to be carried
	 */
	public void setGold(int value)
	{
		fGold = value;
	}
	protected int fGold = 0;
	
	/**
	 * Calculates the total weight carried by the mob
	 * 
	 * @return Returns the weight carried
	 */
	public double getEncumbrance()
	{
		double value = Gold.getWeight(fGold);
		
		for (Treasure treasure: fInventory)
			value += treasure.getWeight();
		
		return value;
	}
	
	/**
	 * Calculates the value of the mob's inventory (in gold)
	 * 
	 * @return Returns the value of the mob's inventory
	 */
	public int getWorth()
	{
		int value = fGold;
		
		for (Treasure treasure: fInventory)
			value += treasure.getWorth();
		
		return value;
	}

	/**
	 * Looks through the mob's inventory for a key to the given door
	 * 
	 * @param door_id The ID of the door
	 * @return Returns the key, if one was found; null otherwise
	 */
	public Key findKey(int door_id)
	{
		for (Treasure treasure: fInventory)
		{
			if (!(treasure instanceof Key))
				continue;
			
			Key key = (Key)treasure;
			if (key.getDoorID() == door_id)
				return key;
		}
		
		return null;
	}
	
	/**
	 * Determines which mobs are within range of the given attack
	 * 
	 * @param game The game state
	 * @param attack The attack
	 * @return Returns a vector of attackable mobs
	 */
	public Vector<Mob> findTargets(Game game, Attack attack)
	{
		Vector<Mob> mobs = new Vector<Mob>();
		mobs.addAll(game.getCreatures());
		
		if (game.getHero() != null)
			mobs.add(game.getHero());
		
		Vector<Mob> targets = new Vector<Mob>();
		
		for (Mob m: mobs)
		{
			// Don't target myself
			if (m == this)
				continue;
			
			// Ignore mobs in the same faction
			if (m.getFaction().equals(getFaction()))
				continue;
			
			// How far away?
			double distance = m.getLocation().distance(getLocation());
			distance -= getSize();
			distance -= m.getSize();
			
			// EHUD - allow "slop factor" of 1
			if ((distance < attack.getMinRange() - getReach() - 1) || (distance > attack.getMaxRange() + getReach() + 1))
				continue;
			
			// Check line of sight
			if (!LineOfSight.exists(getLocation(), m.getLocation(), game.getMap()))
				continue;
			
			targets.add(m);
		}
		
		return targets;
	}
	
	/**
	 * Determines which treasure items are within reach of the mob
	 * 
	 * @param game The game state
	 * @return Returns a vector of nearby treasure items
	 */
	public Vector<Treasure> nearbyTreasure(Game game)
	{
		Vector<Treasure> items = new Vector<Treasure>();
		
		for (Treasure treasure: game.getTreasure())
		{
			// How far away?
			double distance = treasure.getLocation().distance(fLocation);
			distance -= getSize();
			distance -= treasure.getSize();
			
			if (distance <= getReach())
				items.add(treasure);
		}
		
		return items;
	}
	
	/**
	 * Determines which doors are within reach of the mob
	 * <BR>
	 * <B>Note</B>: This method ignores open doors
	 * 
	 * @param game The game state
	 * @return Returns a vector of nearby doors
	 */
	public Vector<Door> nearbyDoors(Game game)
	{
		Vector<Door> doors = new Vector<Door>();
		
		for (Tile tile: game.getMap().getTiles())
		{
			// Ignore non-doors
			if (!(tile instanceof Door))
				continue;
			
			Door door = (Door)tile;
			
			// Ignore open doors
			if (door.getState() == Door.OPEN)
				continue;
			
			// Find the nearest point
			Rectangle2D door_rect = door.getArea();
			double left = door_rect.getX();
			double right = left + door_rect.getWidth();
			double top = door_rect.getY();
			double bottom = top + door_rect.getHeight();
			double x = fLocation.getX();
			double y = fLocation.getY();
			if (x < left)
				x = left;
			if (x > right)
				x = right;
			if (y < top)
				y = top;
			if (y > bottom)
				y = bottom;
			Point2D door_point = new Point2D.Double(x, y);
			
			// How far away are we?
			double distance = fLocation.distance(door_point);
			distance -= getSize();
			
			if (distance <= getReach())
				doors.add(door);
		}
		
		return doors;
	}

	/**
	 * Make the mob pick up a treasure item
	 * <BR>
	 * <B>Note</B>: This method does not check for proximity
	 * 
	 * @param game The game state
	 * @param treasure The item to pick up
	 * @return Returns true if the item was picked up; false otherwise
	 */
	public boolean pickUp(Game game, Treasure treasure)
	{
		double weight = getEncumbrance() + treasure.getWeight();
		if (weight > getStrength())
		{
			App.log(treasure + " is too heavy for " + this);
			return false;
		}
		
		if (treasure instanceof Gold)
		{
			Gold g = (Gold)treasure;
			fGold += g.getWorth();
		}
		else
		{
			fInventory.add(treasure);
		}
		
		game.getTreasure().remove(treasure);
		
		App.showLink(this, treasure);
		App.log(this + " picks up " + treasure);
		
		return true;
	}
	
	/**
	 * Make the mob drop a treasure item
	 * 
	 * @param game The game state
	 * @param treasure The item to drop
	 */
	public void drop(Game game, Treasure treasure)
	{
		if (fInventory.contains(treasure))
			fInventory.remove(treasure);
		
		Random rnd = new Random();
			
		int attempts =- 0;
		boolean placed = false;
		while (!placed)
		{
			attempts += 1;
			if (attempts > 100)
			{
				// Couldn't find a place to drop this
				return;
			}
			
			double x = fLocation.getX() - getSize() + (rnd.nextDouble() * getSize() * 2);
			double y = fLocation.getY() - getSize() + (rnd.nextDouble() * getSize() * 2);
			Point2D location = new Point2D.Double(x, y);
			
			placed = treasure.placeAt(location, game);
		}
		game.getTreasure().add(treasure);
		
		App.log(this + " drops " + treasure);
	}
	
	/**
	 * Make the mob consume a treasure item
	 * 
	 * @param treasure The item to consume
	 */
	public void consume(Treasure treasure)
	{
		// Consume item
		boolean used = treasure.onConsume(this);
		if (used)
			fInventory.remove(treasure);
		
		// We don't log anything here, the treasure item will take care of that
	}
	
	/**
	 * Returns the last element of the mobs inventory if this is a potion
	 * 
	 * @return last Element of Inventory if Potion or else null
	 */
	public Potion getLastElementIfPotion() {
		Potion lastPotion = null;
		if (! getInventory().isEmpty()) {
			Treasure last = getInventory().lastElement();
			if (last instanceof Potion) {
				lastPotion = (Potion) last;
			}
		}
		return lastPotion;
	}
	
	/**
	 * Make the mob open a door
	 * <BR>
	 * <B>Note</B>: This method does not check for proximity
	 * 
	 * @param door The door to open
	 */
	public void openDoor(Door door)
	{
		if (door.getState() != Door.CLOSED)
			return;
		
		App.log(getName() + " opens a door");

		door.setState(Door.OPEN);
	}
	
	/**
	 * Make the mob unlock a door
	 * <BR>
	 * <B>Note</B>: This method does not check for proximity
	 * 
	 * @param door The door to unlock
	 * @param key The key to use
	 */
	public void unlockDoor(Door door, Key key)
	{
		if (door.getState() != Door.LOCKED)
			return;
		
		App.log(getName() + " unlocks a door");

		door.setState(Door.CLOSED);
		fInventory.remove(key);
	}
	
	/**
	 * Make the mob take a step in the given direction
	 * 
	 * @param angle The angle specifying the direction (in radians)
	 * @param game The current game state
	 * @return Returns true if the mob moved; false otherwise
	 */
	public boolean move(double angle, Game game)
	{
		double x = getSpeed() * Math.sin(angle);
		double y = getSpeed() * Math.cos(angle);
		
		Point2D pt = new Point2D.Double(getLocation().getX() + x, getLocation().getY() + y);
		Point2D ptOneFurther = new Point2D.Double(pt.getX() + x/5, pt.getY() + y/5);
		
		if (CollisionDetection.canOccupy(game, this, ptOneFurther))
		{
			fLocation = pt;
			return true;
		}
		//else we are suck, so back off a bit
		pt = new Point2D.Double(getLocation().getX() - x, getLocation().getY() - y);
		if (CollisionDetection.canOccupy(game, this, pt))
		{
			fLocation = pt;
		}
		
		return false;//false means we are stuck, can't get to goal
	}
	
	/** Move towards the goal.  If goal is reached, set it to null.
	 * If no goal, do nothing
	 * @param game
	 * @return Returns true if the mob moved; false otherwise
	 */
	public boolean moveToGoal(Game game) {
		if (getGoal() == null)
			return false;
		// Are we there yet?
		//this check in case we had just repositioned, and we are already on goal
		double distance = getLocation().distance(getGoal());
//		if (distance < 1.5*getSpeed())
//		{		setGoal(null, null);
//		return false;
//	}		
		double dx = getGoal().getX() - getLocation().getX();
		double dy = getGoal().getY() - getLocation().getY();
		double theta = Math.atan2(dx, dy);

		boolean moved = move(theta, game);
//		if (moved)
//		{
			// Are we there yet?
			 distance = getLocation().distance(getGoal());
			if (distance < 1.5*getSpeed())
				setGoal(null, null);
//		}
		
		
		return moved;//false means we are stuck, can't get to goal
	}

	
	/**
	 * Draws the mob as a simple filled circle
	 * <UL>
	 * <LI>If the mob is not at full health, a health bar will be drawn below it</LI>
	 * <LI>If the mob is not at full energy, an energy bar will be drawn above it</LI>
	 * </UL>
	 * 
	 * @param g The graphics object
	 * @param rect The rectangle the mob should fill
	 * @param c The colour to draw the mob with
	 * @param highlighted Whether the mob should be highlighted or not
	 */
	public void defaultDraw(Graphics2D g, Rectangle2D rect, Color c, boolean highlighted)
	{
		int red = c.getRed();
		int green = c.getGreen();
		int blue = c.getBlue();
		int alpha = (fCurrentHealth > 0) ? 255 : 100;
		
		Color color = new Color(red, green, blue, alpha);
		
		Ellipse2D circle = new Ellipse2D.Double(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
		
		g.setColor(highlighted ? Color.WHITE : color);
		g.fill(circle);
		
		g.setColor(highlighted ? Color.BLACK : color.darker());
		g.draw(circle);
		
		double bar_height = Math.max(rect.getHeight() * 0.15, 2);
		if (fCurrentEnergy < getMaxEnergy())
		{
			double pc = Math.max(0, (double)fCurrentEnergy / getMaxEnergy());
			
			double y = rect.getY();
			Rectangle2D gauge_rect = new Rectangle2D.Double(rect.getX(), y, rect.getWidth(), bar_height);
			Rectangle2D value_rect = new Rectangle2D.Double(rect.getX(), y, rect.getWidth() * pc, bar_height);
			
			g.setColor(Color.WHITE);
			g.fill(gauge_rect);
						
			g.setColor(Color.BLUE);
			g.fill(value_rect);
			
			g.setColor(Color.BLACK);
			g.draw(gauge_rect);
		}
		
		if (fCurrentHealth < getMaxHealth())
		{
			double pc = Math.max(0, (double)fCurrentHealth / getMaxHealth());
			
			double y = rect.getY() + rect.getHeight() - bar_height;
			Rectangle2D gauge_rect = new Rectangle2D.Double(rect.getX(), y, rect.getWidth(), bar_height);
			Rectangle2D value_rect = new Rectangle2D.Double(rect.getX(), y, rect.getWidth() * pc, bar_height);
			
			g.setColor(Color.WHITE);
			g.fill(gauge_rect);
						
			int n = (int)(255 * pc);
			g.setColor(new Color(255 - n, n, 0));
			g.fill(value_rect);
			
			g.setColor(Color.BLACK);
			g.draw(gauge_rect);
		}
	}
	
	public void load(Node node)
	{
		super.load(node);
		fCurrentHealth = XMLHelper.getIntValue(node, "CurrentHealth");
		fCurrentEnergy = XMLHelper.getIntValue(node, "CurrentEnergy");
		fGold = XMLHelper.getIntValue(node, "Gold");
		
		if (XMLHelper.findChild(node, "Attacks") != null)
			XMLHelper.loadObject(node, "Attacks", fAttacks);
		
		if (XMLHelper.findChild(node, "Inventory") != null)
			XMLHelper.loadObject(node, "Inventory", fInventory);
		
		if (XMLHelper.attributeExists(node, "GoalX") && XMLHelper.attributeExists(node, "GoalY"))
		{
			double x = XMLHelper.getDblValue(node, "GoalX");
			double y = XMLHelper.getDblValue(node, "GoalY");
			
			fGoal = new Point2D.Double(x, y);
		}
		else
		{
			fGoal = null;
		}
	}

	public void save(Node node)
	{
		super.save(node);
		XMLHelper.setIntValue(node, "CurrentHealth", fCurrentHealth);
		XMLHelper.setIntValue(node, "CurrentEnergy", fCurrentEnergy);
		XMLHelper.setIntValue(node, "Gold", fGold);
		
		XMLHelper.saveObject(node, "Attacks", fAttacks);
		XMLHelper.saveObject(node, "Inventory", fInventory);
		
		if (fGoal != null)
		{
			XMLHelper.setDblValue(node, "GoalX", fGoal.getX());
			XMLHelper.setDblValue(node, "GoalY", fGoal.getY());
		}
	}


}
