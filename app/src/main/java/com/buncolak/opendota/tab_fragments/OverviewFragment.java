package com.buncolak.opendota.tab_fragments;

import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buncolak.opendota.MatchDetailsActivity;
import com.buncolak.opendota.R;
import com.buncolak.opendota.data.HeroValues;
import com.buncolak.opendota.data.ItemValues;
import com.buncolak.opendota.utils.NetworkUtils;
import com.buncolak.opendota.utils.ODJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bunya on 11-May-17.
 */

public class OverviewFragment extends Fragment {
    private static final String MATCH_ID = "match_id";
    JSONObject[] playerJsons = new JSONObject[10];
    TextView[] tv_player_names, tv_player_mmrs, tv_player_lvls, tv_player_kills, tv_player_deaths, tv_player_assists, tv_player_gpms, tv_player_xpms, tv_player_lhs, tv_player_dns, tv_player_hds, tv_player_hhs, tv_player_tds, tv_player_golds;
    ImageView[] img_player_heroes;
    ImageView[][] img_player_items, img_player_backpacks;
    TextView tv_radiant_score, tv_dire_score, tv_win_status, tv_duration;
    ProgressBar pb_match_details;
    ConstraintLayout cl_match_details;

    public OverviewFragment() {
    }

    public static OverviewFragment newInstance(long matchid) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putLong(MATCH_ID, matchid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_match_overview, container, false);
        getActivity().setTitle(getResources().getString(R.string.title_activity_match_details) + ": " + MatchDetailsActivity.matchId);
        initializeViews(rootView);
        setDetails(MatchDetailsActivity.matchJson);
        return rootView;
    }

    private void initializeViews(View rootView) {
        tv_dire_score = rootView.findViewById(R.id.tv_match_details_dire_score);
        tv_radiant_score = rootView.findViewById(R.id.tv_match_details_radiant_score);
        tv_duration = rootView.findViewById(R.id.tv_match_details_duration);
        tv_win_status = rootView.findViewById(R.id.tv_match_win_side);
        pb_match_details = rootView.findViewById(R.id.pb_match_details);
        cl_match_details = rootView.findViewById(R.id.cl_match_details);

        tv_player_names = new TextView[10];
        tv_player_mmrs = new TextView[10];
        tv_player_lvls = new TextView[10];
        tv_player_kills = new TextView[10];
        tv_player_deaths = new TextView[10];
        tv_player_assists = new TextView[10];
        tv_player_gpms = new TextView[10];
        tv_player_xpms = new TextView[10];
        tv_player_lhs = new TextView[10];
        tv_player_dns = new TextView[10];
        tv_player_hds = new TextView[10];
        tv_player_hhs = new TextView[10];
        tv_player_tds = new TextView[10];
        tv_player_golds = new TextView[10];
        img_player_heroes = new ImageView[10];
        img_player_items = new ImageView[10][6];
        img_player_backpacks = new ImageView[10][3];

        tv_player_names[0] = rootView.findViewById(R.id.tv_match_details_player_1_name);
        tv_player_mmrs[0] = rootView.findViewById(R.id.tv_match_details_player_1_mmr);
        tv_player_lvls[0] = rootView.findViewById(R.id.tv_match_details_player_1_lvl);
        tv_player_kills[0] = rootView.findViewById(R.id.tv_match_details_player_1_kill);
        tv_player_deaths[0] = rootView.findViewById(R.id.tv_match_details_player_1_death);
        tv_player_assists[0] = rootView.findViewById(R.id.tv_match_details_player_1_assist);
        tv_player_gpms[0] = rootView.findViewById(R.id.tv_match_details_player_1_GPM);
        tv_player_xpms[0] = rootView.findViewById(R.id.tv_match_details_player_1_XPM);
        tv_player_lhs[0] = rootView.findViewById(R.id.tv_match_details_player_1_LH);
        tv_player_dns[0] = rootView.findViewById(R.id.tv_match_details_player_1_DN);
        tv_player_hds[0] = rootView.findViewById(R.id.tv_match_details_player_1_HD);
        tv_player_hhs[0] = rootView.findViewById(R.id.tv_match_details_player_1_HH);
        tv_player_tds[0] = rootView.findViewById(R.id.tv_match_details_player_1_TD);
        tv_player_golds[0] = rootView.findViewById(R.id.tv_match_details_player_1_G);
        img_player_heroes[0] = rootView.findViewById(R.id.img_match_details_player_1_hero);

        tv_player_names[1] = rootView.findViewById(R.id.tv_match_details_player_2_name);
        tv_player_mmrs[1] = rootView.findViewById(R.id.tv_match_details_player_2_mmr);
        tv_player_lvls[1] = rootView.findViewById(R.id.tv_match_details_player_2_lvl);
        tv_player_kills[1] = rootView.findViewById(R.id.tv_match_details_player_2_kill);
        tv_player_deaths[1] = rootView.findViewById(R.id.tv_match_details_player_2_death);
        tv_player_assists[1] = rootView.findViewById(R.id.tv_match_details_player_2_assist);
        tv_player_gpms[1] = rootView.findViewById(R.id.tv_match_details_player_2_GPM);
        tv_player_xpms[1] = rootView.findViewById(R.id.tv_match_details_player_2_XPM);
        tv_player_lhs[1] = rootView.findViewById(R.id.tv_match_details_player_2_LH);
        tv_player_dns[1] = rootView.findViewById(R.id.tv_match_details_player_2_DN);
        tv_player_hds[1] = rootView.findViewById(R.id.tv_match_details_player_2_HD);
        tv_player_hhs[1] = rootView.findViewById(R.id.tv_match_details_player_2_HH);
        tv_player_tds[1] = rootView.findViewById(R.id.tv_match_details_player_2_TD);
        tv_player_golds[1] = rootView.findViewById(R.id.tv_match_details_player_2_G);
        img_player_heroes[1] = rootView.findViewById(R.id.img_match_details_player_2_hero);

        tv_player_names[2] = rootView.findViewById(R.id.tv_match_details_player_3_name);
        tv_player_mmrs[2] = rootView.findViewById(R.id.tv_match_details_player_3_mmr);
        tv_player_lvls[2] = rootView.findViewById(R.id.tv_match_details_player_3_lvl);
        tv_player_kills[2] = rootView.findViewById(R.id.tv_match_details_player_3_kill);
        tv_player_deaths[2] = rootView.findViewById(R.id.tv_match_details_player_3_death);
        tv_player_assists[2] = rootView.findViewById(R.id.tv_match_details_player_3_assist);
        tv_player_gpms[2] = rootView.findViewById(R.id.tv_match_details_player_3_GPM);
        tv_player_xpms[2] = rootView.findViewById(R.id.tv_match_details_player_3_XPM);
        tv_player_lhs[2] = rootView.findViewById(R.id.tv_match_details_player_3_LH);
        tv_player_dns[2] = rootView.findViewById(R.id.tv_match_details_player_3_DN);
        tv_player_hds[2] = rootView.findViewById(R.id.tv_match_details_player_3_HD);
        tv_player_hhs[2] = rootView.findViewById(R.id.tv_match_details_player_3_HH);
        tv_player_tds[2] = rootView.findViewById(R.id.tv_match_details_player_3_TD);
        tv_player_golds[2] = rootView.findViewById(R.id.tv_match_details_player_3_G);
        img_player_heroes[2] = rootView.findViewById(R.id.img_match_details_player_3_hero);

        tv_player_names[3] = rootView.findViewById(R.id.tv_match_details_player_4_name);
        tv_player_mmrs[3] = rootView.findViewById(R.id.tv_match_details_player_4_mmr);
        tv_player_lvls[3] = rootView.findViewById(R.id.tv_match_details_player_4_lvl);
        tv_player_kills[3] = rootView.findViewById(R.id.tv_match_details_player_4_kill);
        tv_player_deaths[3] = rootView.findViewById(R.id.tv_match_details_player_4_death);
        tv_player_assists[3] = rootView.findViewById(R.id.tv_match_details_player_4_assist);
        tv_player_gpms[3] = rootView.findViewById(R.id.tv_match_details_player_4_GPM);
        tv_player_xpms[3] = rootView.findViewById(R.id.tv_match_details_player_4_XPM);
        tv_player_lhs[3] = rootView.findViewById(R.id.tv_match_details_player_4_LH);
        tv_player_dns[3] = rootView.findViewById(R.id.tv_match_details_player_4_DN);
        tv_player_hds[3] = rootView.findViewById(R.id.tv_match_details_player_4_HD);
        tv_player_hhs[3] = rootView.findViewById(R.id.tv_match_details_player_4_HH);
        tv_player_tds[3] = rootView.findViewById(R.id.tv_match_details_player_4_TD);
        tv_player_golds[3] = rootView.findViewById(R.id.tv_match_details_player_4_G);
        img_player_heroes[3] = rootView.findViewById(R.id.img_match_details_player_4_hero);

        tv_player_names[4] = rootView.findViewById(R.id.tv_match_details_player_5_name);
        tv_player_mmrs[4] = rootView.findViewById(R.id.tv_match_details_player_5_mmr);
        tv_player_lvls[4] = rootView.findViewById(R.id.tv_match_details_player_5_lvl);
        tv_player_kills[4] = rootView.findViewById(R.id.tv_match_details_player_5_kill);
        tv_player_deaths[4] = rootView.findViewById(R.id.tv_match_details_player_5_death);
        tv_player_assists[4] = rootView.findViewById(R.id.tv_match_details_player_5_assist);
        tv_player_gpms[4] = rootView.findViewById(R.id.tv_match_details_player_5_GPM);
        tv_player_xpms[4] = rootView.findViewById(R.id.tv_match_details_player_5_XPM);
        tv_player_lhs[4] = rootView.findViewById(R.id.tv_match_details_player_5_LH);
        tv_player_dns[4] = rootView.findViewById(R.id.tv_match_details_player_5_DN);
        tv_player_hds[4] = rootView.findViewById(R.id.tv_match_details_player_5_HD);
        tv_player_hhs[4] = rootView.findViewById(R.id.tv_match_details_player_5_HH);
        tv_player_tds[4] = rootView.findViewById(R.id.tv_match_details_player_5_TD);
        tv_player_golds[4] = rootView.findViewById(R.id.tv_match_details_player_5_G);
        img_player_heroes[4] = rootView.findViewById(R.id.img_match_details_player_5_hero);

        tv_player_names[5] = rootView.findViewById(R.id.tv_match_details_player_6_name);
        tv_player_mmrs[5] = rootView.findViewById(R.id.tv_match_details_player_6_mmr);
        tv_player_lvls[5] = rootView.findViewById(R.id.tv_match_details_player_6_lvl);
        tv_player_kills[5] = rootView.findViewById(R.id.tv_match_details_player_6_kill);
        tv_player_deaths[5] = rootView.findViewById(R.id.tv_match_details_player_6_death);
        tv_player_assists[5] = rootView.findViewById(R.id.tv_match_details_player_6_assist);
        tv_player_gpms[5] = rootView.findViewById(R.id.tv_match_details_player_6_GPM);
        tv_player_xpms[5] = rootView.findViewById(R.id.tv_match_details_player_6_XPM);
        tv_player_lhs[5] = rootView.findViewById(R.id.tv_match_details_player_6_LH);
        tv_player_dns[5] = rootView.findViewById(R.id.tv_match_details_player_6_DN);
        tv_player_hds[5] = rootView.findViewById(R.id.tv_match_details_player_6_HD);
        tv_player_hhs[5] = rootView.findViewById(R.id.tv_match_details_player_6_HH);
        tv_player_tds[5] = rootView.findViewById(R.id.tv_match_details_player_6_TD);
        tv_player_golds[5] = rootView.findViewById(R.id.tv_match_details_player_6_G);
        img_player_heroes[5] = rootView.findViewById(R.id.img_match_details_player_6_hero);

        tv_player_names[6] = rootView.findViewById(R.id.tv_match_details_player_7_name);
        tv_player_mmrs[6] = rootView.findViewById(R.id.tv_match_details_player_7_mmr);
        tv_player_lvls[6] = rootView.findViewById(R.id.tv_match_details_player_7_lvl);
        tv_player_kills[6] = rootView.findViewById(R.id.tv_match_details_player_7_kill);
        tv_player_deaths[6] = rootView.findViewById(R.id.tv_match_details_player_7_death);
        tv_player_assists[6] = rootView.findViewById(R.id.tv_match_details_player_7_assist);
        tv_player_gpms[6] = rootView.findViewById(R.id.tv_match_details_player_7_GPM);
        tv_player_xpms[6] = rootView.findViewById(R.id.tv_match_details_player_7_XPM);
        tv_player_lhs[6] = rootView.findViewById(R.id.tv_match_details_player_7_LH);
        tv_player_dns[6] = rootView.findViewById(R.id.tv_match_details_player_7_DN);
        tv_player_hds[6] = rootView.findViewById(R.id.tv_match_details_player_7_HD);
        tv_player_hhs[6] = rootView.findViewById(R.id.tv_match_details_player_7_HH);
        tv_player_tds[6] = rootView.findViewById(R.id.tv_match_details_player_7_TD);
        tv_player_golds[6] = rootView.findViewById(R.id.tv_match_details_player_7_G);
        img_player_heroes[6] = rootView.findViewById(R.id.img_match_details_player_7_hero);

        tv_player_names[7] = rootView.findViewById(R.id.tv_match_details_player_8_name);
        tv_player_mmrs[7] = rootView.findViewById(R.id.tv_match_details_player_8_mmr);
        tv_player_lvls[7] = rootView.findViewById(R.id.tv_match_details_player_8_lvl);
        tv_player_kills[7] = rootView.findViewById(R.id.tv_match_details_player_8_kill);
        tv_player_deaths[7] = rootView.findViewById(R.id.tv_match_details_player_8_death);
        tv_player_assists[7] = rootView.findViewById(R.id.tv_match_details_player_8_assist);
        tv_player_gpms[7] = rootView.findViewById(R.id.tv_match_details_player_8_GPM);
        tv_player_xpms[7] = rootView.findViewById(R.id.tv_match_details_player_8_XPM);
        tv_player_lhs[7] = rootView.findViewById(R.id.tv_match_details_player_8_LH);
        tv_player_dns[7] = rootView.findViewById(R.id.tv_match_details_player_8_DN);
        tv_player_hds[7] = rootView.findViewById(R.id.tv_match_details_player_8_HD);
        tv_player_hhs[7] = rootView.findViewById(R.id.tv_match_details_player_8_HH);
        tv_player_tds[7] = rootView.findViewById(R.id.tv_match_details_player_8_TD);
        tv_player_golds[7] = rootView.findViewById(R.id.tv_match_details_player_8_G);
        img_player_heroes[7] = rootView.findViewById(R.id.img_match_details_player_8_hero);

        tv_player_names[8] = rootView.findViewById(R.id.tv_match_details_player_9_name);
        tv_player_mmrs[8] = rootView.findViewById(R.id.tv_match_details_player_9_mmr);
        tv_player_lvls[8] = rootView.findViewById(R.id.tv_match_details_player_9_lvl);
        tv_player_kills[8] = rootView.findViewById(R.id.tv_match_details_player_9_kill);
        tv_player_deaths[8] = rootView.findViewById(R.id.tv_match_details_player_9_death);
        tv_player_assists[8] = rootView.findViewById(R.id.tv_match_details_player_9_assist);
        tv_player_gpms[8] = rootView.findViewById(R.id.tv_match_details_player_9_GPM);
        tv_player_xpms[8] = rootView.findViewById(R.id.tv_match_details_player_9_XPM);
        tv_player_lhs[8] = rootView.findViewById(R.id.tv_match_details_player_9_LH);
        tv_player_dns[8] = rootView.findViewById(R.id.tv_match_details_player_9_DN);
        tv_player_hds[8] = rootView.findViewById(R.id.tv_match_details_player_9_HD);
        tv_player_hhs[8] = rootView.findViewById(R.id.tv_match_details_player_9_HH);
        tv_player_tds[8] = rootView.findViewById(R.id.tv_match_details_player_9_TD);
        tv_player_golds[8] = rootView.findViewById(R.id.tv_match_details_player_9_G);
        img_player_heroes[8] = rootView.findViewById(R.id.img_match_details_player_9_hero);

        tv_player_names[9] = rootView.findViewById(R.id.tv_match_details_player_10_name);
        tv_player_mmrs[9] = rootView.findViewById(R.id.tv_match_details_player_10_mmr);
        tv_player_lvls[9] = rootView.findViewById(R.id.tv_match_details_player_10_lvl);
        tv_player_kills[9] = rootView.findViewById(R.id.tv_match_details_player_10_kill);
        tv_player_deaths[9] = rootView.findViewById(R.id.tv_match_details_player_10_death);
        tv_player_assists[9] = rootView.findViewById(R.id.tv_match_details_player_10_assist);
        tv_player_gpms[9] = rootView.findViewById(R.id.tv_match_details_player_10_GPM);
        tv_player_xpms[9] = rootView.findViewById(R.id.tv_match_details_player_10_XPM);
        tv_player_lhs[9] = rootView.findViewById(R.id.tv_match_details_player_10_LH);
        tv_player_dns[9] = rootView.findViewById(R.id.tv_match_details_player_10_DN);
        tv_player_hds[9] = rootView.findViewById(R.id.tv_match_details_player_10_HD);
        tv_player_hhs[9] = rootView.findViewById(R.id.tv_match_details_player_10_HH);
        tv_player_tds[9] = rootView.findViewById(R.id.tv_match_details_player_10_TD);
        tv_player_golds[9] = rootView.findViewById(R.id.tv_match_details_player_10_G);
        img_player_heroes[9] = rootView.findViewById(R.id.img_match_details_player_10_hero);

        img_player_items[0][0] = rootView.findViewById(R.id.img_match_details_player_1_item_1);
        img_player_items[0][1] = rootView.findViewById(R.id.img_match_details_player_1_item_2);
        img_player_items[0][2] = rootView.findViewById(R.id.img_match_details_player_1_item_3);
        img_player_items[0][3] = rootView.findViewById(R.id.img_match_details_player_1_item_4);
        img_player_items[0][4] = rootView.findViewById(R.id.img_match_details_player_1_item_5);
        img_player_items[0][5] = rootView.findViewById(R.id.img_match_details_player_1_item_6);

        img_player_items[1][0] = rootView.findViewById(R.id.img_match_details_player_2_item_1);
        img_player_items[1][1] = rootView.findViewById(R.id.img_match_details_player_2_item_2);
        img_player_items[1][2] = rootView.findViewById(R.id.img_match_details_player_2_item_3);
        img_player_items[1][3] = rootView.findViewById(R.id.img_match_details_player_2_item_4);
        img_player_items[1][4] = rootView.findViewById(R.id.img_match_details_player_2_item_5);
        img_player_items[1][5] = rootView.findViewById(R.id.img_match_details_player_2_item_6);

        img_player_items[2][0] = rootView.findViewById(R.id.img_match_details_player_3_item_1);
        img_player_items[2][1] = rootView.findViewById(R.id.img_match_details_player_3_item_2);
        img_player_items[2][2] = rootView.findViewById(R.id.img_match_details_player_3_item_3);
        img_player_items[2][3] = rootView.findViewById(R.id.img_match_details_player_3_item_4);
        img_player_items[2][4] = rootView.findViewById(R.id.img_match_details_player_3_item_5);
        img_player_items[2][5] = rootView.findViewById(R.id.img_match_details_player_3_item_6);

        img_player_items[3][0] = rootView.findViewById(R.id.img_match_details_player_4_item_1);
        img_player_items[3][1] = rootView.findViewById(R.id.img_match_details_player_4_item_2);
        img_player_items[3][2] = rootView.findViewById(R.id.img_match_details_player_4_item_3);
        img_player_items[3][3] = rootView.findViewById(R.id.img_match_details_player_4_item_4);
        img_player_items[3][4] = rootView.findViewById(R.id.img_match_details_player_4_item_5);
        img_player_items[3][5] = rootView.findViewById(R.id.img_match_details_player_4_item_6);

        img_player_items[4][0] = rootView.findViewById(R.id.img_match_details_player_5_item_1);
        img_player_items[4][1] = rootView.findViewById(R.id.img_match_details_player_5_item_2);
        img_player_items[4][2] = rootView.findViewById(R.id.img_match_details_player_5_item_3);
        img_player_items[4][3] = rootView.findViewById(R.id.img_match_details_player_5_item_4);
        img_player_items[4][4] = rootView.findViewById(R.id.img_match_details_player_5_item_5);
        img_player_items[4][5] = rootView.findViewById(R.id.img_match_details_player_5_item_6);

        img_player_items[5][0] = rootView.findViewById(R.id.img_match_details_player_6_item_1);
        img_player_items[5][1] = rootView.findViewById(R.id.img_match_details_player_6_item_2);
        img_player_items[5][2] = rootView.findViewById(R.id.img_match_details_player_6_item_3);
        img_player_items[5][3] = rootView.findViewById(R.id.img_match_details_player_6_item_4);
        img_player_items[5][4] = rootView.findViewById(R.id.img_match_details_player_6_item_5);
        img_player_items[5][5] = rootView.findViewById(R.id.img_match_details_player_6_item_6);

        img_player_items[6][0] = rootView.findViewById(R.id.img_match_details_player_7_item_1);
        img_player_items[6][1] = rootView.findViewById(R.id.img_match_details_player_7_item_2);
        img_player_items[6][2] = rootView.findViewById(R.id.img_match_details_player_7_item_3);
        img_player_items[6][3] = rootView.findViewById(R.id.img_match_details_player_7_item_4);
        img_player_items[6][4] = rootView.findViewById(R.id.img_match_details_player_7_item_5);
        img_player_items[6][5] = rootView.findViewById(R.id.img_match_details_player_7_item_6);

        img_player_items[7][0] = rootView.findViewById(R.id.img_match_details_player_8_item_1);
        img_player_items[7][1] = rootView.findViewById(R.id.img_match_details_player_8_item_2);
        img_player_items[7][2] = rootView.findViewById(R.id.img_match_details_player_8_item_3);
        img_player_items[7][3] = rootView.findViewById(R.id.img_match_details_player_8_item_4);
        img_player_items[7][4] = rootView.findViewById(R.id.img_match_details_player_8_item_5);
        img_player_items[7][5] = rootView.findViewById(R.id.img_match_details_player_8_item_6);

        img_player_items[8][0] = rootView.findViewById(R.id.img_match_details_player_9_item_1);
        img_player_items[8][1] = rootView.findViewById(R.id.img_match_details_player_9_item_2);
        img_player_items[8][2] = rootView.findViewById(R.id.img_match_details_player_9_item_3);
        img_player_items[8][3] = rootView.findViewById(R.id.img_match_details_player_9_item_4);
        img_player_items[8][4] = rootView.findViewById(R.id.img_match_details_player_9_item_5);
        img_player_items[8][5] = rootView.findViewById(R.id.img_match_details_player_9_item_6);

        img_player_items[9][0] = rootView.findViewById(R.id.img_match_details_player_10_item_1);
        img_player_items[9][1] = rootView.findViewById(R.id.img_match_details_player_10_item_2);
        img_player_items[9][2] = rootView.findViewById(R.id.img_match_details_player_10_item_3);
        img_player_items[9][3] = rootView.findViewById(R.id.img_match_details_player_10_item_4);
        img_player_items[9][4] = rootView.findViewById(R.id.img_match_details_player_10_item_5);
        img_player_items[9][5] = rootView.findViewById(R.id.img_match_details_player_10_item_6);

        img_player_backpacks[0][0] = rootView.findViewById(R.id.img_match_details_player_1_backpack_1);
        img_player_backpacks[0][1] = rootView.findViewById(R.id.img_match_details_player_1_backpack_2);
        img_player_backpacks[0][2] = rootView.findViewById(R.id.img_match_details_player_1_backpack_3);

        img_player_backpacks[1][0] = rootView.findViewById(R.id.img_match_details_player_2_backpack_1);
        img_player_backpacks[1][1] = rootView.findViewById(R.id.img_match_details_player_2_backpack_2);
        img_player_backpacks[1][2] = rootView.findViewById(R.id.img_match_details_player_2_backpack_3);

        img_player_backpacks[2][0] = rootView.findViewById(R.id.img_match_details_player_3_backpack_1);
        img_player_backpacks[2][1] = rootView.findViewById(R.id.img_match_details_player_3_backpack_2);
        img_player_backpacks[2][2] = rootView.findViewById(R.id.img_match_details_player_3_backpack_3);

        img_player_backpacks[3][0] = rootView.findViewById(R.id.img_match_details_player_4_backpack_1);
        img_player_backpacks[3][1] = rootView.findViewById(R.id.img_match_details_player_4_backpack_2);
        img_player_backpacks[3][2] = rootView.findViewById(R.id.img_match_details_player_4_backpack_3);

        img_player_backpacks[4][0] = rootView.findViewById(R.id.img_match_details_player_5_backpack_1);
        img_player_backpacks[4][1] = rootView.findViewById(R.id.img_match_details_player_5_backpack_2);
        img_player_backpacks[4][2] = rootView.findViewById(R.id.img_match_details_player_5_backpack_3);

        img_player_backpacks[5][0] = rootView.findViewById(R.id.img_match_details_player_6_backpack_1);
        img_player_backpacks[5][1] = rootView.findViewById(R.id.img_match_details_player_6_backpack_2);
        img_player_backpacks[5][2] = rootView.findViewById(R.id.img_match_details_player_6_backpack_3);

        img_player_backpacks[6][0] = rootView.findViewById(R.id.img_match_details_player_7_backpack_1);
        img_player_backpacks[6][1] = rootView.findViewById(R.id.img_match_details_player_7_backpack_2);
        img_player_backpacks[6][2] = rootView.findViewById(R.id.img_match_details_player_7_backpack_3);

        img_player_backpacks[7][0] = rootView.findViewById(R.id.img_match_details_player_8_backpack_1);
        img_player_backpacks[7][1] = rootView.findViewById(R.id.img_match_details_player_8_backpack_2);
        img_player_backpacks[7][2] = rootView.findViewById(R.id.img_match_details_player_8_backpack_3);

        img_player_backpacks[8][0] = rootView.findViewById(R.id.img_match_details_player_9_backpack_1);
        img_player_backpacks[8][1] = rootView.findViewById(R.id.img_match_details_player_9_backpack_2);
        img_player_backpacks[8][2] = rootView.findViewById(R.id.img_match_details_player_9_backpack_3);

        img_player_backpacks[9][0] = rootView.findViewById(R.id.img_match_details_player_10_backpack_1);
        img_player_backpacks[9][1] = rootView.findViewById(R.id.img_match_details_player_10_backpack_2);
        img_player_backpacks[9][2] = rootView.findViewById(R.id.img_match_details_player_10_backpack_3);
    }

    private void setDetails(String s) {

        try {
            JSONObject match = new JSONObject(s);
            Boolean radiantWin = match.getBoolean("radiant_win");

            if (!radiantWin) {
                tv_win_status.setText("Dire Victory!");
                tv_win_status.setTextColor(getResources().getColor(R.color.colorRed));
            }

            tv_radiant_score.setText(match.getString("radiant_score"));
            tv_dire_score.setText(match.getString("dire_score"));

            Long duration = match.getLong("duration");

            //Match duration time formatting, if it's over 1 hour it adds hour*60 to minutes
            Long durationLong = Long.valueOf(duration) * 1000;
            if (durationLong < 3600000) {
                Date dfDr = new java.util.Date(durationLong);
                String durationFormatted = new SimpleDateFormat("m:ss").format(dfDr);
                tv_duration.setText(durationFormatted);
            } else {
                Date dfDr = new java.util.Date(durationLong);
                String seconds = new SimpleDateFormat("ss").format(dfDr);
                String minutes = new SimpleDateFormat("m").format(dfDr);
                int minutesInt = Integer.valueOf(minutes) + (int) (durationLong / 3600000) * 60;
                String durationFormatted = "" + minutesInt + ":" + seconds;
                tv_duration.setText(durationFormatted);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Store user JSON for further use just in case
        for (int i = 0; i < 10; i++) {
            try {
                playerJsons[i] = ODJsonParser.getMatchPlayerJson(s, i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String name, mmr, lvl, kill, death, assist, gpm, xpm, lh, dn, hd, hh, td, gold;
        // Parse into views
        NumberFormat formatter = new DecimalFormat("#0.0");
        for (int i = 0; i < 10; i++) {
            try {
                name = playerJsons[i].getString("personaname");
                Log.d("PLAYER: ", playerJsons[i].getString("personaname"));
            } catch (JSONException e) {
                name = "Anonymous";
            }
            tv_player_names[i].setText(name);

            mmr = "TBD";
            tv_player_mmrs[i].setText(mmr);

            try {
                lvl = playerJsons[i].getString("level") != null
                        ? playerJsons[i].getString("level")
                        : "-";
                tv_player_lvls[i].setText(lvl);

                kill = playerJsons[i].getString("kills") != null
                        ? playerJsons[i].getString("kills")
                        : "0";
                tv_player_kills[i].setText(kill);

                death = playerJsons[i].getString("deaths") != null
                        ? playerJsons[i].getString("deaths")
                        : "-";
                tv_player_deaths[i].setText(death);

                assist = playerJsons[i].getString("assists") != null
                        ? playerJsons[i].getString("assists")
                        : "-";
                tv_player_assists[i].setText(assist);

                gpm = playerJsons[i].getString("gold_per_min") != null
                        ? playerJsons[i].getString("gold_per_min")
                        : "-";
                tv_player_gpms[i].setText(gpm);

                xpm = playerJsons[i].getString("xp_per_min") != null
                        ? playerJsons[i].getString("xp_per_min")
                        : "-";
                tv_player_xpms[i].setText(xpm);

                lh = playerJsons[i].getString("last_hits") != null
                        ? playerJsons[i].getString("last_hits")
                        : "-";
                tv_player_lhs[i].setText(lh);

                dn = playerJsons[i].getString("denies") != null
                        ? playerJsons[i].getString("denies")
                        : "-";
                tv_player_dns[i].setText(dn);

                hd = playerJsons[i].getString("hero_damage") != null
                        ? playerJsons[i].getString("hero_damage")
                        : "-";
                if (Integer.parseInt(hd) > 2000) {

                    hd = formatter.format(Double.parseDouble(hd) / 1000) + "k";
                }
                tv_player_hds[i].setText(hd);

                hh = playerJsons[i].getString("hero_healing") != null
                        ? playerJsons[i].getString("hero_healing")
                        : "-";
                if (Integer.parseInt(hh) > 2000) {

                    hh = formatter.format(Double.parseDouble(hh) / 1000) + "k";
                }
                tv_player_hhs[i].setText(hh);

                td = playerJsons[i].getString("tower_damage") != null
                        ? playerJsons[i].getString("tower_damage")
                        : "-";
                if (Integer.parseInt(td) > 2000) {

                    td = formatter.format(Double.parseDouble(td) / 1000) + "k";
                }
                tv_player_tds[i].setText(td);

                gold = playerJsons[i].getString("gold") != null
                        ? playerJsons[i].getString("gold")
                        : "-";
                if (Integer.parseInt(gold) > 2000) {

                    gold = formatter.format(Double.parseDouble(gold) / 1000) + "k";
                }
                tv_player_golds[i].setText(gold);

                img_player_heroes[i].setImageResource(HeroValues.heroIma[playerJsons[i].getInt("hero_id")]);

                img_player_items[i][0].setImageResource(ItemValues.ItemImages[playerJsons[i].getInt("item_0")]);
                img_player_items[i][1].setImageResource(ItemValues.ItemImages[playerJsons[i].getInt("item_1")]);
                img_player_items[i][2].setImageResource(ItemValues.ItemImages[playerJsons[i].getInt("item_2")]);
                img_player_items[i][3].setImageResource(ItemValues.ItemImages[playerJsons[i].getInt("item_3")]);
                img_player_items[i][4].setImageResource(ItemValues.ItemImages[playerJsons[i].getInt("item_4")]);
                img_player_items[i][5].setImageResource(ItemValues.ItemImages[playerJsons[i].getInt("item_5")]);

                img_player_backpacks[i][0].setImageResource(ItemValues.ItemImages[playerJsons[i].getInt("backpack_0")]);
                img_player_backpacks[i][1].setImageResource(ItemValues.ItemImages[playerJsons[i].getInt("backpack_1")]);
                img_player_backpacks[i][2].setImageResource(ItemValues.ItemImages[playerJsons[i].getInt("backpack_2")]);

                cl_match_details.setVisibility(View.VISIBLE);
                pb_match_details.setVisibility(View.INVISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
