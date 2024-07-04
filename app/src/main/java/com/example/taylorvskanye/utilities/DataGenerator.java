package com.example.taylorvskanye.utilities;

import com.example.taylorvskanye.Models.Player;
import com.example.taylorvskanye.Models.PlayerList;



public class DataGenerator {
    public static PlayerList generatePlayerList() {

        PlayerList playerList = new PlayerList();

        playerList.setName("High scores!")
                .addPlayer(
                        new Player()
                                .setName("Taylor")
                                .setScore(800)
                                .setLat(40.712776)
                                .setLon(-74.005974)
                ).addPlayer(
                        new Player()
                                .setName("Selena")
                                .setScore(180)
                                .setLat(37.774929)
                                .setLon(-122.419416)
                ).addPlayer(
                        new Player()
                                .setName("Travis")
                                .setScore(340)
                                .setLat(34.052235)
                                .setLon(-118.243683)
                ).addPlayer(
                        new Player()
                                .setName("Blake")
                                .setScore(230)
                                .setLat(40.712776)
                                .setLon(-74.005974)
                ).addPlayer(
                        new Player()
                                .setName("Camila")
                                .setScore(420)
                                .setLat(25.761681)
                                .setLon(-80.191788)
                );

        return playerList;
    }
}
