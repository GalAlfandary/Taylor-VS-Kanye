package com.example.taylorvskanye.Models;

public class Player {
    private String name;
    private int score;
    private double lat;
    private double lon;

    public String getName() {
        return name;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Player setScore(int score) {
        this.score = score;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Player setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public Player setLon(double lon) {
        this.lon = lon;
        return this;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
