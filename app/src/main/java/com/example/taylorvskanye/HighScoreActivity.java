package com.example.taylorvskanye;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taylorvskanye.Fragments.ListFragment;
import com.example.taylorvskanye.Fragments.MapFragment;
import com.example.taylorvskanye.Models.PlayerList;
import com.example.taylorvskanye.utilities.DataGenerator;
import com.example.taylorvskanye.utilities.SharePreferencesManagerV3;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

public class HighScoreActivity extends AppCompatActivity {

    private FrameLayout main_FRAME_list;
    private FrameLayout main_FRAME_map;
    private MaterialButton back_to_main_btn;

    private ListFragment listFragment;
    private MapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        findViews();
        initViews();

    }

    private void findViews() {
        main_FRAME_list = findViewById(R.id.main_FRAME_list);
        main_FRAME_map = findViewById(R.id.main_FRAME_map);
        back_to_main_btn = findViewById(R.id.back_to_main_btn);
    }

    private void initViews() {
        listFragment = new ListFragment();
        listFragment.setCallbackListItemClicked((lat, lon) -> {
            if (mapFragment!=null){
                mapFragment.zoom(lat, lon);
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_list, listFragment).commit();
        mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_map, mapFragment).commit();
        back_to_main_btn.setOnClickListener(v ->
                backToMain());
    }

    private void backToMain() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}