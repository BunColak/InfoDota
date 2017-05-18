package com.buncolak.opendota;

import android.app.Application;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.buncolak.opendota.data.MatchesDBContract;
import com.buncolak.opendota.data.UserDBHelper;
import com.buncolak.opendota.utils.NetworkUtils;
import com.buncolak.opendota.utils.ODJsonParser;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,
        SharedPreferences.OnSharedPreferenceChangeListener,PlayerMatchAdapter.MatchClickListener{

    static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 322;
    public  SQLiteDatabase odDB;
    Cursor cursor = null;
    LoaderManager.LoaderCallbacks<String> callbacks;

    RecyclerView playerMatchesRV;
    PlayerMatchAdapter playerMatchAdapter;
    TextView userNameTV, userWinsTV, userLossesTV, userWinrateTV,userSoloMMRTV, userEstMMRTV;
    ImageView userPic;
    String userID;

    SwipeRefreshLayout mSwipeRefresher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application opendota = getApplication();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(opendota);

        setContentView(R.layout.activity_main);

        userNameTV = (TextView)findViewById(R.id.userNameTV);
        userWinsTV = (TextView)findViewById(R.id.wins_value);
        userLossesTV = (TextView)findViewById(R.id.losses_value);
        userWinrateTV = (TextView)findViewById(R.id.winrate_value);
        userSoloMMRTV = (TextView)findViewById(R.id.solo_mmr_value);
        userEstMMRTV = (TextView)findViewById(R.id.est_mmr_value);
        userPic = (ImageView)findViewById(R.id.userProfilePicture);
        mSwipeRefresher = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);

        Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        UserDBHelper helper = new UserDBHelper(this);
        odDB = helper.getWritableDatabase();

        cursor = getAllMatches();

        playerMatchAdapter = new PlayerMatchAdapter(this,cursor,this);
        playerMatchesRV = (RecyclerView)findViewById(R.id.playerMatchesRV);
        playerMatchesRV.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        playerMatchesRV.setLayoutManager(linearLayoutManager);
        playerMatchesRV.setAdapter(playerMatchAdapter);


        final Bundle bundle = null;
        callbacks = MainActivity.this;
        getSupportLoaderManager().initLoader(LOADER_ID,bundle,callbacks);

        mSwipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userID = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).
                        getString(getString(R.string.pref_user_id_key),getString(R.string.pref_user_id_def));
                getSupportLoaderManager().restartLoader(LOADER_ID,bundle,callbacks);
                loadPlayerInfo();
            }
        });

        loadPlayerInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            String playerData = null;

            @Override
            protected void onStartLoading() {
                if (playerData != null){
                    deliverResult(playerData);
                }else
                    forceLoad();
            }

            @Override
            public String loadInBackground() {
                try {
                    //For the implementation I am using my player ID
                    userID = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).
                            getString(getString(R.string.pref_user_id_key),getString(R.string.pref_user_id_def));
                    URL playerInfoURL = NetworkUtils.buildUrlPlayerInfo(userID);
                    URL playerMatchesURL = NetworkUtils.builUrlPlayerMatches(userID);
                    URL playerWL = NetworkUtils.buildUrlPlayerWL(userID);

                    String userData = NetworkUtils.getResponseFromHttpUrl(playerInfoURL);
                    String userWL = NetworkUtils.getResponseFromHttpUrl(playerWL);

                    ODJsonParser.parsePlayerInfo(userData,MainActivity.this);
                    ODJsonParser.parseWinLoss(userWL,MainActivity.this);

                    playerData = NetworkUtils.getResponseFromHttpUrl(playerMatchesURL);
                    return playerData;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String data) {
                playerData = data;
                super.deliverResult(data);
            }
        };
    }
    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        try {
            loadPlayerInfo();
            String[] mData = ODJsonParser.parsePlayerMatches(data);
            AddToDatabase(mData);
            cursor = getAllMatches();
            playerMatchAdapter.swapData(cursor);
            mSwipeRefresher.setRefreshing(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    private void AddToDatabase(String[] data) throws JSONException {
        odDB.delete(MatchesDBContract.AllMatchesEntry.TABLE_NAME,null,null);
        int count = data.length;
        for (int i = 0; i<count; i++){
            JSONObject object = new JSONObject(data[i]);
            ContentValues cv = new ContentValues();
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_MATCH_ID,object.getString("match_id"));
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_RADIANT_WIN,object.getString("radiant_win"));
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_PLAYER_SLOT,object.getString("player_slot"));
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_DURATION,object.getString("duration"));
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_GAME_MODE,object.getString("game_mode"));
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_HERO_ID,object.getString("hero_id"));
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_START_TIME,object.getString("start_time"));
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_KILLS,object.getString("kills"));
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_DEATHS,object.getString("deaths"));
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_ASSISTS,object.getString("assists"));
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_SKILL,object.getString("skill"));
            odDB.insert(MatchesDBContract.AllMatchesEntry.TABLE_NAME,null,cv);
            cv.clear();
        }
        Log.d(TAG,"YAY!");
    }
    private Cursor getAllMatches(){
        Cursor cursor = odDB.query(MatchesDBContract.AllMatchesEntry.TABLE_NAME,
                null,null,null,null,null, MatchesDBContract.AllMatchesEntry._ID);
        return cursor;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        getSupportLoaderManager().restartLoader(LOADER_ID,null,callbacks);
        loadPlayerInfo();
    }

    private void loadPlayerInfo(){
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        double winRate = 0;
        String wins = p.getString(getString(R.string.pref_user_wins_key),getString(R.string.pref_user_zero_def));
        String losses = p.getString(getString(R.string.pref_user_losses_key),getString(R.string.pref_user_zero_def));

        if (Integer.valueOf(wins)+Integer.valueOf(losses) != 0)
            winRate = 100*(Double)(Double.valueOf(wins)/(Double.valueOf(wins)+Double.valueOf(losses)));

        String userName = p.getString(getString(R.string.pref_user_name_key),getString(R.string.pref_user_zero_def));
        String soloMMR = p.getString(getString(R.string.pref_user_solo_mmr_key),getString(R.string.pref_user_zero_def));
        String estMMR = p.getString(getString(R.string.pref_user_est_mmr_key),getString(R.string.pref_user_zero_def));
        String profPic = p.getString(getString(R.string.pref_user_pic_key),getString(R.string.pref_user_zero_def));

        userNameTV.setText(userName);
        userWinsTV.setText(wins);
        userLossesTV.setText(losses);
        userSoloMMRTV.setText(soloMMR);
        userEstMMRTV.setText(estMMR);
        userWinrateTV.setText(String.format("%.1f",winRate)+"%");

        Picasso.with(MainActivity.this).load(profPic).into(userPic);

        p.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_user){
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }else if (itemId == R.id.menu_refresh){
            mSwipeRefresher.setRefreshing(true);
            userID = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).
                    getString(getString(R.string.pref_user_id_key),getString(R.string.pref_user_id_def));
            getSupportLoaderManager().restartLoader(LOADER_ID,null,callbacks);
            loadPlayerInfo();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMatchClick(long matchId) {
        Intent intent = new Intent(this,MatchDetailsActivity.class);
        intent.putExtra(getString(R.string.intent_extra_match_id),matchId);
        startActivity(intent);
    }
}
