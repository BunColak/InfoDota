package com.buncolak.opendota.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;

import com.buncolak.opendota.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bunya on 07-May-17.
 */

public class ODJsonParser {

    /*
     * Parses the all matches from a player and gives seperate Json Strings
     */
    public static String[] parsePlayerMatches(String jsonString) throws JSONException {
        String playerMatchData[];

        JSONArray matchesArray = new JSONArray(jsonString);

        int matchesCount = matchesArray.length();
        playerMatchData = new String[matchesCount];

        for (int i = 0; i<matchesCount; i++){
            JSONObject object = matchesArray.getJSONObject(i);
            playerMatchData[i] = object.toString();
        }
        return playerMatchData;

    }

    public static void parsePlayerInfo(String jsonString, Context context) throws JSONException{
        JSONObject object = new JSONObject(jsonString);
        SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        String userID = object.getJSONObject("profile").getString("account_id");
        String userName = object.getJSONObject("profile").getString("personaname");
        String userPic = object.getJSONObject("profile").getString("avatarfull");
        String soloMMR = object.getString("solo_competitive_rank");
        String estMMR = object.getJSONObject("mmr_estimate").getString("estimate");


        editor.putString(context.getString(R.string.pref_user_id_key),userID);
        editor.putString(context.getString(R.string.pref_user_name_key),userName);
        editor.putString(context.getString(R.string.pref_user_pic_key),userPic);
        editor.putString(context.getString(R.string.pref_user_solo_mmr_key),soloMMR);
        editor.putString(context.getString(R.string.pref_user_est_mmr_key),estMMR);
        editor.apply();
    }

    public static void parseWinLoss(String jsonString, Context context) throws JSONException{
        JSONObject object = new JSONObject(jsonString);

        String wins = object.getString("win");
        String losses = object.getString("lose");

        SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(context.getString(R.string.pref_user_wins_key),wins);
        editor.putString(context.getString(R.string.pref_user_losses_key),losses);

        editor.apply();
    }


}
