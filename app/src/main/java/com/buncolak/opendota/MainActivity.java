package com.buncolak.opendota;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.Toast;

import com.buncolak.opendota.data.MatchesDBContract;
import com.buncolak.opendota.data.UserDBHelper;
import com.buncolak.opendota.utils.NetworkUtils;
import com.buncolak.opendota.utils.ODJsonParser;
import com.squareup.picasso.Picasso;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,
        SharedPreferences.OnSharedPreferenceChangeListener, PlayerMatchAdapter.MatchClickListener {

    static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 322;
    public SQLiteDatabase odDB;
    Cursor cursor = null;
    LoaderManager.LoaderCallbacks<String> callbacks;

    @BindView(R.id.playerMatchesRV)
    RecyclerView playerMatchesRV;
    PlayerMatchAdapter playerMatchAdapter;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.tv_wins_value)
    TextView tv_wins;
    @BindView(R.id.tv_losses_value)
    TextView tv_losses;
    @BindView(R.id.tv_winrate_value)
    TextView tv_winrate;
    @BindView(R.id.img_user_profile)
    ImageView img_user_profile;
    @BindView(R.id.srl_main)
    SwipeRefreshLayout mSwipeRefresher;
    @BindView(R.id.my_toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView nav_view;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;
    TextView tv_user_name_nav;
    CircleImageView civ_user_image;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (PreferenceManager.getDefaultSharedPreferences(MainActivity.this).
                getString(getString(R.string.pref_user_id_key), "0").equals("0")) {
            startActivityForResult(new Intent(this, LoginActivity.class), 1);
        }
        tv_user_name_nav = nav_view.getHeaderView(0).findViewById(R.id.tv_user_name_nav);
        civ_user_image = nav_view.getHeaderView(0).findViewById(R.id.civ_user_image);

        setSupportActionBar(toolbar);
        toolbar.setTitle("InfoDota");

        UserDBHelper helper = new UserDBHelper(this);
        odDB = helper.getWritableDatabase();

        cursor = getAllMatches();

        playerMatchAdapter = new PlayerMatchAdapter(this, cursor, this);
        playerMatchesRV.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        playerMatchesRV.setLayoutManager(linearLayoutManager);
        playerMatchesRV.setAdapter(playerMatchAdapter);

        final Bundle bundle = null;
        callbacks = MainActivity.this;
        getSupportLoaderManager().initLoader(LOADER_ID, bundle, callbacks);


        mSwipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userID = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).
                        getString(getString(R.string.pref_user_id_key), "0");
                getSupportLoaderManager().restartLoader(LOADER_ID, bundle, callbacks);
                loadPlayerInfo();
            }
        });

        nav_view.bringToFront();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int selectId = item.getItemId();
                switch (selectId) {
                    case R.id.action_logout:
                        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor editor = p.edit();
                        editor.putString("user_id", "");
                        editor.apply();
                        Toast.makeText(MainActivity.this, "Logging out..", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.action_settings:
                        Toast.makeText(MainActivity.this, "There are no settings this is just here like this. Don't waste your time", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });

        loadPlayerInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String userId = data.getStringExtra(Intent.EXTRA_TEXT);
            SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            SharedPreferences.Editor editor = p.edit();
            editor.putString("user_id", userId);
            editor.apply();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            String playerData = null;

            @Override
            protected void onStartLoading() {
                if (playerData != null) {
                    deliverResult(playerData);
                } else
                    forceLoad();
            }

            @Override
            public String loadInBackground() {
                try {
                    //For the implementation I am using my player ID
                    userID = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).
                            getString(getString(R.string.pref_user_id_key), "0");
                    if (Long.parseLong(userID) > 76561197960265728L) {
                        userID = String.valueOf(Long.parseLong(userID) - 76561197960265728L);
                    }
                    URL playerInfoURL = NetworkUtils.buildUrlPlayerInfo(userID);
                    URL playerMatchesURL = NetworkUtils.builUrlPlayerMatches(userID);
                    URL playerWL = NetworkUtils.buildUrlPlayerWL(userID);

                    String userData = NetworkUtils.getResponseFromHttpUrl(playerInfoURL);
                    String userWL = NetworkUtils.getResponseFromHttpUrl(playerWL);

                    ODJsonParser.parsePlayerInfo(userData, MainActivity.this);
                    ODJsonParser.parseWinLoss(userWL, MainActivity.this);

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
        loadPlayerInfo();
        String[] mData = null;
        try {
            mData = ODJsonParser.parsePlayerMatches(data);
            AddToDatabase(mData);
            cursor = getAllMatches();
            playerMatchAdapter.swapData(cursor);
            mSwipeRefresher.setRefreshing(false);
        } catch (Exception e) {
            Toast.makeText(this,"SteamID is wrong, please check again.",Toast.LENGTH_LONG).show();
            startActivityForResult(new Intent(this, LoginActivity.class), 1);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    private void AddToDatabase(String[] data) throws JSONException {
        odDB.delete(MatchesDBContract.AllMatchesEntry.TABLE_NAME, null, null);
        int count = data.length;
        for (int i = 0; i < count; i++) {
            JSONObject object = new JSONObject(data[i]);
            ContentValues cv = new ContentValues();
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_MATCH_ID, object.getString("match_id"));
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_RADIANT_WIN, object.getString("radiant_win"));
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_PLAYER_SLOT, object.getString("player_slot"));
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_DURATION, object.getString("duration"));
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_GAME_MODE, object.getString("game_mode"));
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_HERO_ID, object.getString("hero_id"));
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_START_TIME, object.getString("start_time"));
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_KILLS, object.getString("kills"));
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_DEATHS, object.getString("deaths"));
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_ASSISTS, object.getString("assists"));
            cv.put(MatchesDBContract.AllMatchesEntry.COLUMN_SKILL, object.getString("skill"));
            odDB.insert(MatchesDBContract.AllMatchesEntry.TABLE_NAME, null, cv);
            cv.clear();
        }
    }

    private Cursor getAllMatches() {
        Cursor cursor = odDB.query(MatchesDBContract.AllMatchesEntry.TABLE_NAME,
                null, null, null, null, null, MatchesDBContract.AllMatchesEntry._ID);
        return cursor;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        getSupportLoaderManager().restartLoader(LOADER_ID, null, callbacks);
        loadPlayerInfo();
    }

    private void loadPlayerInfo() {
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        double winRate = 0;
        String wins = p.getString(getString(R.string.pref_user_wins_key), getString(R.string.pref_user_zero_def));
        String losses = p.getString(getString(R.string.pref_user_losses_key), getString(R.string.pref_user_zero_def));

        if (Integer.valueOf(wins) + Integer.valueOf(losses) != 0)
            winRate = 100 * (Double) (Double.valueOf(wins) / (Double.valueOf(wins) + Double.valueOf(losses)));

        String userName = p.getString(getString(R.string.pref_user_name_key), getString(R.string.pref_user_zero_def));
        String profPic = p.getString(getString(R.string.pref_user_pic_key), getString(R.string.pref_user_zero_def));

        tv_user_name.setText(userName);
        tv_user_name_nav.setText(userName);
        tv_wins.setText(wins);
        tv_losses.setText(losses);
        tv_winrate.setText(String.format("%.1f", winRate) + "%");

        Picasso.with(MainActivity.this).load(profPic).into(img_user_profile);
        Picasso.with(MainActivity.this).load(profPic).into(civ_user_image);
        p.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onMatchClick(long matchId) {
        Intent intent = new Intent(this, MatchDetailsActivity.class);
        intent.putExtra(getString(R.string.intent_extra_match_id), matchId);
        startActivity(intent);
    }
}
