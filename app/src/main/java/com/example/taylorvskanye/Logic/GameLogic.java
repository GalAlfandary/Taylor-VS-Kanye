package com.example.taylorvskanye.Logic;

import android.util.Log;
import java.util.Arrays;
import java.util.Random;

public class GameLogic {
    private final int LANE_COUNT = 3;
    private final int ROWS_COUNT = 6;
    private int life;
    private int crashes=0;
    private int taylorLane = 1; // Start in the middle lane
    private int[][] matrix = new int[ROWS_COUNT][LANE_COUNT];
    private Random random = new Random();


    public GameLogic() {
    }

    public GameLogic(int life) {
        this.life=life;
    }

    public void moveTaylorLeft() {
        if (taylorLane > 0) {
            taylorLane--;
        } else if (taylorLane==0) {
            setTaylorLane(2);
        }
        Log.d("GameLogic", "Taylor moved left to lane: " + taylorLane);
    }

    public void moveTaylorRight() {
        if (taylorLane < LANE_COUNT - 1) {
            taylorLane++;
        }
        else if (taylorLane == LANE_COUNT-1) {
            setTaylorLane(0);
        }
        Log.d("GameLogic", "Taylor moved right to lane: " + taylorLane);
    }


    public int getTaylorLane() {
        return taylorLane;
    }

    public int getCrashes() {
        return crashes;
    }

    public void setTaylorLane(int taylorLane) {
        this.taylorLane = taylorLane;
    }

    public void updateKanyePositions() {
        for (int row = ROWS_COUNT - 1; row > 0; row--) {
            System.arraycopy(matrix[row - 1], 0, matrix[row], 0, LANE_COUNT);
        }

        Arrays.fill(matrix[0], 0);

        boolean placeKanye = random.nextBoolean();
        if (placeKanye) {
            int randomLane = random.nextInt(LANE_COUNT);
            matrix[0][randomLane] = 1;
        }
        Log.d("GameLogic", "Matrix state after update:");
        for (int row = 0; row < ROWS_COUNT; row++) {
            Log.d("GameLogic", Arrays.toString(matrix[row]));
        }
    }

    public boolean checkCollision() {
        boolean collision = matrix[ROWS_COUNT - 1][taylorLane] == 1;
        Log.d("GameLogic", "Collision check at row " + (ROWS_COUNT - 1) + ", lane " + taylorLane + ": " + collision);
        if (collision){
            increaseCrashes();
            decreaseLife();
            Log.d("GAME STATUS","YOU CRASHED "+getCrashes());
        }
        return collision;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void decreaseLife() {
        life--;
    }

    public void increaseCrashes() {
        crashes++;
    }

    public int getLife() {
        return life;
    }

    public int getLANE_COUNT() {
        return LANE_COUNT;
    }

    public int getROWS_COUNT() {
        return ROWS_COUNT;
    }

    public boolean isGameOver() {
        return life <= 0;
    }

}

