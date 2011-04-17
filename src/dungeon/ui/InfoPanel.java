package dungeon.ui;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;

import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import dungeon.App;
import dungeon.model.items.mobs.Creature;
import dungeon.model.items.mobs.Hero;
import dungeon.model.items.treasure.Key;
import dungeon.model.items.treasure.Treasure;
import dungeon.model.structure.Door;

/**
 * A pane to display information about the hero during the game
 */
public class InfoPanel extends JTextPane
{
	// Required
	private static final long serialVersionUID = -3246710413538688049L;

	/**
	 * Constructor
	 */
	public InfoPanel()
	{
		addHyperlinkListener(fHyperlinkListener);
		
		setEditable(false);
		setContentType("text/html");
	}
	
	public boolean isEditable()
	{
		return false;
	}
	
	/**
	 * Updates the hero information
	 */
	public void updateInfo()
	{
		Hero hero = App.getGame().getHero();
		
		String str = "";
		
		str += "<STYLE type=\"text/css\">";
		str += "body                 { font-family: Arial; font-size: 10pt }";
		str += "h1, h2               { color: #000080 }";
		str += "h1                   { font-size: 24pt; font-weight: bold; text-align: center }";
		str += "h2                   { font-size: 12pt; font-weight: bold }";
		str += "p                    { }";
		str += "table                { border-color: #BBBBBB; border-style: solid; border-width: 1px; border-collapse: collapse; table-layout: fixed; width: 90% }";
		str += "tr                   { }";
		str += "td                   { }";
		str += "ul, ol               { }";
		str += "li                   { }";
		str += "a                    { }";
		str += "a:link               { color: #0000C0 }";
		str += "a:visited            { color: #0000C0 }";
		str += "a:active             { color: #FF0000 }";
		
		str += "<H1 align=center>" + App.getGame().getName() + "</H1>";
		
		if (App.getGame().getLevel() > 0)
			str += "<H2 align=center>Level " + App.getGame().getLevel() + "</H2>";
		
		Vector<String> factions = new Vector<String>();
		for (Creature c: App.getGame().getCreatures())
		{
			String faction = c.getFaction();
			if (faction == "")
				faction = Creature.DEFAULT_FACTION;
			
			if (!factions.contains(faction))
				factions.add(faction);
		}
		Collections.sort(factions);
		
		str += "<P>";
		str += "<TABLE align=center width=90%>";
		str += "<TR>";
		str += "<TD colspan=3 align=center>";
		str += "<B>Factions</B>";
		str += "</TD>";
		str += "</TR>";
		if (factions.size() != 0)
		{
			for (String faction_name : factions)
			{
				int count = 0;
				int score = 0;
				
				for (Creature c: App.getGame().getCreatures())
				{
					if (!faction_name.equals(c.getFaction()))
						continue;
					
					count += 1;
					score += c.getGold();
				}
				
				str += "<TR>";
				
				str += "<TD>";
				str += "<B>" + faction_name + "</B>";
				str += "</TD>";
				
				str += "<TD align=right>";
				str += count + " remaining";
				str += "</TD>";
				
				str += "<TD align=right>";
				str += score + " gold";
				str += "</TD>";
				
				str += "</TR>";
			}
		}
		else
		{
			str += "<TR>";
			str += "<TD colspan=3>";
			str += "(none)";
			str += "</TD>";
			str += "</TR>";
		}
		str += "</TABLE>";
		str += "</P>";
				
		if (hero != null)
		{
			str += "<P>";
			str += "<TABLE align=center width=90%>";
			
			str += "<TR>";
			str += "<TD align=right>";
			str += "<B>Health:</B>";
			str += "</TD>";
			str += "<TD>";
			str += Math.max(hero.getCurrentHealth(), 0) + " / " + hero.getMaxHealth();
			str += "</TD>";
			str += "</TR>";
			
			str += "<TR>";
			str += "<TD align=right>";
			str += "<B>Energy:</B>";
			str += "</TD>";
			str += "<TD>";
			str += hero.getCurrentEnergy() + " / " + hero.getMaxEnergy();
			str += "</TD>";
			str += "</TR>";
			
			str += "<TR>";
			str += "<TD align=right>";
			str += "<B>Encumbrance:</B>";
			str += "</TD>";
			str += "<TD>";
			str += new DecimalFormat("#.#").format(hero.getEncumbrance()) + " / " + hero.getStrength();
			str += "</TD>";
			str += "</TR>";
			
			str += "<TR>";
			str += "<TD align=right>";
			str += "<B>Score:</B>";
			str += "</TD>";
			str += "<TD>";
			str += hero.getWorth() + " gold";
			str += "</TD>";
			str += "</TR>";
			
			
			str += "</TABLE>";
			str += "</P>";
			
			if (hero.getInventory().size() != 0)
			{
				HashMap<String, Vector<Treasure>> map = new HashMap<String, Vector<Treasure>>();
				for (Treasure treasure: hero.getInventory())
				{
					Vector<Treasure> items = map.get(treasure.toString());
					if (items == null)
					{
						items = new Vector<Treasure>();
						map.put(treasure.getName(), items);
					}
					items.add(treasure);
				}
				
				str += "<P>Carried:</P>";
				str += "<UL>";
				
				Vector<String> item_names = new Vector<String>(map.keySet());
				Collections.sort(item_names);
				
				for (String item: item_names)
				{
					str += "<LI>";
					
					Vector<Treasure> items = map.get(item);
					Treasure treasure = items.get(0);
					
					str += treasure;
					
					if (items.size() > 1)
						str += " x" + items.size();
					
					str += " (";
					if (treasure.isConsumable())
						str += "<A href=\"consume:" + treasure.getID() + "\">use</A>, ";
					str += "<A href=\"drop:" + treasure.getID() + "\">drop</A>";
					str += ")";
					
					str += "</LI>";
				}
				
				str += "</UL>";
			}
			
			if (App.getTimer().isRunning())
			{
				Vector<Treasure> detritus = hero.nearbyTreasure(App.getGame());
				Vector<Door> doors = hero.nearbyDoors(App.getGame());
				
				if ((detritus.size() != 0) || (doors.size() != 0))
				{
					str += "<P>Options:</P>";
					
					str += "<UL>";
					
					for (Treasure treasure: detritus)
					{
						str += "<LI>";
						
						str += "<A href=\"pick_up:" + treasure.getID() + "\">";
						str += "Pick up " + treasure;
						str += "</A>";
						
						str += "</LI>";
					}
					
					for (Door door: doors)
					{
						str += "<LI>";
						
						str += "<A href=\"door:" + door.getID() + "\">";
						switch (door.getState())
						{
						case Door.CLOSED:
							str += "Open door";
							break;
						case Door.LOCKED:
							str += "Unlock door";
							break;
						}
						str += "</A>";
						
						str += "</LI>";
					}
					
					str += "</UL>";
				}
			}
		}
		else
		{
			str += "<P align=center>(no hero)</P>";
			
		} 
		
		setText(str);
	}
	
