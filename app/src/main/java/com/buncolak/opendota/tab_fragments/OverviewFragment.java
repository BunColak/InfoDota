package com.buncolak.opendota.tab_fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buncolak.opendota.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by bunya on 11-May-17.
 */

public class OverviewFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>{
    private static final String MATCH_ID = "match_id";

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
        getActivity().setTitle(getResources().getString(R.string.title_activity_match_details)+": 3189654102");
        GraphView graph = (GraphView)rootView.findViewById(R.id.match_details_overview_graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        series.setColor(rootView.getResources().getColor(R.color.colorGold));
        graph.setBackgroundColor(rootView.getResources().getColor(R.color.colorPrimaryMidDark));
        graph.getGridLabelRenderer().
                setHorizontalAxisTitleColor(rootView.getResources().getColor(R.color.colorGray));
        graph.getGridLabelRenderer().
                setHorizontalLabelsColor(rootView.getResources().getColor(R.color.colorGray));
        graph.getGridLabelRenderer().
                setVerticalAxisTitleColor(rootView.getResources().getColor(R.color.colorGray));
        graph.getGridLabelRenderer().
                setVerticalLabelsColor(rootView.getResources().getColor(R.color.colorGray));
        graph.getGridLabelRenderer().
                setHorizontalAxisTitleTextSize(10);
        graph.getGridLabelRenderer().
                setVerticalAxisTitleTextSize(10);
        graph.addSeries(series);
        return rootView;
    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
