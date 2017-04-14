package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Teams;
import nl.sugcube.crystalquest.game.Arena;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SugarCaney
 */
public enum AutoCompleteArgument {

    /**
     * Autocompletes with the arena name, or the ID when the name is not supported.
     */
    ARENA {
        @Override
        public List<String> optionsBase(CrystalQuest plugin) {
            List<String> options = new ArrayList<>();

            // Then add the rest.
            for (Arena arena : plugin.getArenaManager().getArenas()) {
                String name = arena.getName();

                if (name == null || "".equals(name)) {
                    options.add(Integer.toString(arena.getId()));
                }
                else {
                    options.add(name);
                }
            }

            return options;
        }
    },

    /**
     * Autocompletes with the names of all colours.
     */
    TEAMS {
        @Override
        public List<String> optionsBase(CrystalQuest plugin) {
            List<String> options = new ArrayList<>();

            // Add all teams.
            for (int i = 0; i < 8; i++) {
                options.add(Teams.getColourName(i));
            }

            return options;
        }
    },

    /**
     * Clear keyword.
     */
    CLEAR {
        @Override
        protected List<String> optionsBase(CrystalQuest plugin) {
            return new ArrayList<String>() {{
                add("clear");
            }};
        }
    },

    /**
     * Reset keyword.
     */
    RESET {
        @Override
        protected List<String> optionsBase(CrystalQuest plugin) {
            return new ArrayList<String>() {{
                add("reset");
            }};
        }
    },

    /**
     * Setup keyword.
     */
    SETUP {
        @Override
        protected List<String> optionsBase(CrystalQuest plugin) {
            return new ArrayList<String>() {{
                add("setup");
            }};
        }
    },

    /**
     * Give|Set options for money.
     */
    MONEY {
        @Override
        protected List<String> optionsBase(CrystalQuest plugin) {
            return new ArrayList<String>() {{
                add("set");
                add("give");
            }};
        }
    },

    /**
     * The different positions in the /cq pos command.
     */
    POSITIONS {
        @Override
        protected List<String> optionsBase(CrystalQuest plugin) {
            return new ArrayList<String>() {{
                add("1");
                add("2");
            }};
        }
    };

    public List<String> options(String typed, CrystalQuest plugin) {
        List<String> options = optionsBase(plugin);
        options.removeIf(option -> !option.toLowerCase().startsWith(typed.toLowerCase()));
        return options;
    }

    protected abstract List<String> optionsBase(CrystalQuest plugin);
}