	/**
	 * Handles hyperlinks in the pane
	 * <BR>
	 * Hyperlinks are used to allow the user to make the hero drink potions, etc
	 */
	HyperlinkListener fHyperlinkListener = new HyperlinkListener()
	{
		public void hyperlinkUpdate(HyperlinkEvent e)
		{
			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
			{
				String link = e.getDescription();
				String[] tokens = link.split(":");
				if (tokens.length != 2)
					return;
				
				UUID id = UUID.fromString(tokens[1]);
				Hero hero = App.getGame().getHero();

				if (tokens[0].equals("drop"))
				{
					Treasure treasure = hero.getInventory().find(id);
					if (treasure == null)
						return;

					hero.drop(App.getGame(), treasure);
				}
				
				if (tokens[0].equals("consume"))
				{
					Treasure treasure = hero.getInventory().find(id);
					if (treasure == null)
						return;

					hero.consume(treasure);
				}
				
				if (tokens[0].equals("pick_up"))
				{
					Treasure treasure = App.getGame().getTreasure().find(id);
					if (treasure == null)
						return;

					hero.pickUp(App.getGame(), treasure);
				}
				
				if (tokens[0].equals("door"))
				{
					Door door = (Door)App.getGame().getMap().getTiles().find(id);
					if (door == null)
						return;

					switch (door.getState())
					{
					case Door.CLOSED:
						hero.openDoor(door);
						break;
					case Door.LOCKED:
						{
							Key key = hero.findKey(door.getKeyID());
							if (key != null)
							{
								hero.unlockDoor(door, key);
							}
							else
							{
								App.log("You do not have a key for this door");
							}
						}
						break;
					}
				}
			}
		}
	};
}
