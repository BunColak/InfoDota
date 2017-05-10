package com.buncolak.opendota.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.buncolak.opendota.data.MatchesDBContract.AllMatchesEntry;

/**
 * Created by bunya on 07-May-17.
 */

public class UserDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "openDotaUsers.db";
    public static final int DATABASE_VERSION = 2;

    public UserDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlDatabaseCreateQuery = "CREATE TABLE " + AllMatchesEntry.TABLE_NAME
                +"(" + AllMatchesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AllMatchesEntry.COLUMN_MATCH_ID + " LONG NOT NULL, "
                + AllMatchesEntry.COLUMN_RADIANT_WIN + " BOOLEAN NOT NULL, "
                + AllMatchesEntry.COLUMN_PLAYER_SLOT + " INTEGER NOT NULL, "
                + AllMatchesEntry.COLUMN_DURATION + " INTEGER NOT NULL, "
                + AllMatchesEntry.COLUMN_GAME_MODE + " INTEGER NOT NULL, "
                + AllMatchesEntry.COLUMN_HERO_ID + " INTEGER NOT NULL, "
                + AllMatchesEntry.COLUMN_START_TIME + " INTEGER NOT NULL, "
                + AllMatchesEntry.COLUMN_KILLS + " INTEGER NOT NULL, "
                + AllMatchesEntry.COLUMN_DEATHS + " INTEGER NOT NULL, "
                + AllMatchesEntry.COLUMN_ASSISTS + " INTEGER NOT NULL, "
                + AllMatchesEntry.COLUMN_SKILL + " INTEGER NOT NULL);";
        db.execSQL(sqlDatabaseCreateQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AllMatchesEntry.TABLE_NAME);
        onCreate(db);
    }
}
