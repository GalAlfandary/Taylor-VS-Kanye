package com.example.taylorvskanye;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class MenuActivity extends AppCompatActivity {

    private final long FAST = 300L;
    private final long SLOW = 500L;

    private SwitchMaterial fast_or_slow;
    private SwitchMaterial sensors_or_buttons;
    private MaterialButton start_game_btn;
    private MaterialButton high_scores_btn;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();
        initViews();
        context=this;
    }

    private void initViews() {
        start_game_btn.setOnClickListener(view -> {
            long speed =SLOW; //slow game
            int controls = 1 ; //buttons
            if (fast_or_slow.isChecked()){ //fast game
                speed =FAST;
            }
            if (sensors_or_buttons.isChecked()){ //sensors
                controls = 0;
            }

            startGame(speed,controls);
        });
        high_scores_btn.setOnClickListener(v -> highScores());
    }

    private void findViews() {
        fast_or_slow=findViewById(R.id.fast_or_slow);
        sensors_or_buttons = findViewById(R.id.sensors_or_buttons);
        start_game_btn = findViewById(R.id.start_game_btn);
        high_scores_btn = findViewById(R.id.high_scores_btn);
    }


    private void startGame(long speed, int controls) {
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        intent.putExtra("SPEED", speed);
        intent.putExtra("CONTROLS", controls);
        startActivity(intent);
    }

    private void highScores() {
        Intent intent = new Intent(this, HighScoreActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}