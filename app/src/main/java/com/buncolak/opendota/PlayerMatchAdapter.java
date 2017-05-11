package com.buncolak.opendota;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buncolak.opendota.data.HeroValues;
import com.buncolak.opendota.data.MatchesDBContract;
import com.buncolak.opendota.MainActivity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bunya on 07-May-17.
 */

public class PlayerMatchAdapter extends RecyclerView.Adapter<PlayerMatchAdapter.PlayerMatchViewHolder>{

    Cursor mCursor;
    Context mContext;

    private final MatchClickListener matchClickListener;

    public interface MatchClickListener{
        void onMatchClick(int position);
    }

    public PlayerMatchAdapter(Context context,Cursor cursor, MatchClickListener listener){
        mCursor = cursor;
        matchClickListener = listener;
        mContext = context;
    }

    @Override
    public PlayerMatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.player_match_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(layoutID,parent,false);

        return new PlayerMatchViewHolder(itemView);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(PlayerMatchViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) return;

        mCursor.moveToPosition(position);
        long matchID = mCursor.getLong(mCursor.getColumnIndex(MatchesDBContract.AllMatchesEntry.COLUMN_MATCH_ID));
        int hero_id = mCursor.getInt(mCursor.getColumnIndex(MatchesDBContract.AllMatchesEntry.COLUMN_HERO_ID));
        String duration = mCursor.getString(mCursor.getColumnIndex(MatchesDBContract.AllMatchesEntry.COLUMN_DURATION));
        String kills = mCursor.getString(mCursor.getColumnIndex(MatchesDBContract.AllMatchesEntry.COLUMN_KILLS));
        String deaths = mCursor.getString(mCursor.getColumnIndex(MatchesDBContract.AllMatchesEntry.COLUMN_DEATHS));
        String assists = mCursor.getString(mCursor.getColumnIndex(MatchesDBContract.AllMatchesEntry.COLUMN_ASSISTS));
        String startTime = mCursor.getString(mCursor.getColumnIndex(MatchesDBContract.AllMatchesEntry.COLUMN_START_TIME));
        String radiant_win = mCursor.getString(mCursor.getColumnIndex(MatchesDBContract.AllMatchesEntry.COLUMN_RADIANT_WIN));
        int player_slot = mCursor.getInt(mCursor.getColumnIndex(MatchesDBContract.AllMatchesEntry.COLUMN_PLAYER_SLOT));

        holder.heroImage.setImageDrawable(mContext.getDrawable(HeroValues.heroIma[hero_id]));

        holder.heroName.setText(HeroValues.heroNames[hero_id]);
        holder.matchId.setText(String.valueOf(matchID));

        //Match start time formatting
        Long startTimeLong = Long.valueOf(startTime)*1000;
        Date df = new java.util.Date(startTimeLong);
        String startTimeFormatted = new SimpleDateFormat("MMM d, EEE").format(df);
        holder.dateTv.setText(startTimeFormatted);

        //Match duration time formatting, if it's over 1 hour it adds hour*60 to minutes
        Long durationLong = Long.valueOf(duration)*1000;
        if (durationLong<3600000) {
            Date dfDr = new java.util.Date(durationLong);
            String durationFormatted = new SimpleDateFormat("m:ss").format(dfDr);
            holder.duration.setText(durationFormatted);
        }else{
            Date dfDr = new java.util.Date(durationLong);
            String seconds = new SimpleDateFormat("ss").format(dfDr);
            String minutes = new SimpleDateFormat("m").format(dfDr);
            int minutesInt = Integer.valueOf(minutes) + (int)(durationLong/3600000)*60;
            String durationFormatted = ""+minutesInt+":"+seconds;
            holder.duration.setText(durationFormatted);
        }

        holder.kills.setText(kills);
        holder.deaths.setText(deaths);
        holder.assists.setText(assists);

        if (player_slot > 100){
            if (Boolean.valueOf(radiant_win)){
                holder.winTv.setText(mContext.getString(R.string.loss));
                holder.winTv.setTextColor(mContext.getResources().getColor(R.color.colorRed,null));
            }else {
                holder.winTv.setText(mContext.getString(R.string.win));
                holder.winTv.setTextColor(mContext.getResources().getColor(R.color.colorGreen,null));
            }
        }else{
            if (!Boolean.valueOf(radiant_win)){
                holder.winTv.setText(mContext.getString(R.string.loss));
                holder.winTv.setTextColor(mContext.getResources().getColor(R.color.colorRed,null));
            }else {
                holder.winTv.setText(mContext.getString(R.string.win));
                holder.winTv.setTextColor(mContext.getResources().getColor(R.color.colorGreen,null));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    public void swapData(Cursor data){
        if (mCursor!=null) mCursor.close();
        mCursor = data;
        if (mCursor!=null)
            this.notifyDataSetChanged();
    }

    class PlayerMatchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView matchId, heroName, playerSide, gameMode, duration, dateTv, winTv;
        TextView kills,deaths,assists;
        ImageView heroImage;

        public PlayerMatchViewHolder(View itemView) {
            super(itemView);
            matchId = (TextView)itemView.findViewById(R.id.matchIdTV);
            heroName = (TextView)itemView.findViewById(R.id.hero_name);
            playerSide = (TextView)itemView.findViewById(R.id.playerSide);
            gameMode = (TextView)itemView.findViewById(R.id.gameMode);
            duration = (TextView)itemView.findViewById(R.id.duration_value);
            dateTv = (TextView)itemView.findViewById(R.id.date);
            winTv = (TextView)itemView.findViewById(R.id.match_win);
            kills = (TextView)itemView.findViewById(R.id.kills);
            deaths = (TextView)itemView.findViewById(R.id.deaths);
            assists = (TextView)itemView.findViewById(R.id.assists);
            heroImage = (ImageView)itemView.findViewById(R.id.hero_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            matchClickListener.onMatchClick(position);
        }
    }
}
