package com.example.taylorvskanye.Logic;

import android.widget.ImageView;

import java.util.Arrays;
import java.util.Random;

public class GameLogic {
    private static final int LANE_COUNT = 3;
    private static final int ROWS_COUNT = 7;
    private int life;
    private int crashes=0;
    private int taylorLane = 1; // Start in the middle lane
    private int[][] matrix = new int[ROWS_COUNT][LANE_COUNT];
    private Random random = new Random();

    public GameLogic() {
        this(3);
    };
    public GameLogic(int life) {
        this.life=life;
    }

    public void moveTaylorLeft() {
        if (taylorLane > 0) {
            taylorLane--;
        } else if (taylorLane==0) {
            taylorLane=2;
        }
    }

    public void moveTaylorRight() {
        if (taylorLane < LANE_COUNT - 1) {
            taylorLane++;
        }
        else if (taylorLane == LANE_COUNT-1) {
            taylorLane=0;
        }
    }


    public int getTaylorLane() {
        return taylorLane;
    }

    public int getCrashes() {
        return crashes;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void setCrashes(int crashes) {
        this.crashes = crashes;
    }

    public void setTaylorLane(int taylorLane) {
        this.taylorLane = taylorLane;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public void updateKanyePositions() {
        // Shift all rows down
        for (int row = ROWS_COUNT - 1; row > 0; row--) {
            System.arraycopy(matrix[row - 1], 0, matrix[row], 0, LANE_COUNT);
        }

        // Clear the top row
        Arrays.fill(matrix[0], 0);

        // Randomly select a lane for Kanye image placement in the top row
        int randomLane = random.nextInt(LANE_COUNT);
        matrix[0][randomLane] = 1;
    }

    public boolean checkCollision() {
        return matrix[ROWS_COUNT - 1][taylorLane] == 1;
    }

    public void resetKanyePosition(int lane) {
        for (int row = 0; row < ROWS_COUNT; row++) {
            matrix[row][lane] = 0;
        }
        matrix[0][lane] = 1;
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

    public boolean isGameOver() {
        return life <= 0;
    }
}

