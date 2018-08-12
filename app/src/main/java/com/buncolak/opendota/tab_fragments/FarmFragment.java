package com.buncolak.opendota.tab_fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

/**
 * Created by bunya on 11-May-17.
 */

public class FarmFragment extends Fragment {

    TextView[] tv_player_names, tv_player_hero_kills, tv_player_creeps, tv_player_neutrals, tv_player_ancients, tv_player_towers, tv_player_couriers, tv_player_roshan, tv_player_observers, tv_player_necros;

    ImageView[] img_player_heroes;

    JSONObject[] playerJsons = new JSONObject[10];

    public FarmFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_match_farm, container, false);
        getActivity().setTitle(getResources().getString(R.string.title_activity_match_details) + ": " + MatchDetailsActivity.matchId);
        initializeView(rootView);
        setDetails(MatchDetailsActivity.matchJson);
        return rootView;
    }

    private void initializeView(View rootView) {
        tv_player_names = new TextView[10];
        tv_player_hero_kills = new TextView[10];
        tv_player_creeps = new TextView[10];
        tv_player_neutrals = new TextView[10];
        tv_player_ancients = new TextView[10];
        tv_player_towers = new TextView[10];
        tv_player_couriers = new TextView[10];
        tv_player_roshan = new TextView[10];
        tv_player_observers = new TextView[10];
        tv_player_necros = new TextView[10];
        img_player_heroes = new ImageView[10];

        tv_player_names[0] = rootView.findViewById(R.id.tv_match_farm_name_player_1);
        tv_player_names[1] = rootView.findViewById(R.id.tv_match_farm_name_player_2);
        tv_player_names[2] = rootView.findViewById(R.id.tv_match_farm_name_player_3);
        tv_player_names[3] = rootView.findViewById(R.id.tv_match_farm_name_player_4);
        tv_player_names[4] = rootView.findViewById(R.id.tv_match_farm_name_player_5);
        tv_player_names[5] = rootView.findViewById(R.id.tv_match_farm_name_player_6);
        tv_player_names[6] = rootView.findViewById(R.id.tv_match_farm_name_player_7);
        tv_player_names[7] = rootView.findViewById(R.id.tv_match_farm_name_player_8);
        tv_player_names[8] = rootView.findViewById(R.id.tv_match_farm_name_player_9);
        tv_player_names[9] = rootView.findViewById(R.id.tv_match_farm_name_player_10);

        tv_player_hero_kills[0] = rootView.findViewById(R.id.tv_match_farm_heroes_player_1);
        tv_player_hero_kills[1] = rootView.findViewById(R.id.tv_match_farm_heroes_player_2);
        tv_player_hero_kills[2] = rootView.findViewById(R.id.tv_match_farm_heroes_player_3);
        tv_player_hero_kills[3] = rootView.findViewById(R.id.tv_match_farm_heroes_player_4);
        tv_player_hero_kills[4] = rootView.findViewById(R.id.tv_match_farm_heroes_player_5);
        tv_player_hero_kills[5] = rootView.findViewById(R.id.tv_match_farm_heroes_player_6);
        tv_player_hero_kills[6] = rootView.findViewById(R.id.tv_match_farm_heroes_player_7);
        tv_player_hero_kills[7] = rootView.findViewById(R.id.tv_match_farm_heroes_player_8);
        tv_player_hero_kills[8] = rootView.findViewById(R.id.tv_match_farm_heroes_player_9);
        tv_player_hero_kills[9] = rootView.findViewById(R.id.tv_match_farm_heroes_player_10);

        tv_player_creeps[0] = rootView.findViewById(R.id.tv_match_farm_creeps_player_1);
        tv_player_creeps[1] = rootView.findViewById(R.id.tv_match_farm_creeps_player_2);
        tv_player_creeps[2] = rootView.findViewById(R.id.tv_match_farm_creeps_player_3);
        tv_player_creeps[3] = rootView.findViewById(R.id.tv_match_farm_creeps_player_4);
        tv_player_creeps[4] = rootView.findViewById(R.id.tv_match_farm_creeps_player_5);
        tv_player_creeps[5] = rootView.findViewById(R.id.tv_match_farm_creeps_player_6);
        tv_player_creeps[6] = rootView.findViewById(R.id.tv_match_farm_creeps_player_7);
        tv_player_creeps[7] = rootView.findViewById(R.id.tv_match_farm_creeps_player_8);
        tv_player_creeps[8] = rootView.findViewById(R.id.tv_match_farm_creeps_player_9);
        tv_player_creeps[9] = rootView.findViewById(R.id.tv_match_farm_creeps_player_10);

        tv_player_neutrals[0] = rootView.findViewById(R.id.tv_match_farm_neutrals_player_1);
        tv_player_neutrals[1] = rootView.findViewById(R.id.tv_match_farm_neutrals_player_2);
        tv_player_neutrals[2] = rootView.findViewById(R.id.tv_match_farm_neutrals_player_3);
        tv_player_neutrals[3] = rootView.findViewById(R.id.tv_match_farm_neutrals_player_4);
        tv_player_neutrals[4] = rootView.findViewById(R.id.tv_match_farm_neutrals_player_5);
        tv_player_neutrals[5] = rootView.findViewById(R.id.tv_match_farm_neutrals_player_6);
        tv_player_neutrals[6] = rootView.findViewById(R.id.tv_match_farm_neutrals_player_7);
        tv_player_neutrals[7] = rootView.findViewById(R.id.tv_match_farm_neutrals_player_8);
        tv_player_neutrals[8] = rootView.findViewById(R.id.tv_match_farm_neutrals_player_9);
        tv_player_neutrals[9] = rootView.findViewById(R.id.tv_match_farm_neutrals_player_10);

        tv_player_ancients[0] = rootView.findViewById(R.id.tv_match_farm_ancients_player_1);
        tv_player_ancients[1] = rootView.findViewById(R.id.tv_match_farm_ancients_player_2);
        tv_player_ancients[2] = rootView.findViewById(R.id.tv_match_farm_ancients_player_3);
        tv_player_ancients[3] = rootView.findViewById(R.id.tv_match_farm_ancients_player_4);
        tv_player_ancients[4] = rootView.findViewById(R.id.tv_match_farm_ancients_player_5);
        tv_player_ancients[5] = rootView.findViewById(R.id.tv_match_farm_ancients_player_6);
        tv_player_ancients[6] = rootView.findViewById(R.id.tv_match_farm_ancients_player_7);
        tv_player_ancients[7] = rootView.findViewById(R.id.tv_match_farm_ancients_player_8);
        tv_player_ancients[8] = rootView.findViewById(R.id.tv_match_farm_ancients_player_9);
        tv_player_ancients[9] = rootView.findViewById(R.id.tv_match_farm_ancients_player_10);

        tv_player_towers[0] = rootView.findViewById(R.id.tv_match_farm_towers_player_1);
        tv_player_towers[1] = rootView.findViewById(R.id.tv_match_farm_towers_player_2);
        tv_player_towers[2] = rootView.findViewById(R.id.tv_match_farm_towers_player_3);
        tv_player_towers[3] = rootView.findViewById(R.id.tv_match_farm_towers_player_4);
        tv_player_towers[4] = rootView.findViewById(R.id.tv_match_farm_towers_player_5);
        tv_player_towers[5] = rootView.findViewById(R.id.tv_match_farm_towers_player_6);
        tv_player_towers[6] = rootView.findViewById(R.id.tv_match_farm_towers_player_7);
        tv_player_towers[7] = rootView.findViewById(R.id.tv_match_farm_towers_player_8);
        tv_player_towers[8] = rootView.findViewById(R.id.tv_match_farm_towers_player_9);
        tv_player_towers[9] = rootView.findViewById(R.id.tv_match_farm_towers_player_10);

        tv_player_couriers[0] = rootView.findViewById(R.id.tv_match_farm_couriers_player_1);
        tv_player_couriers[1] = rootView.findViewById(R.id.tv_match_farm_couriers_player_2);
        tv_player_couriers[2] = rootView.findViewById(R.id.tv_match_farm_couriers_player_3);
        tv_player_couriers[3] = rootView.findViewById(R.id.tv_match_farm_couriers_player_4);
        tv_player_couriers[4] = rootView.findViewById(R.id.tv_match_farm_couriers_player_5);
        tv_player_couriers[5] = rootView.findViewById(R.id.tv_match_farm_couriers_player_6);
        tv_player_couriers[6] = rootView.findViewById(R.id.tv_match_farm_couriers_player_7);
        tv_player_couriers[7] = rootView.findViewById(R.id.tv_match_farm_couriers_player_8);
        tv_player_couriers[8] = rootView.findViewById(R.id.tv_match_farm_couriers_player_9);
        tv_player_couriers[9] = rootView.findViewById(R.id.tv_match_farm_couriers_player_10);

        tv_player_roshan[0] = rootView.findViewById(R.id.tv_match_farm_roshan_player_1);
        tv_player_roshan[1] = rootView.findViewById(R.id.tv_match_farm_roshan_player_2);
        tv_player_roshan[2] = rootView.findViewById(R.id.tv_match_farm_roshan_player_3);
        tv_player_roshan[3] = rootView.findViewById(R.id.tv_match_farm_roshan_player_4);
        tv_player_roshan[4] = rootView.findViewById(R.id.tv_match_farm_roshan_player_5);
        tv_player_roshan[5] = rootView.findViewById(R.id.tv_match_farm_roshan_player_6);
        tv_player_roshan[6] = rootView.findViewById(R.id.tv_match_farm_roshan_player_7);
        tv_player_roshan[7] = rootView.findViewById(R.id.tv_match_farm_roshan_player_8);
        tv_player_roshan[8] = rootView.findViewById(R.id.tv_match_farm_roshan_player_9);
        tv_player_roshan[9] = rootView.findViewById(R.id.tv_match_farm_roshan_player_10);

        tv_player_observers[0] = rootView.findViewById(R.id.tv_match_farm_observers_player_1);
        tv_player_observers[1] = rootView.findViewById(R.id.tv_match_farm_observers_player_2);
        tv_player_observers[2] = rootView.findViewById(R.id.tv_match_farm_observers_player_3);
        tv_player_observers[3] = rootView.findViewById(R.id.tv_match_farm_observers_player_4);
        tv_player_observers[4] = rootView.findViewById(R.id.tv_match_farm_observers_player_5);
        tv_player_observers[5] = rootView.findViewById(R.id.tv_match_farm_observers_player_6);
        tv_player_observers[6] = rootView.findViewById(R.id.tv_match_farm_observers_player_7);
        tv_player_observers[7] = rootView.findViewById(R.id.tv_match_farm_observers_player_8);
        tv_player_observers[8] = rootView.findViewById(R.id.tv_match_farm_observers_player_9);
        tv_player_observers[9] = rootView.findViewById(R.id.tv_match_farm_observers_player_10);

        tv_player_necros[0] = rootView.findViewById(R.id.tv_match_farm_necronomicon_player_1);
        tv_player_necros[1] = rootView.findViewById(R.id.tv_match_farm_necronomicon_player_2);
        tv_player_necros[2] = rootView.findViewById(R.id.tv_match_farm_necronomicon_player_3);
        tv_player_necros[3] = rootView.findViewById(R.id.tv_match_farm_necronomicon_player_4);
        tv_player_necros[4] = rootView.findViewById(R.id.tv_match_farm_necronomicon_player_5);
        tv_player_necros[5] = rootView.findViewById(R.id.tv_match_farm_necronomicon_player_6);
        tv_player_necros[6] = rootView.findViewById(R.id.tv_match_farm_necronomicon_player_7);
        tv_player_necros[7] = rootView.findViewById(R.id.tv_match_farm_necronomicon_player_8);
        tv_player_necros[8] = rootView.findViewById(R.id.tv_match_farm_necronomicon_player_9);
        tv_player_necros[9] = rootView.findViewById(R.id.tv_match_farm_necronomicon_player_10);

        img_player_heroes[0] = rootView.findViewById(R.id.img_match_farm_player_1);
        img_player_heroes[1] = rootView.findViewById(R.id.img_match_farm_player_2);
        img_player_heroes[2] = rootView.findViewById(R.id.img_match_farm_player_3);
        img_player_heroes[3] = rootView.findViewById(R.id.img_match_farm_player_4);
        img_player_heroes[4] = rootView.findViewById(R.id.img_match_farm_player_5);
        img_player_heroes[5] = rootView.findViewById(R.id.img_match_farm_player_6);
        img_player_heroes[6] = rootView.findViewById(R.id.img_match_farm_player_7);
        img_player_heroes[7] = rootView.findViewById(R.id.img_match_farm_player_8);
        img_player_heroes[8] = rootView.findViewById(R.id.img_match_farm_player_9);
        img_player_heroes[9] = rootView.findViewById(R.id.img_match_farm_player_10);
    }


    private void setDetails(String s) {
        for (int i = 0; i < 10; i++) {
            try {
                playerJsons[i] = ODJsonParser.getMatchPlayerJson(s, i);
                img_player_heroes[i].setImageResource(HeroValues.heroIma[playerJsons[i].getInt("hero_id")]);

                try {
                    tv_player_names[i].setText(playerJsons[i].getString("personaname"));
                } catch (JSONException e) {
                    tv_player_names[i].setText("Anonymous");
                }

                if (playerJsons[i].getInt("hero_kills") != 0)
                    tv_player_hero_kills[i].setText(String.valueOf(playerJsons[i].getInt("hero_kills")));
                else
                    tv_player_hero_kills[i].setText("-");

                if (playerJsons[i].getInt("lane_kills") != 0)
                    tv_player_creeps[i].setText(String.valueOf(playerJsons[i].getInt("lane_kills")));
                else
                    tv_player_creeps[i].setText("-");

                if (playerJsons[i].getInt("neutral_kills") != 0)
                    tv_player_neutrals[i].setText(String.valueOf(playerJsons[i].getInt("neutral_kills")));
                else
                    tv_player_neutrals[i].setText("-");

                if (playerJsons[i].getInt("ancient_kills") != 0)
                    tv_player_ancients[i].setText(String.valueOf(playerJsons[i].getInt("ancient_kills")));
                else
                    tv_player_ancients[i].setText("-");

                if (playerJsons[i].getInt("tower_kills") != 0)
                    tv_player_towers[i].setText(String.valueOf(playerJsons[i].getInt("tower_kills")));
                else
                    tv_player_towers[i].setText("-");

                if (playerJsons[i].getInt("courier_kills") != 0)
                    tv_player_couriers[i].setText(String.valueOf(playerJsons[i].getInt("courier_kills")));
                else
                    tv_player_couriers[i].setText("-");

                if (playerJsons[i].getInt("roshan_kills") != 0)
                    tv_player_roshan[i].setText(String.valueOf(playerJsons[i].getInt("roshan_kills")));
                else
                    tv_player_roshan[i].setText("-");

                if (playerJsons[i].getInt("observer_kills") != 0)
                    tv_player_observers[i].setText(String.valueOf(playerJsons[i].getInt("observer_kills")));
                else
                    tv_player_observers[i].setText("-");

                if (playerJsons[i].getInt("necronomicon_kills") != 0)
                    tv_player_necros[i].setText(String.valueOf(playerJsons[i].getInt("necronomicon_kills")));
                else
                    tv_player_necros[i].setText("-");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

