package com.buncolak.opendota.tab_fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buncolak.opendota.MatchDetailsActivity;
import com.buncolak.opendota.R;
import com.buncolak.opendota.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by bunya on 11-May-17.
 */

public class ChatFragment extends Fragment {

    RecyclerView rv_match_chat;
    ChatAdapter adapter;

    public ChatFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_match_chat, container, false);
        getActivity().setTitle(getResources().getString(R.string.title_activity_match_details) + ": " + MatchDetailsActivity.matchId);
        initializeView(rootView);
        setDetails(MatchDetailsActivity.matchJson);
        return rootView;
    }

    private void initializeView(View rootView) {
        rv_match_chat = rootView.findViewById(R.id.rv_match_chat);

        adapter = new ChatAdapter(getContext());
        rv_match_chat.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv_match_chat.setLayoutManager(llm);
    }

    private void setDetails(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray chatJson = jsonObject.getJSONArray("chat");

            int count = 0;
            String[] names = new String[count];
            String[] messages = new String[count];
            int[] heroes = new int[count];

            for (int i = 0; i < chatJson.length(); i++) {
                if (chatJson.getJSONObject(i).getString("type").equals("chat")) {
                    if (count != 0) {
                        count++;
                        String[] tempNames = names;
                        String[] tempMessages = messages;
                        int[] tempHeroes = heroes;

                        names = new String[count];
                        messages = new String[count];
                        heroes = new int[count];

                        for (int j = 0; j < count - 1; j++) {
                            names[j] = tempNames[j];
                            messages[j] = tempMessages[j];
                            heroes[j] = tempHeroes[j];
                        }
                        names[count - 1] = chatJson.getJSONObject(i).getString("unit");
                        messages[count - 1] = chatJson.getJSONObject(i).getString("key");
                        heroes[count - 1] =
                                jsonObject.getJSONArray("players")
                                        .getJSONObject
                                                (chatJson
                                                        .getJSONObject(i)
                                                        .getInt("slot"))
                                        .getInt("hero_id");
                    } else {
                        count++;

                        names = new String[count];
                        messages = new String[count];
                        heroes = new int[count];

                        names[count - 1] = chatJson.getJSONObject(i).getString("unit");
                        messages[count - 1] = chatJson.getJSONObject(i).getString("key");
                        heroes[count - 1] =
                                jsonObject.getJSONArray("players")
                                        .getJSONObject
                                                (chatJson
                                                        .getJSONObject(i)
                                                        .getInt("slot"))
                                        .getInt("hero_id");
                    }
                }
            }
            adapter.changeData(names, messages, heroes);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
