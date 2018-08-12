package com.buncolak.opendota.data;

import android.util.SparseArray;

public class GameConstants {
    /*
        Mappings for the game data
     */
    public static final SparseArray<String> GAME_MODES = new SparseArray<>();
    public static final SparseArray<String> SKILL_LEVELS = new SparseArray<>();
    static {
        GAME_MODES.append(0, "Unknown");
        GAME_MODES.append(1, "All Pick");
        GAME_MODES.append(2, "Captains Mode");
        GAME_MODES.append(3, "Random Draft");
        GAME_MODES.append(4, "Single Draft");
        GAME_MODES.append(5, "All Random");
        GAME_MODES.append(6, "Intro");
        GAME_MODES.append(7, "Diretide"); // FeelsBadMan
        GAME_MODES.append(8, "Reverse Captains Mode");
        GAME_MODES.append(9, "Greeviling");
        GAME_MODES.append(10, "Tutorial");
        GAME_MODES.append(11, "Mid Only");
        GAME_MODES.append(12, "Least Played");
        GAME_MODES.append(13, "Limited Heroes");
        GAME_MODES.append(14, "Compendium Matchmaking");
        GAME_MODES.append(15, "Custom");
        GAME_MODES.append(16, "Captains Draft");
        GAME_MODES.append(17, "Balanced Draft");
        GAME_MODES.append(18, "Ability Draft");
        GAME_MODES.append(19, "Event");
        GAME_MODES.append(20, "ARDM");
        GAME_MODES.append(21, "1v1 Mid");
        GAME_MODES.append(22, "All Draft");
        GAME_MODES.append(23, "Turbo");
        GAME_MODES.append(24, "Mutation");

        SKILL_LEVELS.append(0, "");
        SKILL_LEVELS.append(1, "Normal Skill");
        SKILL_LEVELS.append(2, "High Skill");
        SKILL_LEVELS.append(3, "Very High Skill");
    }
}
