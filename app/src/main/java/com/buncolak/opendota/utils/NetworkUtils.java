package com.buncolak.opendota.utils;

import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * This is class to handle api connections
 */

public class NetworkUtils {

    //For debug purposes TAG variable
    private static final String TAG = NetworkUtils.class.getSimpleName();

    //API strings for OpenDota
    private static final String BASE_API_URL = "https://api.opendota.com/api";
    private static final String PATH_PLAYER = "players";
    private static final String PATH_MATCHES = "matches";
    private static final String PATH_WIN_LOSS = "wl";


    /*
     * Builds API URL for the given playerID, Steam32
     */
    public static URL builUrlPlayerMatches(String playerID) throws MalformedURLException {
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendPath(PATH_PLAYER)
                .appendPath(playerID)
                .appendPath(PATH_MATCHES).appendQueryParameter("limit","20").build();
        try{
            URL url = new URL(uri.toString());
            Log.d(TAG,"URL success:" +url.toString());
            return url;
        }catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }

    }

    public static URL buildUrlPlayerInfo(String playerID) throws MalformedURLException{
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendPath(PATH_PLAYER)
                .appendPath(playerID)
                .build();
        try{
            URL url = new URL(uri.toString());
            Log.d(TAG,"URL success:" +url.toString());
            return url;
        }catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static URL buildUrlPlayerWL(String playerID) throws MalformedURLException{
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendPath(PATH_PLAYER)
                .appendPath(playerID)
                .appendPath(PATH_WIN_LOSS)
                .build();
        try{
            URL url = new URL(uri.toString());
            Log.d(TAG,"URL success:" +url.toString());
            return url;
        }catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static URL buildUrlMatchDetails(String matchID) {
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendPath(PATH_MATCHES)
                .appendPath(matchID)
                .build();
        try{
            URL url = new URL(uri.toString());
            Log.d(TAG,"URL success:" +url.toString());
            return url;
        }catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }

    /*
     * This method gets the data from the given URL and returns the JSON data in String form
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

}
