package com.buncolak.opendota;

import android.animation.Animator;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buncolak.opendota.data.GameConstants;
import com.buncolak.opendota.data.HeroValues;
import com.buncolak.opendota.data.MatchesDBContract;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bunya on 07-May-17.
 */

public class PlayerMatchAdapter extends RecyclerView.Adapter<PlayerMatchAdapter.PlayerMatchViewHolder> {

    Cursor mCursor;
    Context mContext;

    private final MatchClickListener matchClickListener;
    private int skill;

    public interface MatchClickListener {
        void onMatchClick(long matchId);
    }

    public PlayerMatchAdapter(Context context, Cursor cursor,
                              MatchClickListener listener) {
        mCursor = cursor;
        matchClickListener = listener;
        mContext = context;
    }

    @Override
    public PlayerMatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.layout_player_match_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(layoutID, parent, false);

        return new PlayerMatchViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(PlayerMatchViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) return;

        // Get the data from the local DB.
        mCursor.moveToPosition(position);
        long matchID = mCursor.getLong(mCursor.getColumnIndex(MatchesDBContract.AllMatchesEntry.COLUMN_MATCH_ID));
        int hero_id = mCursor.getInt(mCursor.getColumnIndex(MatchesDBContract.AllMatchesEntry.COLUMN_HERO_ID));
        String duration = mCursor.getString(mCursor.getColumnIndex(MatchesDBContract.AllMatchesEntry.COLUMN_DURATION));
        String kills = mCursor.getString(mCursor.getColumnIndex(MatchesDBContract.AllMatchesEntry.COLUMN_KILLS));
        String deaths = mCursor.getString(mCursor.getColumnIndex(MatchesDBContract.AllMatchesEntry.COLUMN_DEATHS));
        String assists = mCursor.getString(mCursor.getColumnIndex(MatchesDBContract.AllMatchesEntry.COLUMN_ASSISTS));
        String startTime = mCursor.getString(mCursor.getColumnIndex(MatchesDBContract.AllMatchesEntry.COLUMN_START_TIME));
        int gameMode = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(MatchesDBContract.AllMatchesEntry.COLUMN_GAME_MODE)));
        String radiant_win = mCursor.getString(mCursor.getColumnIndex(MatchesDBContract.AllMatchesEntry.COLUMN_RADIANT_WIN));
        int player_slot = mCursor.getInt(mCursor.getColumnIndex(MatchesDBContract.AllMatchesEntry.COLUMN_PLAYER_SLOT));
        int skill = mCursor.getInt(mCursor.getColumnIndex(MatchesDBContract.AllMatchesEntry.COLUMN_SKILL));


        // Place the data to views
        try {
            holder.img_hero.setImageDrawable(mContext.getDrawable(HeroValues.heroIma[hero_id]));
        } catch (Exception e) {
            holder.img_hero.setImageDrawable(mContext.getDrawable(HeroValues.heroIma[100]));
        }
        holder.tv_match_id.setText(String.valueOf(matchID));

        //Match start time formatting
        Long startTimeLong = Long.valueOf(startTime) * 1000;
        Date df = new java.util.Date(startTimeLong);
        String startTimeFormatted = new SimpleDateFormat("MMM d, EEE").format(df);
        holder.tv_match_date.setText(startTimeFormatted);

        //Match duration time formatting, if it's over 1 hour it adds hour*60 to minutes
        Long durationLong = Long.valueOf(duration) * 1000;
        if (durationLong < 3600000) {
            Date dfDr = new java.util.Date(durationLong);
            String durationFormatted = new SimpleDateFormat("m:ss").format(dfDr);
            holder.tv_duration.setText(durationFormatted);
        } else {
            Date dfDr = new java.util.Date(durationLong);
            String seconds = new SimpleDateFormat("ss").format(dfDr);
            String minutes = new SimpleDateFormat("m").format(dfDr);
            int minutesInt = Integer.valueOf(minutes) + (int) (durationLong / 3600000) * 60;
            String durationFormatted = "" + minutesInt + ":" + seconds;
            holder.tv_duration.setText(durationFormatted);
        }

        holder.tv_skill.setText(GameConstants.SKILL_LEVELS.get(skill));
        holder.tv_game_mode.setText(GameConstants.GAME_MODES.get(gameMode));
        holder.tv_kills.setText(kills);
        holder.tv_deaths.setText(deaths);
        holder.tv_assists.setText(assists);

        // Depending on the user's side(Dire or Radiant) decide if win.
        if (player_slot > 100) {
            holder.tv_user_side.setText("Dire");
            if (Boolean.valueOf(radiant_win))
                holder.view_win_status.setBackgroundColor(mContext.getResources().getColor(R.color.colorRed));
            else
                holder.view_win_status.setBackgroundColor(mContext.getResources().getColor(R.color.colorGreen));
        } else {
            holder.tv_user_side.setText("Radiant");
            if (!Boolean.valueOf(radiant_win)) {
                holder.view_win_status.setBackgroundColor(mContext.getResources().getColor(R.color.colorRed));
            } else {
                holder.view_win_status.setBackgroundColor(mContext.getResources().getColor(R.color.colorGreen));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    public void swapData(Cursor data) {
        if (mCursor != null) mCursor.close();
        mCursor = data;
        if (mCursor != null)
            this.notifyDataSetChanged();
    }

    class PlayerMatchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_match_id, tv_user_side, tv_game_mode, tv_duration, tv_match_date;
        TextView tv_kills, tv_deaths, tv_assists, tv_skill;
        View view_win_status;
        ImageView img_hero;

        public PlayerMatchViewHolder(View itemView) {
            super(itemView);
            tv_match_id = itemView.findViewById(R.id.tv_match_id);
            tv_user_side = itemView.findViewById(R.id.tv_user_side);
            tv_game_mode = itemView.findViewById(R.id.tv_game_mode);
            tv_skill = itemView.findViewById(R.id.tv_match_skill);
            tv_duration = itemView.findViewById(R.id.tv_duration);
            tv_match_date = itemView.findViewById(R.id.tv_match_date);
            tv_kills = itemView.findViewById(R.id.tv_kills);
            tv_deaths = itemView.findViewById(R.id.tv_deaths);
            tv_assists = itemView.findViewById(R.id.tv_assists);
            img_hero = itemView.findViewById(R.id.img_hero);
            view_win_status = itemView.findViewById(R.id.view_win_status);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            matchClickListener.onMatchClick(Long.valueOf(tv_match_id.getText().toString()));
        }
    }
}
