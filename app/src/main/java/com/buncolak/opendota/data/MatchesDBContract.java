package com.buncolak.opendota.data;

import android.provider.BaseColumns;

/**
 * Created by bunya on 07-May-17.
 */

public class MatchesDBContract {
    public static final class AllMatchesEntry implements BaseColumns{
        public static final String TABLE_NAME = "user_all_matches";
        public static final String COLUMN_MATCH_ID = "match_id";
        public static final String COLUMN_RADIANT_WIN = "radi_win";
        public static final String COLUMN_PLAYER_SLOT = "player_slot";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_GAME_MODE = "game_mode";
        public static final String COLUMN_HERO_ID = "hero_id";
        public static final String COLUMN_START_TIME = "start_time";
        public static final String COLUMN_KILLS = "kills";
        public static final String COLUMN_DEATHS = "deaths";
        public static final String COLUMN_ASSISTS = "assists";
        public static final String COLUMN_SKILL= "skill";

    }
}
