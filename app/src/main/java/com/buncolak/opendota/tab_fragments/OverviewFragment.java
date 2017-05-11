package com.buncolak.opendota.tab_fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buncolak.opendota.R;

/**
 * Created by bunya on 11-May-17.
 */

public class OverviewFragment extends Fragment {
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
        TextView textView = (TextView) rootView.findViewById(R.id.match_overview_id);
        textView.setText(getString(R.string.section_format, getArguments().getLong(MATCH_ID)));
        return rootView;
    }
}
