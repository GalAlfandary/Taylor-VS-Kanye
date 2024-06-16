package com.example.taylorvskanye;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taylorvskanye.Logic.GameLogic;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int LANE_COUNT = 3;
    private static final int ROWS_COUNT = 6;
    private static final long DELAY = 600L;
    private ExtendedFloatingActionButton main_FAB_left;
    private ExtendedFloatingActionButton main_FAB_right;
    private AppCompatImageView main_IMG_taylor_center;
    private AppCompatImageView main_IMG_taylor_left;
    private AppCompatImageView main_IMG_taylor_right;
    private AppCompatImageView [] main_IMG_hearts;
    private AppCompatImageView[][] kanyeImages = new AppCompatImageView[ROWS_COUNT][LANE_COUNT] ;
    private int[] kanyeImageIds = {
            R.id.main_IMG_kanye_1_1, R.id.main_IMG_kanye_2_1, R.id.main_IMG_kanye_3_1, R.id.main_IMG_kanye_1_2, R.id.main_IMG_kanye_2_2, R.id.main_IMG_kanye_3_2,
            R.id.main_IMG_kanye_1_3, R.id.main_IMG_kanye_2_3, R.id.main_IMG_kanye_3_3, R.id.main_IMG_kanye_1_4, R.id.main_IMG_kanye_2_4, R.id.main_IMG_kanye_3_4,
            R.id.main_IMG_kanye_1_5, R.id.main_IMG_kanye_2_5, R.id.main_IMG_kanye_3_5, R.id.main_IMG_kanye_1_6, R.id.main_IMG_kanye_2_6, R.id.main_IMG_kanye_3_6
    };
    private Toast toast1;
    private GameLogic gameLogic;
    private Timer timer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        toast1 = Toast.makeText(this, "OHH LOOK WHAT YOU MADE ME DO 🐍" , Toast.LENGTH_SHORT);
        gameLogic = new GameLogic(main_IMG_hearts.length,toast1,vibrator);
        initGameLogic();
        initViews();

    }

    private void updateUi() {
        Log.d("MainActivity", "Updating UI");
        refreshKanyeImages();
        if (gameLogic.getCrashes()>0 && gameLogic.getCrashes()<=3) {
            main_IMG_hearts[main_IMG_hearts.length-gameLogic.getCrashes()].setVisibility(View.INVISIBLE);
        }
        if (gameLogic.isGameOver()) {
            // Handle game over
            Log.d("GAME STATUS","YOU LOSE ");
            changeActivity("Kanye got you! 😭\n Please try again");
        }
//        else {
//            refreshKanyeImages();
//        }



    }

    private void changeActivity(String status) {
        Intent intent = new Intent(this,ScoreActivity.class);
        intent.putExtra(ScoreActivity.KEY_STATUS,status);
        startActivity(intent);
        finish();
    }

    private void initGameLogic() {
        //gameLogic = new GameLogic();
        main_IMG_taylor_left.setVisibility(View.INVISIBLE);
        main_IMG_taylor_right.setVisibility(View.INVISIBLE);

        for (int i = 0; i < ROWS_COUNT; i++) {
            for (int j = 0; j < LANE_COUNT; j++) {
                kanyeImages[i][j].setVisibility(View.INVISIBLE);
            }
        }

    }

    private void initViews() {
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
        main_IMG_taylor_center.setVisibility(gameLogic.getTaylorLane() == 1 ? View.VISIBLE : View.INVISIBLE);
        main_IMG_taylor_left.setVisibility(gameLogic.getTaylorLane() == 0 ? View.VISIBLE : View.INVISIBLE);
        main_IMG_taylor_right.setVisibility(gameLogic.getTaylorLane() == 2 ? View.VISIBLE : View.INVISIBLE);

    }


    private void refreshKanyeImages() {
        int[][] matrix = gameLogic.getMatrix();
        Log.d("MainActivity", "Refreshing Kanye images");
        for (int row = 0; row < ROWS_COUNT; row++) {
            for (int col = 0; col < LANE_COUNT; col++) {
                boolean isVisible = matrix[row][col] == 1;
                kanyeImages[row][col].setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
                Log.d("MainActivity", "Setting Kanye at (" + row + "," + col + ") to " + (isVisible ? "VISIBLE" : "INVISIBLE"));
            }
        }
    }


    private void findViews() {
        main_FAB_left = findViewById(R.id.main_FAB_left);
        main_FAB_right = findViewById(R.id.main_FAB_right);
        main_IMG_taylor_center = findViewById(R.id.main_IMG_taylor_center);
        main_IMG_taylor_right = findViewById(R.id.main_IMG_taylor_right);
        main_IMG_taylor_left = findViewById(R.id.main_IMG_taylor_left);

        for (int i = 0; i < ROWS_COUNT; i++) {
            for (int j = 0; j < LANE_COUNT; j++) {
                int index = i * LANE_COUNT + j;
                kanyeImages[i][j] = findViewById(kanyeImageIds[index]);
            }
        }
        main_IMG_hearts = new AppCompatImageView[] {
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
                    gameLogic.updateKanyePositions();
                    runOnUiThread(() -> updateUi());
                }
            },0L,DELAY);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }
}