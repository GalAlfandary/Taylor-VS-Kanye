package com.example.taylorvskanye;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.taylorvskanye.Logic.GameLogic;
import com.example.taylorvskanye.utilities.SignalManager;
import com.example.taylorvskanye.utilities.SoundPlayer;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements GameEventListener,MoveCallback {

    private static long DELAY ;
    private static int controlType;

    private ExtendedFloatingActionButton main_FAB_left;
    private ExtendedFloatingActionButton main_FAB_right;
    private MaterialTextView score_lbl;

    private AppCompatImageView main_IMG_taylor_1;
    private AppCompatImageView main_IMG_taylor_2;
    private AppCompatImageView main_IMG_taylor_3;
    private AppCompatImageView main_IMG_taylor_4;
    private AppCompatImageView main_IMG_taylor_5;
    private AppCompatImageView[] main_IMG_hearts;
    private GameLogic gameLogic = new GameLogic();
    private final AppCompatImageView[][] kanyeImages = new AppCompatImageView[gameLogic.getROWS_COUNT()][gameLogic.getLANE_COUNT()];
    private final AppCompatImageView[][] trophyImages = new AppCompatImageView[gameLogic.getROWS_COUNT()][gameLogic.getLANE_COUNT()];
    private final int[] kanyeImageIds = {
            R.id.main_IMG_kanye_1_1, R.id.main_IMG_kanye_1_2, R.id.main_IMG_kanye_1_3, R.id.main_IMG_kanye_1_4, R.id.main_IMG_kanye_1_5,
            R.id.main_IMG_kanye_2_1, R.id.main_IMG_kanye_2_2, R.id.main_IMG_kanye_2_3, R.id.main_IMG_kanye_2_4, R.id.main_IMG_kanye_2_5,
            R.id.main_IMG_kanye_3_1, R.id.main_IMG_kanye_3_2, R.id.main_IMG_kanye_3_3, R.id.main_IMG_kanye_3_4, R.id.main_IMG_kanye_3_5,
            R.id.main_IMG_kanye_4_1, R.id.main_IMG_kanye_4_2, R.id.main_IMG_kanye_4_3, R.id.main_IMG_kanye_4_4, R.id.main_IMG_kanye_4_5,
            R.id.main_IMG_kanye_5_1, R.id.main_IMG_kanye_5_2, R.id.main_IMG_kanye_5_3, R.id.main_IMG_kanye_5_4, R.id.main_IMG_kanye_5_5,
            R.id.main_IMG_kanye_6_1, R.id.main_IMG_kanye_6_2, R.id.main_IMG_kanye_6_3, R.id.main_IMG_kanye_6_4, R.id.main_IMG_kanye_6_5,
    };
    private final int[] trophyImageIds = {
            R.id.main_IMG_trophy_1_1, R.id.main_IMG_trophy_1_2, R.id.main_IMG_trophy_1_3, R.id.main_IMG_trophy_1_4, R.id.main_IMG_trophy_1_5,
            R.id.main_IMG_trophy_2_1, R.id.main_IMG_trophy_2_2, R.id.main_IMG_trophy_2_3, R.id.main_IMG_trophy_2_4, R.id.main_IMG_trophy_2_5,
            R.id.main_IMG_trophy_3_1, R.id.main_IMG_trophy_3_2, R.id.main_IMG_trophy_3_3, R.id.main_IMG_trophy_3_4, R.id.main_IMG_trophy_3_5,
            R.id.main_IMG_trophy_4_1, R.id.main_IMG_trophy_4_2, R.id.main_IMG_trophy_4_3, R.id.main_IMG_trophy_4_4, R.id.main_IMG_trophy_4_5,
            R.id.main_IMG_trophy_5_1, R.id.main_IMG_trophy_5_2, R.id.main_IMG_trophy_5_3, R.id.main_IMG_trophy_5_4, R.id.main_IMG_trophy_5_5,
            R.id.main_IMG_trophy_6_1, R.id.main_IMG_trophy_6_2, R.id.main_IMG_trophy_6_3, R.id.main_IMG_trophy_6_4, R.id.main_IMG_trophy_6_5,
    };
    private SoundPlayer soundPlayer;

    private Timer timer;
    private MoveDetector moveDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retrieveIntentExtras();
        findViews();
        gameLogic = new GameLogic(main_IMG_hearts.length);
        gameLogic.setGameEventListener(this);
        initGameLogic();
        initViews();

        if (controlType == 0) {
            initGameSensors();
        }
    }

    private void retrieveIntentExtras() {
        Intent intent = getIntent();
        DELAY = intent.getLongExtra("SPEED", 0);
        controlType = intent.getIntExtra("CONTROLS", 0);
        Log.d("GAME SETTINGS:", "BUTTONS-1 SENSORS-0:" +controlType);
        Log.d("GAME SETTINGS:", "SPEED:" +DELAY);
    }

    private void updateUi() {
        Log.d("MainActivity", "Updating UI");
        refreshFallingImages();
        if (gameLogic.getCrashes() > 0 && gameLogic.getCrashes() <= 3) {
            main_IMG_hearts[main_IMG_hearts.length - gameLogic.getCrashes()].setVisibility(View.INVISIBLE);
        }
        if (gameLogic.isGameOver()) {
            Log.d("GAME STATUS", "YOU LOSE ");
            changeActivity("Kanye got you! 😭\n Please try again",gameLogic.getScore());
        }
        score_lbl.setText(String.valueOf(gameLogic.getScore()));
    }

    private void changeActivity(String status, int score) {
        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra(ScoreActivity.KEY_STATUS, status);
        intent.putExtra(ScoreActivity.KEY_SCORE, score);
        //Intent intent = new Intent(this, HighScoreActivity.class);
        startActivity(intent);
        finish();
    }

    private void initGameLogic() {
        main_IMG_taylor_1.setVisibility(View.INVISIBLE);
        main_IMG_taylor_2.setVisibility(View.INVISIBLE);
        main_IMG_taylor_4.setVisibility(View.INVISIBLE);
        main_IMG_taylor_5.setVisibility(View.INVISIBLE);

        for (int i = 0; i < gameLogic.getROWS_COUNT(); i++) {
            for (int j = 0; j < gameLogic.getLANE_COUNT(); j++) {
                kanyeImages[i][j].setVisibility(View.INVISIBLE);
                trophyImages[i][j].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void initGameSensors() {
        moveDetector = new MoveDetector(this, this);
        moveDetector.start();
        main_FAB_right.setVisibility(View.INVISIBLE);
        main_FAB_left.setVisibility(View.INVISIBLE);
    }

    private void initViews() {
        score_lbl.setText(String.valueOf(gameLogic.getScore()));
        main_FAB_left.setOnClickListener(v -> {
            gameLogic.moveTaylorLeft();
            updateTaylorPosition();
        });
        main_FAB_right.setOnClickListener(v -> {
            gameLogic.moveTaylorRight();
            updateTaylorPosition();
        });
    }

    private void updateTaylorPosition() {
        main_IMG_taylor_1.setVisibility(gameLogic.getTaylorLane() == 0 ? View.VISIBLE : View.INVISIBLE);
        main_IMG_taylor_2.setVisibility(gameLogic.getTaylorLane() == 1 ? View.VISIBLE : View.INVISIBLE);
        main_IMG_taylor_3.setVisibility(gameLogic.getTaylorLane() == 2 ? View.VISIBLE : View.INVISIBLE);
        main_IMG_taylor_4.setVisibility(gameLogic.getTaylorLane() == 3 ? View.VISIBLE : View.INVISIBLE);
        main_IMG_taylor_5.setVisibility(gameLogic.getTaylorLane() == 4 ? View.VISIBLE : View.INVISIBLE);
    }


    private void refreshFallingImages() {
        int[][] matrix = gameLogic.getMatrix();
        Log.d("MainActivity", "Refreshing Kanye and Trophy images");
        for (int row = 0; row < gameLogic.getROWS_COUNT(); row++) {
            for (int col = 0; col < gameLogic.getLANE_COUNT(); col++) {
                boolean isKanyeVisible = matrix[row][col] == GameLogic.KANYE;
                boolean isTrophyVisible = matrix[row][col] == GameLogic.TROPHY;

                // Assuming you have separate image views for Kanye and trophies
                AppCompatImageView kanyeImageView = kanyeImages[row][col];
                AppCompatImageView trophyImageView = trophyImages[row][col];

                kanyeImageView.setVisibility(isKanyeVisible ? View.VISIBLE : View.INVISIBLE);
                trophyImageView.setVisibility(isTrophyVisible ? View.VISIBLE : View.INVISIBLE);

                if (isKanyeVisible) {
                    Log.d("MainActivity", "Setting Kanye at (" + row + "," + col + ") to VISIBLE");
                } else if (isTrophyVisible) {
                    Log.d("MainActivity", "Setting Trophy at (" + row + "," + col + ") to VISIBLE");
                } else {
                    Log.d("MainActivity", "Setting Kanye and Trophy at (" + row + "," + col + ") to INVISIBLE");
                }
            }
        }
    }

    private void findViews() {
        main_FAB_left = findViewById(R.id.main_FAB_left);
        main_FAB_right = findViewById(R.id.main_FAB_right);
        score_lbl = findViewById(R.id.score_lbl);
        main_IMG_taylor_1 = findViewById(R.id.main_IMG_taylor_1);
        main_IMG_taylor_2 = findViewById(R.id.main_IMG_taylor_2);
        main_IMG_taylor_3 = findViewById(R.id.main_IMG_taylor_3);
        main_IMG_taylor_4 = findViewById(R.id.main_IMG_taylor_4);
        main_IMG_taylor_5 = findViewById(R.id.main_IMG_taylor_5);

        for (int i = 0; i < gameLogic.getROWS_COUNT(); i++) {
            for (int j = 0; j < gameLogic.getLANE_COUNT(); j++) {
                int index = i * gameLogic.getLANE_COUNT() + j;
                kanyeImages[i][j] = findViewById(kanyeImageIds[index]);
                trophyImages[i][j] = findViewById(trophyImageIds[index]);
            }
        }
        main_IMG_hearts = new AppCompatImageView[]{
                findViewById(R.id.main_IMG_heart3),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart1)
        };
    }

    private void startTimer() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    gameLogic.updateFallingObjects();
                    runOnUiThread(() -> updateUi());
                    gameLogic.checkCollision();
                }
            }, 0L, DELAY);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTimer();
        soundPlayer = new SoundPlayer(this);
        soundPlayer.playSound(R.raw.cruel_summer_playback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (moveDetector != null) {
            moveDetector.stop();
        }
        timer.cancel();
        soundPlayer.stopSound();
    }

    @Override
    public void onCollision() {
        runOnUiThread(() -> {
            SignalManager.getInstance().toast("OHH LOOK WHAT YOU MADE ME DO 🐍");
            SignalManager.getInstance().vibrate(400);
        });
    }

    @Override
    public void onTrophyCollected() {
        runOnUiThread(() -> {
            SignalManager.getInstance().vibrate(300);
        });
    }


    @Override
    public void moveRight() {
        gameLogic.moveTaylorRight();
        runOnUiThread(this::updateTaylorPosition);
    }

    @Override
    public void moveLeft() {
        gameLogic.moveTaylorLeft();
        runOnUiThread(this::updateTaylorPosition);
    }

//    @Override
//    public void moveY() {
//        gameLogic.moveTaylorRight(); // or gameLogic.moveTaylorLeft(); depending on how you want to handle X movements
//        runOnUiThread(this::updateTaylorPosition);
//    }
}