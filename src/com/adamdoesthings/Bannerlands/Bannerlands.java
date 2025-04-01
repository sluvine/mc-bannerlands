package com.adamdoesthings.Bannerlands;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.java.JavaPlugin;

public class Bannerlands extends JavaPlugin {
	
	/* Notes/To-Do
	 * 
	 * Check to see if banner is underground (not sky above) & disregard it if so
	 * Make radius configurable through yml settings
	 * 
	 */
	
	public ArrayList<EntityType> MonsterEntityTypes;
	public ArrayList<SpawnReason> AllowedSpawnReasons;
	public ArrayList<Material> MaterialsToCheck;
	
	// fired when plugin is first enabled
	@Override
	public void onEnable() {
		// debug message
		getServer().getConsoleSender().sendMessage(this.getName()  + "Adding entity types to list for spawn checking...");
		MonsterEntityTypes = new ArrayList<EntityType>();
		
		MonsterEntityTypes.add(EntityType.CREEPER);			// creeper
		MonsterEntityTypes.add(EntityType.ENDERMAN);		// enderman
		MonsterEntityTypes.add(EntityType.SKELETON);		// skeleton
		MonsterEntityTypes.add(EntityType.SPIDER);			// spider
		MonsterEntityTypes.add(EntityType.ZOMBIE);			// zombie
		MonsterEntityTypes.add(EntityType.ZOMBIE_VILLAGER);	// zombie_villager, added 1.0.1
		MonsterEntityTypes.add(EntityType.WITCH);			// witch, added 1.0.1
		MonsterEntityTypes.add(EntityType.DROWNED);			// drowned (zombie), added 1.1.0
		MonsterEntityTypes.add(EntityType.HUSK);			// husk (zombie), added 1.1.0
		// MonsterEntityTypes.add(EntityType.PILLAGER);		// pillager, added for consideration 1.1.1 but wait til we can make a pillager invasion
															// invasion would be when > [invasion_thresh] banners are within a certain area, small chance to
															// spawn large num of pillagers
		
		for (EntityType type : MonsterEntityTypes) {
			getServer().getConsoleSender().sendMessage("    Added " + type.toString());
		}
		
		// debug message
		getServer().getConsoleSender().sendMessage(this.getName()  + "Adding spawnreason types to list for exclusion from spawn cancelling...");
		AllowedSpawnReasons = new ArrayList<SpawnReason>();
		
		//AllowedSpawnReasons.add(SpawnReason.CHUNK_GEN);		// spawned due to chunk generation
		AllowedSpawnReasons.add(SpawnReason.INFECTION);		// spawned due to zombie infecting villager
		AllowedSpawnReasons.add(SpawnReason.NETHER_PORTAL);	// spawned from nether portal
		AllowedSpawnReasons.add(SpawnReason.SPAWNER);		// spawned from spawner block
		//AllowedSpawnReasons.add(SpawnReason.SPAWNER_EGG);	// spawned from spawner egg
		AllowedSpawnReasons.add(SpawnReason.EGG);			// spawned from regular egg (like chicken)
		AllowedSpawnReasons.add(SpawnReason.TRAP);			// spawned from trap for approaching players
		AllowedSpawnReasons.add(SpawnReason.DISPENSE_EGG);	// spawned from egg dispenser, added 1.1.1
		
		for (SpawnReason type : AllowedSpawnReasons) {
			getServer().getConsoleSender().sendMessage("    Added " + type.toString());
		}
		
		getServer().getConsoleSender().sendMessage(this.getName()  + "Adding banner material types to list for block type checks...");
		MaterialsToCheck = new ArrayList<Material>();
		
		//MaterialsToCheck.add(Material.BANNER);					// default banner
		//MaterialsToCheck.add(Material.LEGACY_STANDING_BANNER);	// standing banner
		//MaterialsToCheck.add(Material.LEGACY_WALL_BANNER;			// hanging banner
		MaterialsToCheck.add(Material.BLACK_BANNER);
		MaterialsToCheck.add(Material.BLACK_WALL_BANNER);
		MaterialsToCheck.add(Material.BLUE_BANNER);
		MaterialsToCheck.add(Material.BLUE_WALL_BANNER);
		MaterialsToCheck.add(Material.BROWN_BANNER);
		MaterialsToCheck.add(Material.BROWN_WALL_BANNER);
		MaterialsToCheck.add(Material.CYAN_BANNER);
		MaterialsToCheck.add(Material.CYAN_WALL_BANNER);
		MaterialsToCheck.add(Material.GRAY_BANNER);
		MaterialsToCheck.add(Material.GRAY_WALL_BANNER);
		MaterialsToCheck.add(Material.GREEN_BANNER);
		MaterialsToCheck.add(Material.GREEN_WALL_BANNER);
		MaterialsToCheck.add(Material.LIGHT_BLUE_BANNER);
		MaterialsToCheck.add(Material.LIGHT_BLUE_WALL_BANNER);
		MaterialsToCheck.add(Material.LIGHT_GRAY_BANNER);
		MaterialsToCheck.add(Material.LIGHT_GRAY_WALL_BANNER);
		MaterialsToCheck.add(Material.LIME_BANNER);
		MaterialsToCheck.add(Material.LIME_WALL_BANNER);
		MaterialsToCheck.add(Material.MAGENTA_BANNER);
		MaterialsToCheck.add(Material.MAGENTA_WALL_BANNER);
		MaterialsToCheck.add(Material.ORANGE_BANNER);
		MaterialsToCheck.add(Material.ORANGE_WALL_BANNER);
		MaterialsToCheck.add(Material.PINK_BANNER);
		MaterialsToCheck.add(Material.PINK_WALL_BANNER);
		MaterialsToCheck.add(Material.PURPLE_BANNER);
		MaterialsToCheck.add(Material.PURPLE_WALL_BANNER);
		MaterialsToCheck.add(Material.RED_BANNER);
		MaterialsToCheck.add(Material.RED_WALL_BANNER);
		MaterialsToCheck.add(Material.WHITE_BANNER);
		MaterialsToCheck.add(Material.WHITE_WALL_BANNER);
		MaterialsToCheck.add(Material.YELLOW_BANNER);
		MaterialsToCheck.add(Material.YELLOW_WALL_BANNER);
		
		for (Material type : MaterialsToCheck) {
			getServer().getConsoleSender().sendMessage("    Added " + type.toString());
		}
		
		getServer().getPluginManager().registerEvents(new CreatureSpawnEventListener(this), this);
	}
	
