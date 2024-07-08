package com.example.taylorvskanye.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taylorvskanye.Adapters.PlayerAdapter;
import com.example.taylorvskanye.Interface.Callback_ListItemClicked;
import com.example.taylorvskanye.Models.PlayerList;
import com.example.taylorvskanye.R;
import com.example.taylorvskanye.utilities.DataGenerator;
import com.example.taylorvskanye.utilities.PlayerListManager;
import com.example.taylorvskanye.utilities.SharePreferencesManagerV3;
import com.google.gson.Gson;


public class ListFragment extends Fragment {

    private RecyclerView main_LST_players;
    private PlayerList playerList;
    private Callback_ListItemClicked callbackListItemClicked;

    public ListFragment() {
    }

    public void setCallbackListItemClicked(Callback_ListItemClicked callbackListItemClicked) {
        this.callbackListItemClicked = callbackListItemClicked;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(v);
        initViews();
        return v;
    }


    private void initViews() {
        //Load from data manager
//        Gson gson = new Gson();
//        playerList = DataGenerator.generatePlayerList();
//        String playerListAsJson =gson.toJson(playerList);
//        SharePreferencesManagerV3.getInstance().putString("playerList", playerListAsJson);
//        Log.d("JSON",playerListAsJson);

        playerList = PlayerListManager.loadPlayerList();
        PlayerAdapter playerAdapter = new PlayerAdapter(playerList.getPlayers(), callbackListItemClicked);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        main_LST_players.setLayoutManager(linearLayoutManager);
        main_LST_players.setAdapter(playerAdapter);
    }


    public PlayerList getPlayerList() {
        return playerList;
    }

    public void setPlayerList(PlayerList playerList) {
        this.playerList = playerList;
    }


    private void findViews(View v) {
        main_LST_players= v.findViewById(R.id.main_LST_players);
    }
}