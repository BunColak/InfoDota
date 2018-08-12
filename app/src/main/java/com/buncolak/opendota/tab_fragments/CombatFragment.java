package com.buncolak.opendota.tab_fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buncolak.opendota.MatchDetailsActivity;
import com.buncolak.opendota.R;
import com.buncolak.opendota.data.HeroValues;
import com.buncolak.opendota.utils.NetworkUtils;
import com.buncolak.opendota.utils.ODJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by bunya on 11-May-17.
 */

public class CombatFragment extends Fragment {

    private ImageView[] img_heroes, img_heroes_damage;
    private TextView[][] tv_kills, tv_deaths, tv_player_damage_tos, tv_player_damage_froms;
    private TextView[] tv_player_kills, tv_player_deaths;
    private JSONObject[] playerJsons = new JSONObject[10];
    private int[][] kills_count = new int[10][5];
    private String[] heroes = new String[10];

    public CombatFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_match_combat, container, false);
        getActivity().setTitle(getResources().getString(R.string.title_activity_match_details) + ": " + MatchDetailsActivity.matchId);
        initializeView(rootView);
        setDetails(MatchDetailsActivity.matchJson);
        return rootView;
    }

    private void initializeView(View rootView) {
        tv_kills = new TextView[5][5];
        tv_deaths = new TextView[5][5];
        tv_player_damage_tos = new TextView[5][5];
        tv_player_damage_froms = new TextView[5][5];
        img_heroes = new ImageView[10];
        img_heroes_damage = new ImageView[10];
        tv_player_deaths = new TextView[10];
        tv_player_kills = new TextView[10];

        img_heroes[0] = rootView.findViewById(R.id.img_match_combat_player_1_hero);
        img_heroes[1] = rootView.findViewById(R.id.img_match_combat_player_2_hero);
        img_heroes[2] = rootView.findViewById(R.id.img_match_combat_player_3_hero);
        img_heroes[3] = rootView.findViewById(R.id.img_match_combat_player_4_hero);
        img_heroes[4] = rootView.findViewById(R.id.img_match_combat_player_5_hero);
        img_heroes[5] = rootView.findViewById(R.id.img_match_combat_player_6_hero);
        img_heroes[6] = rootView.findViewById(R.id.img_match_combat_player_7_hero);
        img_heroes[7] = rootView.findViewById(R.id.img_match_combat_player_8_hero);
        img_heroes[8] = rootView.findViewById(R.id.img_match_combat_player_9_hero);
        img_heroes[9] = rootView.findViewById(R.id.img_match_combat_player_10_hero);

        img_heroes_damage[0] = rootView.findViewById(R.id.img_match_combat_player_1_hero_damage);
        img_heroes_damage[1] = rootView.findViewById(R.id.img_match_combat_player_2_hero_damage);
        img_heroes_damage[2] = rootView.findViewById(R.id.img_match_combat_player_3_hero_damage);
        img_heroes_damage[3] = rootView.findViewById(R.id.img_match_combat_player_4_hero_damage);
        img_heroes_damage[4] = rootView.findViewById(R.id.img_match_combat_player_5_hero_damage);
        img_heroes_damage[5] = rootView.findViewById(R.id.img_match_combat_player_6_hero_damage);
        img_heroes_damage[6] = rootView.findViewById(R.id.img_match_combat_player_7_hero_damage);
        img_heroes_damage[7] = rootView.findViewById(R.id.img_match_combat_player_8_hero_damage);
        img_heroes_damage[8] = rootView.findViewById(R.id.img_match_combat_player_9_hero_damage);
        img_heroes_damage[9] = rootView.findViewById(R.id.img_match_combat_player_10_hero_damage);

        tv_kills[0][0] = rootView.findViewById(R.id.tv_kill_1_v_6);
        tv_kills[0][1] = rootView.findViewById(R.id.tv_kill_1_v_7);
        tv_kills[0][2] = rootView.findViewById(R.id.tv_kill_1_v_8);
        tv_kills[0][3] = rootView.findViewById(R.id.tv_kill_1_v_9);
        tv_kills[0][4] = rootView.findViewById(R.id.tv_kill_1_v_10);

        tv_kills[0][0] = rootView.findViewById(R.id.tv_kill_1_v_6);
        tv_kills[0][1] = rootView.findViewById(R.id.tv_kill_1_v_7);
        tv_kills[0][2] = rootView.findViewById(R.id.tv_kill_1_v_8);
        tv_kills[0][3] = rootView.findViewById(R.id.tv_kill_1_v_9);
        tv_kills[0][4] = rootView.findViewById(R.id.tv_kill_1_v_10);

        tv_kills[1][0] = rootView.findViewById(R.id.tv_kill_2_v_6);
        tv_kills[1][1] = rootView.findViewById(R.id.tv_kill_2_v_7);
        tv_kills[1][2] = rootView.findViewById(R.id.tv_kill_2_v_8);
        tv_kills[1][3] = rootView.findViewById(R.id.tv_kill_2_v_9);
        tv_kills[1][4] = rootView.findViewById(R.id.tv_kill_2_v_10);

        tv_kills[2][0] = rootView.findViewById(R.id.tv_kill_3_v_6);
        tv_kills[2][1] = rootView.findViewById(R.id.tv_kill_3_v_7);
        tv_kills[2][2] = rootView.findViewById(R.id.tv_kill_3_v_8);
        tv_kills[2][3] = rootView.findViewById(R.id.tv_kill_3_v_9);
        tv_kills[2][4] = rootView.findViewById(R.id.tv_kill_3_v_10);

        tv_kills[3][0] = rootView.findViewById(R.id.tv_kill_4_v_6);
        tv_kills[3][1] = rootView.findViewById(R.id.tv_kill_4_v_7);
        tv_kills[3][2] = rootView.findViewById(R.id.tv_kill_4_v_8);
        tv_kills[3][3] = rootView.findViewById(R.id.tv_kill_4_v_9);
        tv_kills[3][4] = rootView.findViewById(R.id.tv_kill_4_v_10);

        tv_kills[4][0] = rootView.findViewById(R.id.tv_kill_5_v_6);
        tv_kills[4][1] = rootView.findViewById(R.id.tv_kill_5_v_7);
        tv_kills[4][2] = rootView.findViewById(R.id.tv_kill_5_v_8);
        tv_kills[4][3] = rootView.findViewById(R.id.tv_kill_5_v_9);
        tv_kills[4][4] = rootView.findViewById(R.id.tv_kill_5_v_10);

        tv_deaths[0][0] = rootView.findViewById(R.id.tv_death_1v6);
        tv_deaths[0][1] = rootView.findViewById(R.id.tv_death_1v7);
        tv_deaths[0][2] = rootView.findViewById(R.id.tv_death_1v8);
        tv_deaths[0][3] = rootView.findViewById(R.id.tv_death_1v9);
        tv_deaths[0][4] = rootView.findViewById(R.id.tv_death_1v10);

        tv_deaths[1][0] = rootView.findViewById(R.id.tv_death_2v6);
        tv_deaths[1][1] = rootView.findViewById(R.id.tv_death_2v7);
        tv_deaths[1][2] = rootView.findViewById(R.id.tv_death_2v8);
        tv_deaths[1][3] = rootView.findViewById(R.id.tv_death_2v9);
        tv_deaths[1][4] = rootView.findViewById(R.id.tv_death_2v10);

        tv_deaths[2][0] = rootView.findViewById(R.id.tv_death_3v6);
        tv_deaths[2][1] = rootView.findViewById(R.id.tv_death_3v7);
        tv_deaths[2][2] = rootView.findViewById(R.id.tv_death_3v8);
        tv_deaths[2][3] = rootView.findViewById(R.id.tv_death_3v9);
        tv_deaths[2][4] = rootView.findViewById(R.id.tv_death_3v10);

        tv_deaths[3][0] = rootView.findViewById(R.id.tv_death_4v6);
        tv_deaths[3][1] = rootView.findViewById(R.id.tv_death_4v7);
        tv_deaths[3][2] = rootView.findViewById(R.id.tv_death_4v8);
        tv_deaths[3][3] = rootView.findViewById(R.id.tv_death_4v9);
        tv_deaths[3][4] = rootView.findViewById(R.id.tv_death_4v10);

        tv_deaths[4][0] = rootView.findViewById(R.id.tv_death_5v6);
        tv_deaths[4][1] = rootView.findViewById(R.id.tv_death_5v7);
        tv_deaths[4][2] = rootView.findViewById(R.id.tv_death_5v8);
        tv_deaths[4][3] = rootView.findViewById(R.id.tv_death_5v9);
        tv_deaths[4][4] = rootView.findViewById(R.id.tv_death_5v10);

        tv_player_deaths[0] = rootView.findViewById(R.id.tv_death_1);
        tv_player_deaths[1] = rootView.findViewById(R.id.tv_death_2);
        tv_player_deaths[2] = rootView.findViewById(R.id.tv_death_3);
        tv_player_deaths[3] = rootView.findViewById(R.id.tv_death_4);
        tv_player_deaths[4] = rootView.findViewById(R.id.tv_death_5);
        tv_player_deaths[5] = rootView.findViewById(R.id.tv_death_6);
        tv_player_deaths[6] = rootView.findViewById(R.id.tv_death_7);
        tv_player_deaths[7] = rootView.findViewById(R.id.tv_death_8);
        tv_player_deaths[8] = rootView.findViewById(R.id.tv_death_9);
        tv_player_deaths[9] = rootView.findViewById(R.id.tv_death_10);

        tv_player_kills[0] = rootView.findViewById(R.id.tv_kill_1);
        tv_player_kills[1] = rootView.findViewById(R.id.tv_kill_2);
        tv_player_kills[2] = rootView.findViewById(R.id.tv_kill_3);
        tv_player_kills[3] = rootView.findViewById(R.id.tv_kill_4);
        tv_player_kills[4] = rootView.findViewById(R.id.tv_kill_5);
        tv_player_kills[5] = rootView.findViewById(R.id.tv_kill_6);
        tv_player_kills[6] = rootView.findViewById(R.id.tv_kill_7);
        tv_player_kills[7] = rootView.findViewById(R.id.tv_kill_8);
        tv_player_kills[8] = rootView.findViewById(R.id.tv_kill_9);
        tv_player_kills[9] = rootView.findViewById(R.id.tv_kill_10);

        tv_player_damage_tos[0][0] = rootView.findViewById(R.id.tv_damage_to_1_v_6);
        tv_player_damage_tos[0][1] = rootView.findViewById(R.id.tv_damage_to_1_v_7);
        tv_player_damage_tos[0][2] = rootView.findViewById(R.id.tv_damage_to_1_v_8);
        tv_player_damage_tos[0][3] = rootView.findViewById(R.id.tv_damage_to_1_v_9);
        tv_player_damage_tos[0][4] = rootView.findViewById(R.id.tv_damage_to_1_v_10);

        tv_player_damage_tos[1][0] = rootView.findViewById(R.id.tv_damage_to_2_v_6);
        tv_player_damage_tos[1][1] = rootView.findViewById(R.id.tv_damage_to_2_v_7);
        tv_player_damage_tos[1][2] = rootView.findViewById(R.id.tv_damage_to_2_v_8);
        tv_player_damage_tos[1][3] = rootView.findViewById(R.id.tv_damage_to_2_v_9);
        tv_player_damage_tos[1][4] = rootView.findViewById(R.id.tv_damage_to_2_v_10);

        tv_player_damage_tos[2][0] = rootView.findViewById(R.id.tv_damage_to_3_v_6);
        tv_player_damage_tos[2][1] = rootView.findViewById(R.id.tv_damage_to_3_v_7);
        tv_player_damage_tos[2][2] = rootView.findViewById(R.id.tv_damage_to_3_v_8);
        tv_player_damage_tos[2][3] = rootView.findViewById(R.id.tv_damage_to_3_v_9);
        tv_player_damage_tos[2][4] = rootView.findViewById(R.id.tv_damage_to_3_v_10);

        tv_player_damage_tos[3][0] = rootView.findViewById(R.id.tv_damage_to_4_v_6);
        tv_player_damage_tos[3][1] = rootView.findViewById(R.id.tv_damage_to_4_v_7);
        tv_player_damage_tos[3][2] = rootView.findViewById(R.id.tv_damage_to_4_v_8);
        tv_player_damage_tos[3][3] = rootView.findViewById(R.id.tv_damage_to_4_v_9);
        tv_player_damage_tos[3][4] = rootView.findViewById(R.id.tv_damage_to_4_v_10);

        tv_player_damage_tos[4][0] = rootView.findViewById(R.id.tv_damage_to_5_v_6);
        tv_player_damage_tos[4][1] = rootView.findViewById(R.id.tv_damage_to_5_v_7);
        tv_player_damage_tos[4][2] = rootView.findViewById(R.id.tv_damage_to_5_v_8);
        tv_player_damage_tos[4][3] = rootView.findViewById(R.id.tv_damage_to_5_v_9);
        tv_player_damage_tos[4][4] = rootView.findViewById(R.id.tv_damage_to_5_v_10);


        tv_player_damage_froms[0][0] = rootView.findViewById(R.id.tv_damage_from_1v6);
        tv_player_damage_froms[0][1] = rootView.findViewById(R.id.tv_damage_from_1v7);
        tv_player_damage_froms[0][2] = rootView.findViewById(R.id.tv_damage_from_1v8);
        tv_player_damage_froms[0][3] = rootView.findViewById(R.id.tv_damage_from_1v9);
        tv_player_damage_froms[0][4] = rootView.findViewById(R.id.tv_damage_from_1v10);

        tv_player_damage_froms[1][0] = rootView.findViewById(R.id.tv_damage_from_2v6);
        tv_player_damage_froms[1][1] = rootView.findViewById(R.id.tv_damage_from_2v7);
        tv_player_damage_froms[1][2] = rootView.findViewById(R.id.tv_damage_from_2v8);
        tv_player_damage_froms[1][3] = rootView.findViewById(R.id.tv_damage_from_2v9);
        tv_player_damage_froms[1][4] = rootView.findViewById(R.id.tv_damage_from_2v10);

        tv_player_damage_froms[2][0] = rootView.findViewById(R.id.tv_damage_from_3v6);
        tv_player_damage_froms[2][1] = rootView.findViewById(R.id.tv_damage_from_3v7);
        tv_player_damage_froms[2][2] = rootView.findViewById(R.id.tv_damage_from_3v8);
        tv_player_damage_froms[2][3] = rootView.findViewById(R.id.tv_damage_from_3v9);
        tv_player_damage_froms[2][4] = rootView.findViewById(R.id.tv_damage_from_3v10);

        tv_player_damage_froms[3][0] = rootView.findViewById(R.id.tv_damage_from_4v6);
        tv_player_damage_froms[3][1] = rootView.findViewById(R.id.tv_damage_from_4v7);
        tv_player_damage_froms[3][2] = rootView.findViewById(R.id.tv_damage_from_4v8);
        tv_player_damage_froms[3][3] = rootView.findViewById(R.id.tv_damage_from_4v9);
        tv_player_damage_froms[3][4] = rootView.findViewById(R.id.tv_damage_from_4v10);

        tv_player_damage_froms[4][0] = rootView.findViewById(R.id.tv_damage_from_5v6);
        tv_player_damage_froms[4][1] = rootView.findViewById(R.id.tv_damage_from_5v7);
        tv_player_damage_froms[4][2] = rootView.findViewById(R.id.tv_damage_from_5v8);
        tv_player_damage_froms[4][3] = rootView.findViewById(R.id.tv_damage_from_5v9);
        tv_player_damage_froms[4][4] = rootView.findViewById(R.id.tv_damage_from_5v10);

    }

    private void setDetails(String s) {
        for (int i = 0; i < 10; i++) {
            try {
                playerJsons[i] = ODJsonParser.getMatchPlayerJson(s, i);
                heroes[i] = HeroValues.hero_npc_names[playerJsons[i].getInt("hero_id")];
                img_heroes[i].setImageResource(HeroValues.heroIma[playerJsons[i].getInt("hero_id")]);
                img_heroes_damage[i].setImageResource(HeroValues.heroIma[playerJsons[i].getInt("hero_id")]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Get the kill counts
        int kill_count;
        String killed;
        for (int i = 0; i < 10; i++) {
            try {
                kill_count = playerJsons[i].getJSONArray("kills_log").length();
                for (int j = 0; j < kill_count; j++) {
                    killed = playerJsons[i].getJSONArray("kills_log").getJSONObject(j).getString("key");
                    if (i < 5) {
                        if (killed.equals(heroes[5]))
                            kills_count[i][0]++;
                        else if (killed.equals(heroes[6]))
                            kills_count[i][1]++;
                        else if (killed.equals(heroes[7]))
                            kills_count[i][2]++;
                        else if (killed.equals(heroes[8]))
                            kills_count[i][3]++;
                        else if (killed.equals(heroes[9]))
                            kills_count[i][4]++;


                    } else {
                        if (killed.equals(heroes[0]))
                            kills_count[i][0]++;
                        else if (killed.equals(heroes[1]))
                            kills_count[i][1]++;
                        else if (killed.equals(heroes[2]))
                            kills_count[i][2]++;
                        else if (killed.equals(heroes[3]))
                            kills_count[i][3]++;
                        else if (killed.equals(heroes[4]))
                            kills_count[i][4]++;


                    }
                }
                for (int j = 0; j < 5; j++) {
                    if (i < 5) {
                        // Damages
                        try {
                            tv_player_damage_tos[i][j].setText(formatDamage(playerJsons[i].getJSONObject("damage").getString(heroes[j + 5])));
                        } catch (JSONException e) {
                            tv_player_damage_tos[i][j].setText("-");
                        }

                        try {
                            tv_player_damage_froms[i][j].setText(formatDamage(playerJsons[i].getJSONObject("damage_taken").getString(heroes[j + 5])));
                        } catch (JSONException e) {
                            tv_player_damage_froms[i][j].setText("-");
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Write the stats
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (kills_count[i][j] != 0)
                    tv_kills[i][j].setText(String.valueOf(kills_count[i][j]));
                else
                    tv_kills[i][j].setText("-");
                if (kills_count[j + 5][i] != 0)
                    tv_deaths[i][j].setText(String.valueOf(kills_count[j + 5][i]));
                else
                    tv_deaths[i][j].setText("-");
            }
        }

        // Total kills
        int kill_sum;
        int death_sum;
        for (int i = 0; i < 10; i++) {
            kill_sum = 0;
            death_sum = 0;
            for (int j = 0; j < 5; j++) {
                kill_sum += kills_count[i][j];
                if (i >= 5)
                    death_sum += kills_count[j][i - 5];
                else
                    death_sum += kills_count[j + 5][i];
            }

            tv_player_kills[i].setText(String.valueOf(kill_sum));
            tv_player_deaths[i].setText(String.valueOf(death_sum));
        }
    }


    private String formatDamage(String damage) {
        NumberFormat formatter = new DecimalFormat("#0.0");

        if (Integer.parseInt(damage) > 1000) {
            return String.valueOf(formatter.format(Double.parseDouble(damage) / 1000) + "k");
        }

        return damage;
    }

}