package dungeon;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import java.util.Vector;

import dungeon.ai.CollisionDetection;
import dungeon.model.Game;
import dungeon.model.items.Item;
import dungeon.model.items.mobs.Creature;
import dungeon.model.items.mobs.Hero;
import dungeon.model.items.mobs.Ogre;
import dungeon.model.items.mobs.Orc;
import dungeon.model.items.treasure.Gold;
import dungeon.model.items.treasure.Key;
import dungeon.model.items.treasure.Potion;
import dungeon.model.items.treasure.Treasure;
import dungeon.model.structure.Door;
import dungeon.model.structure.Finish;
import dungeon.model.structure.FlameTrap;
import dungeon.model.structure.Floor;
import dungeon.model.structure.Map;
import dungeon.model.structure.Pit;
import dungeon.model.structure.Tile;

/**
 * Class for creating pseudo-random dungeons
 */
public class DungeonBuilder
{
	/**
	 * Creates a random dungeon
	 * 
	 * @param name The name of the dungeon to create
	 * @return Returns the resulting Game object
	 */
	public static Game createDungeon(String name, int level)
	{
		int id = name.hashCode() + level;
		return create_dungeon(name, level, id);
	}
	
	/**
	 * Creates a random dungeon using a specific seed ID
	 * 
	 * @param name The name of the dungeon
	 * @param id The ID used to seed the random number generator
	 * @return Returns the resulting Game object
	 */
	static Game create_dungeon(String name, int level, int id)
	{
		Game game = new Game();
		game.setName(name);
		game.setLevel(level);
		
		Random rnd = new Random(id);
		
		double corridor_size = game.getHero().getSize() * 5;
		
		// Create rooms and corridors etc
		Vector<Floor> rooms = add_rooms(game, corridor_size, rnd);
		add_pits(game, rooms, corridor_size, rnd);
		add_finishes(game, rooms, corridor_size, rnd);
				
		populate(game, rooms, rnd);
		
		return game;
	}
	
