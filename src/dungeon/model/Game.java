package dungeon.model;

import java.util.Vector;

import org.w3c.dom.Node;

import dungeon.App;
import dungeon.collections.CreatureList;
import dungeon.collections.FactionList;
import dungeon.collections.TreasureList;
import dungeon.configuration.ConfigurationHandler;
import dungeon.configuration.Configurations;
import dungeon.model.items.mobs.Creature;
import dungeon.model.items.mobs.Faction;
import dungeon.model.items.mobs.FastOgre;
import dungeon.model.items.mobs.Hero;
import dungeon.model.items.mobs.Mob;
import dungeon.model.items.treasure.Treasure;
import dungeon.model.structure.Finish;
import dungeon.model.structure.LocationHelper;
import dungeon.model.structure.Map;
import dungeon.model.structure.Tile;
import dungeon.utils.Persistent;
import dungeon.utils.XMLHelper;

/**
 * Class to hold game state information
 */
public class Game implements Persistent
{
	/**
	 * @return Returns the name of the dungeon
	 */
	public String getName()
	{
		return fName;
	}
	/**
	 * Sets the name of the dungeon
	 * @param name The name
	 */
	public void setName(String name)
	{
		fName = name;
	}
	private String fName = "";
	
	/**
	 * @return Returns the level of the dungeon
	 */
	public int getLevel()
	{
		return fLevel;
	}
	/**
	 * Sets the level of the dungeon
	 * @param name The level
	 */
	public void setLevel(int level)
	{
		fLevel = level;
	}
	private int fLevel = 0;
	
	/**
	 * @return Returns the dungeon map
	 */
	public Map getMap()
	{
		return fMap;
	}
	private Map fMap = new Map();
	
	/**
	 * @return Returns whether the game is a tournament
	 */
	public boolean isTournament()
	{
		return fTournament;
	}
	private boolean fTournament = false;

	/**
	 * @return Returns the list of factions in the dungeon
	 */
	public FactionList getFactions()
	{
		return fFactions;
	}
	private FactionList fFactions = new FactionList();

	/**
	 * @return Returns the list of creatures in the dungeon
	 */
	public CreatureList getCreatures()
	{
		return fCreatures;
	}
	private CreatureList fCreatures = new CreatureList();
	
	
	/**
	 * @return Returns the list of treasure items in the dungeon
	 */
	public TreasureList getTreasure()
	{
		return fTreasure;
	}
	private TreasureList fTreasure = new TreasureList();
	
	/**
	 * @return Returns the game's hero
	 */
	public Hero getHero()
	{
		return fHero;
	}
	
	
	/**
	 * Sets the game's hero
	 * @param hero The hero
	 */
	public void setHero(Hero hero)
	{
		fHero = hero;
	}
	private Hero fHero = new Hero();
	
	public boolean hasConfigurations() {
		return (fConfigurations != null);
	}
	private Configurations fConfigurations;
	
	private ConfigurationHandler configHandler = new ConfigurationHandler();
	
	public void setConfigurations(Configurations config) {
		fConfigurations = config; 
	}
	
	public Configurations getConfigurations() {
		return fConfigurations;
	}

	
	/**
	 * Advances the game by one round
	 * <BR>
	 * Each round has the following stages:
	 * <UL>
	 * <LI>Each map tile is informed about the new round</LI>
	 * <LI>Each treasure item is informed about the new round</LI>
	 * <LI>For each creature in turn, and then for the hero:</LI>
	 * <UL>
	 * <LI>The tile they are standing on is informed of this fact</LI>
	 * <LI>They are prompted to act</LI>
	 * </UL>
	 * <LI>Dead creatures are removed from the game</LI>
	 * </UL>
	 */
	public void tick()
	{
		for (Tile tile: fMap.getTiles())
			tile.onTick(this);
		
		for (Treasure treasure: fTreasure)
			treasure.onTick(this);
		
		for (Faction faction: fFactions)
			faction.onTick(this);
		
		Vector<Mob> mobs = new Vector<Mob>();
		mobs.addAll(fCreatures);
		
		if (fHero != null)
			mobs.add(fHero);
		
		for (Mob m: mobs)
		{
			// Find the tile this mob is on
			Tile current_tile = fMap.getTileAt(m.getLocation());
			if (current_tile != null)
			{
				// Apply terrain effect
				current_tile.affect(m);
			}
						
			if (m.getCurrentHealth() > 0)
				m.onTick(this);
		}
		
		// Clear dead creatures 
		Vector<Creature> dead = new Vector<Creature>();
		for (Creature c: fCreatures)
		{
			if (c.getCurrentHealth() <= 0)
			{
				c.onDeath(this);
				App.log(c + " is dead");
				dead.add(c);
			}
		}
		fCreatures.removeAll(dead);
		
		
		if (fTournament)
			checkTournamentOver();

		if ((fHero != null) && (fHero.getCurrentHealth() <= 0))
			App.finishGame(false);
	}
	
