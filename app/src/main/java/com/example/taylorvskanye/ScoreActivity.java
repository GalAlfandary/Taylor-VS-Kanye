package com.example.taylorvskanye;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.taylorvskanye.Models.Player;
import com.example.taylorvskanye.Models.PlayerList;
import com.example.taylorvskanye.utilities.PlayerListManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class ScoreActivity extends AppCompatActivity {
    public static final String KEY_STATUS = "KEY_STATUS";
    public static final String KEY_SCORE = "KEY_SCORE";
    private static final int LOCATION_REQUEST_CODE = 100;
    private MaterialTextView LBL_status;
    private MaterialButton try_again_button;
    private MaterialButton save_score_btn;
    private MaterialTextView score;
    private EditText editText_name;
    private LocationManager locationManager;
    private double lat;
    private double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        findViews();
        initViews();
        setupEditTextListener();
    }

    private void initViews() {
        Intent prevIntent = getIntent();
        String status = prevIntent.getStringExtra(KEY_STATUS);
        int scoreValue = prevIntent.getIntExtra(KEY_SCORE, 0);
        LBL_status.setText(status);
        score.setText(String.valueOf(scoreValue));

        try_again_button.setOnClickListener(view -> {
            Intent intent = new Intent(ScoreActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        });
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        save_score_btn.setOnClickListener(view -> {
            getLocation(scoreValue);
        });
    }

    private void getLocation(int scoreValue) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location!=null){
            lat = location.getLatitude();
            lon = location.getLongitude();
        }
        savePlayerData(scoreValue);
        Log.d("Lat",String.valueOf(lat));
        Log.d("Lon",String.valueOf(lon));
    }

    private void savePlayerData(int scoreValue) {
        PlayerList playerList = PlayerListManager.loadPlayerList();
        Log.d("playerList",playerList.toString());
        playerList.addPlayer(new Player()
                .setName(editText_name.getText().toString())
                .setScore(scoreValue)
                .setLat(lat)
                .setLon(lon)
        );
        PlayerListManager.savePlayerList(playerList);
        highScores();
    }


    private void findViews() {
        LBL_status = findViewById(R.id.LBL_status);
        try_again_button=findViewById(R.id.try_again_button);
        score=findViewById(R.id.score);
        editText_name=findViewById(R.id.editText_name);
        save_score_btn=findViewById(R.id.save_score_btn);
        save_score_btn.setEnabled(false);
    }

    private void highScores() {
        Intent intent = new Intent(this, HighScoreActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupEditTextListener() {
        editText_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //pass
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Enable the button if there is text in the EditText, disable otherwise
                save_score_btn.setEnabled(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //pass
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation(getIntent().getIntExtra(KEY_SCORE, 0));
            }
        }
    }
}