	static Vector<Floor> add_rooms(Game game, double corridor_size, Random rnd)
	{
		int room_count = 2 + rnd.nextInt(5) + rnd.nextInt(5);
		
		double padding = game.getHero().getSize() * 2;

		Vector<Floor> rooms = new Vector<Floor>();
		while (rooms.size() != room_count)
		{
			double room_width = (1 + rnd.nextDouble() + rnd.nextDouble()) * 10;
			double room_height = (1 + rnd.nextDouble() + rnd.nextDouble()) * 10;

			room_width = Math.max(room_width, corridor_size);
			room_height = Math.max(room_height, corridor_size);
			
			room_width = round_value(room_width);
			room_height = round_value(room_height);
			
			if (rooms.size() == 0)
			{
				// Create a room to start with
				Floor first_room = new Floor(0, 0, room_width, room_height);
				
				rooms.add(first_room);
				game.getMap().getTiles().add(first_room);
			}
			else
			{
				// Pick a room at random
				int room_index = rnd.nextInt(rooms.size());
				Floor previous_room = rooms.get(room_index);
				Rectangle2D prev_rect = previous_room.getArea();
				
				double corridor_length = (1 + rnd.nextDouble()) * 10;
				corridor_length = Math.max(corridor_length, corridor_size);
				corridor_length = round_value(corridor_length);
				
				Rectangle2D corridor = null;
				Rectangle2D room = null;
				
				// Pick a direction
				int direction = rnd.nextInt(4);
				switch (direction)
				{
				case 0:
				{
					// North
					double corridor_x = prev_rect.getX() + (rnd.nextDouble() * (prev_rect.getWidth() - corridor_size));
					double room_x = corridor_x - (rnd.nextDouble() * (room_width - corridor_size));
					
					corridor_x = round_value(corridor_x);
					room_x = round_value(room_x);
					
					double corridor_y = prev_rect.getY() - corridor_length;
					double room_y = corridor_y - room_height;
					corridor = new Rectangle2D.Double(corridor_x, corridor_y, corridor_size, corridor_length);
					room = new Rectangle2D.Double(room_x, room_y, room_width, room_height);
				}
					break;
				case 1:
				{
					// East
					double corridor_y = prev_rect.getY() + (rnd.nextDouble() * (prev_rect.getHeight() - corridor_size));
					double room_y = corridor_y - (rnd.nextDouble() * (room_height - corridor_size));
					
					corridor_y = round_value(corridor_y);
					room_y = round_value(room_y);
					
					double corridor_x = prev_rect.getX() + prev_rect.getWidth();
					double room_x = corridor_x + corridor_length; 
					corridor = new Rectangle2D.Double(corridor_x, corridor_y, corridor_length, corridor_size);
					room = new Rectangle2D.Double(room_x, room_y, room_width, room_height);
				}
					break;
				case 2:
				{
					// South
					double corridor_x = prev_rect.getX() + (rnd.nextDouble() * (prev_rect.getWidth() - corridor_size));
					double room_x = corridor_x - (rnd.nextDouble() * (room_width - corridor_size));
					
					corridor_x = round_value(corridor_x);
					room_x = round_value(room_x);
					
					double corridor_y = prev_rect.getY() + prev_rect.getHeight();
					double room_y = corridor_y + corridor_length;
					corridor = new Rectangle2D.Double(corridor_x, corridor_y, corridor_size, corridor_length);
					room = new Rectangle2D.Double(room_x, room_y, room_width, room_height);
				}
					break;
				case 3:
				{
					// West
					double corridor_y = prev_rect.getY() + (rnd.nextDouble() * (prev_rect.getHeight() - corridor_size));
					double room_y = corridor_y - (rnd.nextDouble() * (room_height - corridor_size));
					
					corridor_y = round_value(corridor_y);
					room_y = round_value(room_y);
					
					double corridor_x = prev_rect.getX() - corridor_length;
					double room_x = corridor_x - room_width; 
					corridor = new Rectangle2D.Double(corridor_x, corridor_y, corridor_length, corridor_size);
					room = new Rectangle2D.Double(room_x, room_y, room_width, room_height);
				}
					break;
				}
				
				if (overlaps(corridor, game.getMap()))
					continue;
				
				Rectangle2D padded = new Rectangle2D.Double(room.getX() - padding, room.getY() - padding, room.getWidth() + (2 * padding), room.getHeight() + (2 * padding));
				if (overlaps(padded, game.getMap()))
					continue;
				
				// The corridor might end with a door
				if (rnd.nextInt(5) != 0)
				{
					double door_size = 2;
					
					Rectangle2D door_rect = null;
					
					switch (direction)
					{
					case 0:
					{
						// North
						door_rect = new Rectangle2D.Double(corridor.getX(), corridor.getY(), corridor.getWidth(), door_size);
						corridor = new Rectangle2D.Double(corridor.getX(), corridor.getY() + door_size, corridor.getWidth(), corridor.getHeight() - door_size);
					}
						break;
					case 1:
					{
						// East
						door_rect = new Rectangle2D.Double(room.getX() - door_size, corridor.getY(), door_size, corridor.getHeight());
						corridor = new Rectangle2D.Double(corridor.getX(), corridor.getY(), corridor.getWidth() - door_size, corridor.getHeight());
					}
						break;
					case 2:
					{
						// South
						door_rect = new Rectangle2D.Double(corridor.getX(), room.getY() - door_size, corridor.getWidth(), door_size);
						corridor = new Rectangle2D.Double(corridor.getX(), corridor.getY(), corridor.getWidth(), corridor.getHeight() - door_size);
					}
						break;
					case 3:
					{
						// West
						door_rect = new Rectangle2D.Double(corridor.getX(), corridor.getY(), door_size, corridor.getHeight());
						corridor = new Rectangle2D.Double(corridor.getX() + door_size, corridor.getY(), corridor.getWidth() - door_size, corridor.getHeight());
					}
						break;
					}
					
					int state = rnd.nextBoolean() ? Door.LOCKED : Door.CLOSED;
					Door door = new Door(door_rect, state);
					game.getMap().getTiles().add(door);
				}
				
				// The corridor might be trapped
				Tile tile_corridor = (rnd.nextInt(8) != 0) ? new Floor(corridor) : new FlameTrap(corridor);
				Floor tile_room = new Floor(room);
				
				game.getMap().getTiles().add(tile_corridor);
				game.getMap().getTiles().add(tile_room);
				
				rooms.add(tile_room);
			}
		}
		
		return rooms;
	}
		
