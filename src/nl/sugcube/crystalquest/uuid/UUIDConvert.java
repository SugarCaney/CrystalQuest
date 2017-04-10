package nl.sugcube.crystalquest.uuid;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author SugarCaney
 */
public class UUIDConvert {

    public static void convert(FileConfiguration data, Logger log) {

        if (data.isSet("economy")) {

            log.info("It seems that you last ran an older version");
            log.info("of CrystalQuest. CrystalQuest is now converting");
            log.info("all players' economy to the new UUID format.");
            log.info("This could take a while... Please be patient!");

			/*
             * CRYSTALS
			 */
            for (String playerName : data.getConfigurationSection("economy.crystals").getKeys(false)) {
                UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(playerName));
                Map<String, UUID> response = null;
                try {
                    response = fetcher.call();
                    String uuid = response.get(playerName).toString();

                    data.set("shop.crystals." + uuid + ".xp", data.getInt("economy.crystals." + playerName + ".xp"));
                    data.set("shop.crystals." + uuid + ".smash", data.getInt("economy.crystals." + playerName + ".smash"));
                    data.set("shop.crystals." + uuid + ".win", data.getInt("economy.crystals." + playerName + ".win"));
                    data.set("shop.crystals." + uuid + ".blood", data.getInt("economy.crystals." + playerName + ".blood"));
                }
                catch (Exception e) {
                    log.warning("Oops could't fetch one's UUID, don't worry! The process continues!");
                    e.printStackTrace();
                }
            }
			
			/*
			 * BALANCE
			 */
            for (String playerName : data.getConfigurationSection("economy.balance").getKeys(false)) {
                UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(playerName));
                Map<String, UUID> response = null;
                try {
                    response = fetcher.call();
                    String uuid = response.get(playerName).toString();

                    data.set("shop.balance." + uuid, data.getInt("economy.balance." + playerName));
                }
                catch (Exception e) {
                    log.warning("Oops could't fetch one's UUID, don't worry! The process continues!");
                    e.printStackTrace();
                }
            }
			
			/*
			 * UPGRADE
			 */
            for (String playerName : data.getConfigurationSection("economy.upgrade").getKeys(false)) {
                UUIDFetcher fetcher = new UUIDFetcher(Collections.singletonList(playerName));
                Map<String, UUID> response = null;
                try {
                    response = fetcher.call();
                    String uuid = response.get(playerName).toString();

                    data.set("shop.upgrade." + uuid + ".buff", data.getInt("economy.upgrade." + playerName + ".buff"));
                    data.set("shop.upgrade." + uuid + ".debuff", data.getInt("economy.upgrade." + playerName + ".debuff"));
                    data.set("shop.upgrade." + uuid + ".explosive", data.getInt("economy.upgrade." + playerName + ".explosive"));
                    data.set("shop.upgrade." + uuid + ".weaponry", data.getInt("economy.upgrade." + playerName + ".weaponry"));
                    data.set("shop.upgrade." + uuid + ".creepers", data.getInt("economy.upgrade." + playerName + ".creepers"));
                    data.set("shop.upgrade." + uuid + ".wolf", data.getInt("economy.upgrade." + playerName + ".wolf"));
                }
                catch (Exception e) {
                    log.warning("Oops could't fetch one's UUID, don't worry! The process continues!");
                    e.printStackTrace();
                }
            }
			
			/*
			 * CLASSES
			 */
            for (String playerName : data.getConfigurationSection("economy.classes").getKeys(false)) {
                UUIDFetcher fetcher = new UUIDFetcher(Collections.singletonList(playerName));
                Map<String, UUID> response = null;
                try {
                    response = fetcher.call();
                    String uuid = response.get(playerName).toString();

                    data.set("shop.classes." + uuid, data.getStringList("economy.classes." + playerName));
                }
                catch (Exception e) {
                    log.warning("Oops could't fetch one's UUID, don't worry! The process continues!");
                    e.printStackTrace();
                }
            }

            data.set("economy", null);

            log.info("Finished converting!");

        }

    }

}
