package com.example.taylorvskanye.utilities;

import com.example.taylorvskanye.Models.Player;
import com.example.taylorvskanye.Models.PlayerList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

public class PlayerListManager {

    public static PlayerList loadPlayerList() {
        Gson gson = new Gson();
        String playerListAsJson = SharePreferencesManagerV3.getInstance().getString("playerList", "");
        PlayerList playerList = gson.fromJson(playerListAsJson, PlayerList.class);
        if (playerList == null) {
            playerList = new PlayerList();
        }
        else {
            sortPlayers(playerList);
        }
        return playerList;
    }

    public static void savePlayerList(PlayerList playerList) {
        Gson gson = new Gson();
        sortPlayers(playerList);
        String playerListAsJson = gson.toJson(playerList);

        SharePreferencesManagerV3.getInstance().putString("playerList", playerListAsJson);
    }

    public static void sortPlayers(PlayerList playerList) {
        ArrayList<Player> players = playerList.getPlayers();
        Collections.sort(players, (player1, player2) -> Integer.compare(player2.getScore(), player1.getScore()));
        if (players.size() > 10) {
            players = new ArrayList<>(players.subList(0, 10));
        }
        playerList.setPlayers(players);
    }
}