	static void add_pits(Game game, Vector<Floor> rooms, double corridor_size, Random rnd)
	{
		int pit_count = rnd.nextInt(rooms.size() / 3);
		int pit_added = 0;
		while (pit_added != pit_count)
		{
			int room_index = rnd.nextInt(rooms.size());
			Floor room = rooms.get(room_index);
			Rectangle2D rect = room.getArea();
			
			double available_width = rect.getWidth() - (2 * corridor_size);
			double available_height = rect.getHeight() - (2 * corridor_size);
			
			if ((available_width < corridor_size) || (available_height < corridor_size))
				continue;
			
			double width = Math.max(rnd.nextDouble() * available_width, corridor_size);
			double height = Math.max(rnd.nextDouble() * available_height, corridor_size);
			
			width = round_value(width);
			height = round_value(height);
			
			double x = rect.getX() + corridor_size + (rnd.nextDouble() * (available_width - width));
			double y = rect.getY() + corridor_size + (rnd.nextDouble() * (available_height - height));
			
			x = round_value(x);
			y = round_value(y);

			game.getMap().getTiles().add(new Pit(x, y, width, height));
			pit_added += 1;
		}
	}
	
	static void add_finishes(Game game, Vector<Floor> rooms, double corridor_size, Random rnd)
	{
		int finish_count = rnd.nextInt(rooms.size() / 4) + 1;
		int finish_added = 0;
		while (finish_added != finish_count)
		{
			int room_index = rnd.nextInt(rooms.size());
			Floor room = rooms.get(room_index);
			Rectangle2D rect = room.getArea();
			
			double x = 0;
			double y = 0;
			double width = corridor_size;
			double height = corridor_size;
			
			int direction = rnd.nextInt(4);
			switch (direction)
			{
			case 0:
				// North
				x = rect.getX() + (rnd.nextDouble() * (rect.getWidth() - width));
				y = rect.getY() - height;
				break;
			case 1:
				// East
				x = rect.getX() + rect.getWidth();
				y = rect.getY() + (rnd.nextDouble() * (rect.getHeight() - height));
				break;
			case 2:
				// South
				x = rect.getX() + (rnd.nextDouble() * (rect.getWidth() - width));
				y = rect.getY() + rect.getHeight();
				break;
			case 3:
				// West
				x = rect.getX() - width;
				y = rect.getY() + (rnd.nextDouble() * (rect.getHeight() - height));
				break;
			}
			
			x = round_value(x);
			y = round_value(y);
			
			// Does this overlap anything?
			Rectangle2D finish_rect = new Rectangle2D.Double(x, y, width, height);
			if (overlaps(finish_rect, game.getMap()))
				continue;
			
			Finish finish = new Finish(finish_rect);
			
			if (game.getLevel() != 0)
			{
				// Is it a stairway?
				if (rnd.nextInt(4) != 0)
				{
					// Stairs go 1 - 3 levels down
					int level_change = 0;
					switch (rnd.nextInt(6))
					{
					case 0:
					case 1:
					case 2:
						level_change = 1;
						break;
					case 3:
					case 4:
						level_change = 2;
						break;
					case 5:
						level_change = 3;
						break;
					}
					
					finish.setLevelChange(level_change);
				}
			}
			
			// Add it
			game.getMap().getTiles().add(finish);
			finish_added += 1;
		}
	}
	
