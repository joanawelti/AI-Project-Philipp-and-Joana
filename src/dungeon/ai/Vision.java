package dungeon.ai;

import java.util.Vector;

import dungeon.model.Game;
import dungeon.model.items.Item;
import dungeon.model.items.mobs.Creature;

public class Vision
{
	public static Vector<Item> getVisibleItems(Creature c, Game game)
	{
		Vector<Item> all_items = new Vector<Item>();
		all_items.addAll(game.getCreatures());
		all_items.addAll(game.getTreasure());
		all_items.add(game.getHero());
		
		Vector<Item> visible_items = new Vector<Item>();
		
		for (Item item : all_items)
		{
			if (item == c)
				continue;
			
			if (LineOfSight.exists(item.getLocation(), c.getLocation(), game.getMap()))
				visible_items.add(item);
		}
		
		return visible_items;
	}
}