	private void checkTournamentOver()
	{
		Vector<String> factions = new Vector<String>();
		for (Creature c: fCreatures)
		{
			if ((c.getCurrentHealth() > 0) && (!factions.contains(c.getFaction())))
				factions.add(c.getFaction());
		}
		
		if (factions.size() == 0)
		{
			// simultaneous death
			App.finishTournament("simultaneous death - no winner");
//			System.out.println("strange");
		}
		else if (factions.size() == 1)
		{
			// one faction left
			//do the final tick so learners can update
			//note that only the winners get this tick
			Vector<Mob> mobs = new Vector<Mob>();
			mobs.addAll(fCreatures);
			for (Mob m: mobs)
					m.gameOverTick(this);	
			App.finishTournament(factions.get(0));
		} else {
			// check if any creature is on exit field
			Vector<Mob> mobs = new Vector<Mob>();
			mobs.addAll(fCreatures);
			Finish finish = LocationHelper.getFinish(this.getMap());
			for (Mob m: mobs) {				
				Tile maybeFinish = this.getMap().getTileAt(m.getLocation());
				if (m instanceof FastOgre && finish.equals(maybeFinish)) {
					m.gameOverTick(this);
				}
			}					
		}
	}

	public void load(Node node)
	{
		fName = XMLHelper.getStrValue(node, "Name");
		if (XMLHelper.attributeExists(node, "Tournament")) {
			fTournament = XMLHelper.getBoolValue(node, "Tournament");
		}

		
		if (XMLHelper.attributeExists(node, "Level"))
			fLevel = XMLHelper.getIntValue(node, "Level");
		
		XMLHelper.loadObject(node, "Map", fMap);
		XMLHelper.loadObject(node, "Factions", fFactions);
		XMLHelper.loadObject(node, "Creatures", fCreatures);
		XMLHelper.loadObject(node, "Treasure", fTreasure);
		
		if (XMLHelper.findChild(node, "Hero") != null) {
			XMLHelper.loadObject(node, "Hero", fHero);
		} else {
			fHero = null;
		}
		
		// does this scenario want to provide configuration options?
		if (XMLHelper.findChild(node, "Configurations") != null) {
			XMLHelper.loadObject(node, "Configurations", configHandler);
			setConfigurations(configHandler.getConfigurations());
		}
		
		// Make sure we have all the factions that we're supposed to have
		for (Creature c : fCreatures)
		{
			Faction faction = fFactions.find(c.getFaction());
			if (faction == null)
			{
				faction = new Faction(c.getFaction());
				fFactions.add(faction);
			}
		}
	}

	public void save(Node node)
	{
		XMLHelper.setStrValue(node, "Name", fName);
		if (isTournament())
			XMLHelper.setBoolValue(node, "Tournament", fTournament);

		
		if (fLevel != 1)
			XMLHelper.setIntValue(node, "Level", fLevel);
		
		XMLHelper.saveObject(node, "Map", fMap);
		XMLHelper.saveObject(node, "Factions", fFactions);
		XMLHelper.saveObject(node, "Creatures", fCreatures);
		XMLHelper.saveObject(node, "Treasure", fTreasure);
		
		if (fHero != null)
			XMLHelper.saveObject(node, "Hero", fHero);
	}
}