	// fired when plugin is disabled
	@Override
	public void onDisable() {
		
	}
}


class CreatureSpawnEventListener implements Listener {
	private final Bannerlands plugin;
	private boolean CancelSpawn;
	// private Block BannerBlock;
	
	public CreatureSpawnEventListener(Bannerlands instance) {
		this.plugin = instance;
		CancelSpawn = false;
	}
	
	public void checkForBanners(Location location, int xRadius, int yRadius, int zRadius) {
		
		for(int x = location.getBlockX() - xRadius; x <= location.getBlockX() + xRadius; x++) {
            for(int y = location.getBlockY() - yRadius; y <= location.getBlockY() + yRadius; y++) {
                for(int z = location.getBlockZ() - zRadius; z <= location.getBlockZ() + zRadius; z++) {
                   if (this.plugin.MaterialsToCheck.contains(location.getWorld().getBlockAt(x, y, z).getType())) {
                	   CancelSpawn = true;
                	   //BannerBlock = location.getWorld().getBlockAt(x, y, z);
                	   break;
                   }
                }
                if (CancelSpawn) { break; }
            }
            if (CancelSpawn) { break; }
        }
	}
	
	// event handler for creature spawn event
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent spawnEvent) {
		// debug message
		//this.plugin.getServer().broadcastMessage("Creature attempting to spawn...");
		
		if (this.plugin.MonsterEntityTypes.contains(spawnEvent.getEntityType())) {
			if (!this.plugin.AllowedSpawnReasons.contains(spawnEvent.getSpawnReason())) {
				//this.plugin.getServer().broadcastMessage("Cancel-able " + spawnEvent.getEntityType().toString() + " spawn detected.");
				//this.plugin.getServer().broadcastMessage(spawnEvent.getSpawnReason().toString() + " generated spawn event.");
				//this.plugin.getServer().broadcastMessage("Location of spawn: " + spawnEvent.getLocation().toString());
				
				//this.plugin.getServer().broadcastMessage("Checking surrounding blocks...");
				
				checkForBanners(spawnEvent.getLocation(), 25, 8, 25);
				
				if (!CancelSpawn) {
					//this.plugin.getServer().broadcastMessage("No banner found near spawn point, spawn allowed.");
				}
				else {
					//this.plugin.getServer().broadcastMessage("");
					//this.plugin.getServer().broadcastMessage("!!!!!!!!!!!!!!");
					//this.plugin.getServer().broadcastMessage("Cancelled spawn due to banner found at location '" + BannerBlock.getLocation() + "'.");
					//this.plugin.getServer().broadcastMessage("!!!!!!!!!!!!!!");
					//this.plugin.getServer().broadcastMessage("");
					spawnEvent.setCancelled(true);
					CancelSpawn = false;
				}
			}
			else
			{
				//this.plugin.getServer().broadcastMessage("Allowed spawn of " + spawnEvent.getEntityType().toString() + "due to allowed spawn reason.");
			}
		}
		else
		{
			//this.plugin.getServer().broadcastMessage("Allowed spawn of " + spawnEvent.getEntityType().toString() + " due to allowed entity type.");
		}
	}
}