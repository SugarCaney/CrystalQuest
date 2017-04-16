package nl.sugcube.crystalquest.command;

import nl.sugcube.crystalquest.CrystalQuest;
import nl.sugcube.crystalquest.game.Arena;
import nl.sugcube.crystalquest.game.CrystalQuestTeam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SugarCaney
 */
public enum AutoCompleteArgument {

    /**
     * Autocompletes with the arenas name, or the ID when the name is not supported.
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
            for (CrystalQuestTeam team : CrystalQuestTeam.getTeams()) {
                options.add(team.getName());
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