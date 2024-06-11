package com.example.taylorvskanye;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taylorvskanye.Logic.GameLogic;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private static final int LANE_COUNT = 3;
    private static final int ROWS_COUNT = 7;
    private static final long DELAY = 1000L;
    private ExtendedFloatingActionButton main_FAB_left;
    private ExtendedFloatingActionButton main_FAB_right;
    private AppCompatImageView main_IMG_taylor;
    private AppCompatImageView [] main_IMG_hearts;
    private AppCompatImageView[][] kanyeImages = new AppCompatImageView[ROWS_COUNT][LANE_COUNT] ;
    private int[] kanyeImageIds = {
            R.id.main_IMG_kanye_1_1, R.id.main_IMG_kanye_1_2, R.id.main_IMG_kanye_1_3, R.id.main_IMG_kanye_1_4, R.id.main_IMG_kanye_1_5, R.id.main_IMG_kanye_1_6, R.id.main_IMG_kanye_1_7,
            R.id.main_IMG_kanye_2_1, R.id.main_IMG_kanye_2_2, R.id.main_IMG_kanye_2_3, R.id.main_IMG_kanye_2_4, R.id.main_IMG_kanye_2_5, R.id.main_IMG_kanye_2_6, R.id.main_IMG_kanye_2_7,
            R.id.main_IMG_kanye_3_1, R.id.main_IMG_kanye_3_2, R.id.main_IMG_kanye_3_3, R.id.main_IMG_kanye_3_4, R.id.main_IMG_kanye_3_5, R.id.main_IMG_kanye_3_6, R.id.main_IMG_kanye_3_7
    };

    private GameLogic gameLogic;



    private Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateUi();
            handler.postDelayed(this,DELAY);

        }
    };

    private void updateUi() {
        gameLogic.updateKanyePositions();
        refreshKanyeImages();

        if (gameLogic.checkCollision()) {
            gameLogic.increaseCrashes();
            gameLogic.decreaseLife();
            Log.d("GAME STATUS","YOU CRASHED"+gameLogic.getCrashes());
            main_IMG_hearts[main_IMG_hearts.length-gameLogic.getCrashes()].setVisibility(View.INVISIBLE);
            if (gameLogic.isGameOver()) {
                // Handle game over
                Log.d("GAME STATUS","YOU LOSE ");
                changeActivity("GAME OVER \n😭");
                handler.removeCallbacks(runnable);
            }
            else {
                // Reset Kanye positions
                for (int lane = 0; lane < LANE_COUNT; lane++) {
                    gameLogic.resetKanyePosition(lane);
                }
            }
        }
    }

    private void changeActivity(String status) {
        Intent intent = new Intent(this,ScoreActivity.class);
        intent.putExtra(ScoreActivity.KEY_STATUS,status);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();
        gameLogic = new GameLogic(main_IMG_hearts.length);
        initGameLogic();
    }

    private void initGameLogic() {
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        gameLogic = new GameLogic();
        handler.postDelayed(runnable, DELAY);
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
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) main_IMG_taylor.getLayoutParams();
        int laneWidth = getResources().getDisplayMetrics().widthPixels / LANE_COUNT;
        params.leftMargin = laneWidth * gameLogic.getTaylorLane() + laneWidth / 2 - main_IMG_taylor.getWidth() / 2;
        main_IMG_taylor.setLayoutParams(params);
//        if (gameLogic.getTaylorLane()==0){
//            main_IMG_taylor.
//        }
    }


    private void refreshKanyeImages() {
        int[][] matrix = gameLogic.getMatrix();
        for (int row = 0; row < ROWS_COUNT; row++) {
            for (int col = 0; col < LANE_COUNT; col++) {
                if (matrix[row][col] == 1) {
                    kanyeImages[row][col].setVisibility(View.VISIBLE);
                } else {
                    kanyeImages[row][col].setVisibility(View.INVISIBLE);
                }
            }
        }
    }


    private void findViews() {
        main_FAB_left = findViewById(R.id.main_FAB_left);
        main_FAB_right = findViewById(R.id.main_FAB_right);
        main_IMG_taylor = findViewById(R.id.main_IMG_taylor);

        for (int i = 0; i < ROWS_COUNT; i++) {
            for (int j = 0; j < LANE_COUNT; j++) {
                int index = i * LANE_COUNT + j;
                kanyeImages[i][j] = findViewById(kanyeImageIds[index]);
            }
        }
        main_IMG_hearts = new AppCompatImageView[] {
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, DELAY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}