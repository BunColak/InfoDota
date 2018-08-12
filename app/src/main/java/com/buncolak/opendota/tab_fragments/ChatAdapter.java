package com.buncolak.opendota.tab_fragments;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buncolak.opendota.PlayerMatchAdapter;
import com.buncolak.opendota.R;
import com.buncolak.opendota.data.HeroValues;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{

    private String[] names, messages;
    private int[] heroes;
    private Context matchContext;

    public ChatAdapter(Context context) {
        matchContext = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.item_match_chat;
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(layoutID,parent,false);

        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.tv_chat_message.setText(messages[position]);
        holder.tv_chat_username.setText(names[position]);
        holder.img_chat_hero.setImageDrawable(
                matchContext
                        .getResources()
                        .getDrawable(HeroValues.heroIma[heroes[position]]));
    }

    @Override
    public int getItemCount() {
        if (names != null)
            return names.length;
        else
            return 0;
    }

    public void changeData(String[] names, String[] messages, int[] heroes) {
        this.heroes = heroes;
        this.messages = messages;
        this.names = names;
        notifyDataSetChanged();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView img_chat_hero;
        TextView tv_chat_username, tv_chat_message;

        private ChatViewHolder(View itemView) {
            super(itemView);
            img_chat_hero = itemView.findViewById(R.id.img_match_chat_hero);
            tv_chat_username = itemView.findViewById(R.id.tv_chat_username);
            tv_chat_message = itemView.findViewById(R.id.tv_chat_message);
        }
    }
}
