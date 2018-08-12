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

import com.buncolak.opendota.MatchDetailsActivity;
import com.buncolak.opendota.R;
import com.buncolak.opendota.data.HeroValues;
import com.buncolak.opendota.utils.NetworkUtils;
import com.buncolak.opendota.utils.ODJsonParser;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by bunya on 11-May-17.
 */

public class GraphsFragment extends Fragment {

    GraphView gv_radiant_adv, gv_player_golds, gv_player_lhs;
    JSONObject[] playerJsons = new JSONObject[10];

    public GraphsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_match_graphs, container, false);
        getActivity().setTitle(getResources().getString(R.string.title_activity_match_details) + ": " + MatchDetailsActivity.matchId);
        initializeView(rootView);
        setDetails(MatchDetailsActivity.matchJson);
        return rootView;
    }

    private void setDetails(String s) {
        try {
            JSONObject json = new JSONObject(s);
            JSONArray radiant_adv = json.getJSONArray("radiant_gold_adv");
            int count = radiant_adv.length();
            DataPoint[] gold_adv = new DataPoint[count];
            int minGold = 0, maxGold = 0;
            for (int i = 0; i < count; i++) {
                gold_adv[i] = new DataPoint(i, radiant_adv.getInt(i));
                if (radiant_adv.getInt(i) < minGold)
                    minGold = radiant_adv.getInt(i);
                else if (radiant_adv.getInt(i) > maxGold)
                    maxGold = radiant_adv.getInt(i);
            }
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(gold_adv);
            series.setColor(getResources().getColor(R.color.colorGold));
            gv_radiant_adv.addSeries(series);
            gv_radiant_adv.getViewport().setMaxX(count);
            gv_radiant_adv.getViewport().setMinY(minGold);
            gv_radiant_adv.getViewport().setMaxY(maxGold);

            DataPoint[][] player_gold = new DataPoint[10][];
            DataPoint[][] player_lh = new DataPoint[10][];
            int[] player_colors = new int[]{
                    getResources().getColor(R.color.colorBlue),
                    getResources().getColor(R.color.colorTeal),
                    getResources().getColor(R.color.colorPurple),
                    getResources().getColor(R.color.colorGold),
                    getResources().getColor(R.color.colorOrange),
                    getResources().getColor(R.color.colorPurple),
                    getResources().getColor(R.color.colorMustard),
                    getResources().getColor(R.color.colorSecondary),
                    getResources().getColor(R.color.colorGreen),
                    getResources().getColor(R.color.colorBrown)
            };

            int maxLh = 0, minLh = 0;
            for (int i = 0; i < 10; i++) {
                playerJsons[i] = ODJsonParser.getMatchPlayerJson(s, i);
                JSONArray player_gold_json = playerJsons[i].getJSONArray("gold_t");
                JSONArray player_lh_json = playerJsons[i].getJSONArray("lh_t");
                player_gold[i] = new DataPoint[count];
                player_lh[i] = new DataPoint[count];

                for (int j = 0; j < count; j++) {
                    player_gold[i][j] = new DataPoint(j, player_gold_json.getInt(j));
                    player_lh[i][j] = new DataPoint(j, player_lh_json.getInt(j));

                    // Find edges for the gold
                    if (player_gold_json.getInt(j) < minGold)
                        minGold = player_gold_json.getInt(j);
                    else if (player_gold_json.getInt(j) > maxGold)
                        maxGold = player_gold_json.getInt(j);

                    // Find edges for last hits
                    if (player_lh_json.getInt(j) < minLh)
                        minLh = player_lh_json.getInt(j);
                    else if (player_lh_json.getInt(j) > maxLh)
                        maxLh = player_lh_json.getInt(j);
                }

                LineGraphSeries<DataPoint> player_gold_serie = new LineGraphSeries<>(player_gold[i]);
                LineGraphSeries<DataPoint> player_lh_serie = new LineGraphSeries<>(player_lh[i]);

                // Set colors and titles
                player_gold_serie.setTitle(HeroValues.heroNames[playerJsons[i].getInt("hero_id")]);
                player_lh_serie.setTitle(HeroValues.heroNames[playerJsons[i].getInt("hero_id")]);
                player_gold_serie.setColor(player_colors[i]);
                player_lh_serie.setColor(player_colors[i]);

                gv_player_golds.addSeries(player_gold_serie);
                gv_player_lhs.addSeries(player_lh_serie);
            }

            gv_player_golds.getViewport().setMaxX(count);
            gv_player_golds.getViewport().setMaxY(maxGold);
            gv_player_golds.getLegendRenderer().setVisible(true);
            gv_player_golds.getLegendRenderer().setFixedPosition(50, 0);

            gv_player_lhs.getViewport().setMaxX(count);
            gv_player_lhs.getViewport().setMaxY(maxLh);
            gv_player_lhs.getLegendRenderer().setVisible(true);
            gv_player_lhs.getLegendRenderer().setFixedPosition(50, 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeView(View rootView) {
        gv_radiant_adv = rootView.findViewById(R.id.gv_match_radiand_adv);
        gv_player_golds = rootView.findViewById(R.id.gv_match_player_golds);
        gv_player_lhs = rootView.findViewById(R.id.gv_match_player_lhs);

        gv_radiant_adv.getViewport().setScalable(true);
        gv_radiant_adv.getViewport().setScalableY(true);
        gv_radiant_adv.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.colorWhite));
        gv_radiant_adv.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.colorWhite));
        gv_radiant_adv.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return super.formatLabel(value, isValueX);
                } else {
                    // show currency for y values
                    if (Math.abs(value) > 1000) {
                        int new_value = (int) Math.floor(value / 1000);
                        return new_value + "k";
                    } else
                        return super.formatLabel(value, isValueX);
                }
            }
        });

        gv_player_golds.getViewport().setScalable(true);
        gv_player_golds.getViewport().setScalableY(true);
        gv_player_golds.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.colorWhite));
        gv_player_golds.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.colorWhite));
        gv_player_golds.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return super.formatLabel(value, isValueX);
                } else {
                    // show currency for y values
                    if (Math.abs(value) > 1000) {
                        int new_value = (int) Math.floor(value / 1000);
                        return new_value + "k";
                    } else
                        return super.formatLabel(value, isValueX);
                }
            }
        });

        gv_player_lhs.getViewport().setScalable(true);
        gv_player_lhs.getViewport().setScalableY(true);
        gv_player_lhs.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.colorWhite));
        gv_player_lhs.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.colorWhite));
        gv_player_lhs.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return super.formatLabel(value, isValueX);
                } else {
                    // show currency for y values
                    if (Math.abs(value) > 1000) {
                        int new_value = (int) Math.floor(value / 1000);
                        return new_value + "k";
                    } else
                        return super.formatLabel(value, isValueX);
                }
            }
        });
    }
}
