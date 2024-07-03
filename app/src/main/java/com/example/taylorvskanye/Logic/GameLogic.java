package com.example.taylorvskanye.Logic;

import android.util.Log;

import com.example.taylorvskanye.GameEventListener;

import java.util.Arrays;
import java.util.Random;

public class GameLogic {
    private static final int ANSWER_POINTS = 10;
    public static final int KANYE = 1;
    public static final int TROPHY = 2;

    private final int LANE_COUNT = 5;
    private final int ROWS_COUNT = 6;
    private int life;
    private int crashes = 0;
    private int score = 0;
    private int taylorLane = 2; // Start in the middle lane
    private int[][] matrix = new int[ROWS_COUNT][LANE_COUNT];
    private Random random = new Random();
    private GameEventListener gameEventListener;


    public GameLogic() {
        this(3);
    }

    public GameLogic(int life) {
        this.life = life;
    }

    public void moveTaylorLeft() {
        if (taylorLane > 0) {
            taylorLane--;
        } else if (taylorLane == 0) {
            setTaylorLane(4);
        }
        Log.d("GameLogic", "Taylor moved left to lane: " + taylorLane);
    }

    public void moveTaylorRight() {
        if (taylorLane < LANE_COUNT - 1) {
            taylorLane++;
        } else if (taylorLane == LANE_COUNT - 1) {
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

    public void setGameEventListener(GameEventListener gameEventListener) {
        this.gameEventListener = gameEventListener;
    }

    public void updateFallingObjects() {
        for (int row = ROWS_COUNT - 1; row > 0; row--) {
            System.arraycopy(matrix[row - 1], 0, matrix[row], 0, LANE_COUNT);
        }
        Arrays.fill(matrix[0], 0);

        for (int lane = 0; lane < LANE_COUNT; lane++) {
            int chance = random.nextInt(100); // 0 to 99
            if (chance < 25) {
                int randomObject = random.nextInt(3); // 0: nothing, 1 for KANYE, 2 for TROPHY
                if (randomObject == 1) {
                    matrix[0][lane] = KANYE;
                } else if (randomObject == 2) {
                    matrix[0][lane] = TROPHY;
                }
            }
        }
        Log.d("GameLogic", "Matrix state after update:");
        for (int row = 0; row < ROWS_COUNT; row++) {
            Log.d("GameLogic", Arrays.toString(matrix[row]));
        }
    }

    public boolean checkCollision() {
        int currentCell = matrix[ROWS_COUNT - 1][taylorLane];
        boolean collision = currentCell == KANYE;
        boolean collectedTrophy = currentCell == TROPHY;
        Log.d("GameLogic", "Collision check at row " + (ROWS_COUNT - 1) + ", lane " + taylorLane + ": " + collision);

        if (collision) {
            increaseCrashes();
            decreaseLife();
            clearCell(ROWS_COUNT - 1, taylorLane);
            Log.d("GAME STATUS", "YOU CRASHED " + getCrashes());
            if (gameEventListener != null) {
                gameEventListener.onCollision();
            }
        } else if (collectedTrophy) {
            score += ANSWER_POINTS;
            clearCell(ROWS_COUNT - 1, taylorLane);
            Log.d("GAME STATUS", "You collected a trophy! " + getScore());
            if (gameEventListener != null) {
                gameEventListener.onTrophyCollected();
            }
        }
        return collision;
    }

    private void clearCell(int row, int col) {
        matrix[row][col] = 0;
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

    public int getScore() {
        return score;
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

