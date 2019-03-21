package nl.sugcube.crystalquest.uuid;

import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class UUIDEntity {
	
	/**
	 * Search for an entity by UUID.
	 * @param uuid (UUID) The UUID of the entity.
	 * @param world (World) The world to look for the entity.
	 * @return (Entity) The entity with the corresponding UUID.
	 */
	public static Entity getEntity(UUID uuid, World world) {
		
		for (Entity entity : world.getEntities()) {
			if (entity.getUniqueId().equals(uuid)) {
				return entity;
			}
		}
		
		return null;
	}
}
