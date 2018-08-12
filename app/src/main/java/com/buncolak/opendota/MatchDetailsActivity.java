package com.buncolak.opendota;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.buncolak.opendota.tab_fragments.ChatFragment;
import com.buncolak.opendota.tab_fragments.CombatFragment;
import com.buncolak.opendota.tab_fragments.FarmFragment;
import com.buncolak.opendota.tab_fragments.GraphsFragment;
import com.buncolak.opendota.tab_fragments.OverviewFragment;
import com.buncolak.opendota.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MatchDetailsActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    public static long matchId;
    public static String matchJson;
    @BindView(R.id.pb_match_details) ProgressBar pb_match_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.intent_extra_match_id))) {
            matchId = intent.getLongExtra((getString(R.string.intent_extra_match_id)), 0);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new GetMatchDetails().execute();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_match_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return OverviewFragment.newInstance(matchId);
                case 1:
                    return new CombatFragment();
                case 2:
                    return new FarmFragment();
                case 3:
                    return new GraphsFragment();
                case 4:
                    return new ChatFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Overview";
                case 1:
                    return "Combat";
                case 2:
                    return "Farm";
                case 3:
                    return "Graphs";
                case 4:
                    return "Chat";
            }
            return null;
        }
    }

    class GetMatchDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb_match_details.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            URL matchDetailUrl = NetworkUtils.buildUrlMatchDetails(String.valueOf(matchId));
            try {
                matchJson = NetworkUtils.getResponseFromHttpUrl(matchDetailUrl);
                return matchJson;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            pb_match_details.setVisibility(View.INVISIBLE);
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }
}
