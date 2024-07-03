package com.example.taylorvskanye.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taylorvskanye.Callback_ListItemClicked;
import com.example.taylorvskanye.Models.Player;
import com.example.taylorvskanye.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private final ArrayList<Player> players;
    private Callback_ListItemClicked callbackListItemClicked;

    public PlayerAdapter(ArrayList<Player> players, Callback_ListItemClicked callbackListItemClicked) {
        this.players = players;
        this.callbackListItemClicked = callbackListItemClicked;
    }


    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_player_item, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = getItem(position);

        holder.high_score_item_name.setText(player.getName());
        holder.high_score_item_score.setText(String.valueOf(player.getScore()));
        holder.high_score_item_name.setOnClickListener(v -> {
            if (callbackListItemClicked != null) {
                callbackListItemClicked.listItemClicked(player.getLat(), player.getLon());
            }
        });

    }

    @Override
    public int getItemCount() {
        return players == null ? 0 : players.size();
    }

    public Player getItem(int position) {
        return players.get(position);
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView high_score_item_name;
        private final MaterialTextView high_score_item_score;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            high_score_item_name = itemView.findViewById(R.id.high_score_item_name);
            high_score_item_score = itemView.findViewById(R.id.high_score_item_score);
        }
    }
}