	static void populate(Game game, Vector<Floor> rooms, Random rnd)
	{
		int orc_count = rnd.nextInt(rooms.size() * 4);
		int orc_added = 0;
		while (orc_added != orc_count)
		{
			place_item(new Orc(), null, game, rnd);
			orc_added += 1;
		}
		
		int ogre_count = rnd.nextInt(rooms.size() / 2);
		int ogre_added = 0;
		while (ogre_added != ogre_count)
		{
			place_item(new Ogre(), null, game, rnd);
			ogre_added += 1;
		}
		
		int key_count = count_locked_doors(game.getMap());
		int key_added = 0;
		while (key_added != key_count)
		{
			place_item(new Key(), null, game, rnd);
			key_added += 1;
		}
		
		int gold_count = rnd.nextInt(rooms.size() * 2);
		int gold_added = 0;
		while (gold_added != gold_count)
		{
			int worth = 10 * (1 + rnd.nextInt(20));
			place_item(new Gold(worth), null, game, rnd);
			gold_added += 1;
		}
		
		int potion_count = rnd.nextInt(rooms.size() * 2);
		int potion_added = 0;
		while (potion_added != potion_count)
		{
			int type = (rnd.nextBoolean() ? Potion.POTION_HEALTH : Potion.POTION_ENERGY);
			place_item(new Potion(type), null, game, rnd);
			potion_added += 1;
		}
		
		// Set hero location
		place_item(new Hero(), null, game, rnd);
	}
	
	/**
	 * Determines whether a given rectangle intersects any tile in the game
	 * 
	 * @param rect The rectangle to check
	 * @param map The map containing the current set of floor tiles
	 * @return Returns true if rect intersects any tile; false otherwise
	 */
	static boolean overlaps(Rectangle2D rect, Map map)
	{
		for (Tile t: map.getTiles())
		{
			if (t.getArea().intersects(rect))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Randomly places an item on the game board, making sure it does not
	 * occupy an invalid location
	 * 
	 * @param item The item to place (can be a creature, hero or treasure)
	 * @param tile The tile to place it on, or null to place it anywhere on the board
	 * @param game The current Game object
	 * @param rnd The random number generator to use
	 * @return Returns true if the item was placed successfully; false otherwise
	 */
	static boolean place_item(Item item, Tile tile, Game game, Random rnd)
	{
		int max_attempts = 1000;
		
		Rectangle2D bounds = (tile != null) ? tile.getArea() : game.getMap().getBounds(0);
		
		int attempts = 0;
		while (attempts != max_attempts)
		{
			attempts += 1;
			
			double x = (rnd.nextDouble() * bounds.getWidth()) + bounds.getX();
			double y = (rnd.nextDouble() * bounds.getHeight()) + bounds.getY();
			
			x = round_value(x);
			y = round_value(y);
			
			Point2D pt = new Point2D.Double(x,y);
			Tile t = game.getMap().getTileAt(pt);
			
			if ((CollisionDetection.canOccupy(game, item, pt) && (t instanceof Floor)))
			{
				item.placeAt(pt, game);
				
				if (item instanceof Creature)
					game.getCreatures().add((Creature)item);
				
				if (item instanceof Treasure)
					game.getTreasure().add((Treasure)item);
				
				if (item instanceof Hero)
					game.setHero((Hero)item);
				
				return true;
			}
		}
		
		return false;
	}
	
	static int count_locked_doors(Map map)
	{
		int count = 0;
		
		for (Tile t: map.getTiles())
		{
			if (t instanceof Door)
			{
				Door d = (Door)t;
				if (d.getState() == Door.LOCKED)
					count += 1;
			}
		}
		
		return count;
	}
	
	private static double round_value(double value)
	{
		return Math.floor(value + 0.5);
	}
}
