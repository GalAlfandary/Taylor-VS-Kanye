package com.example.taylorvskanye;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textview.MaterialTextView;

public class ScoreActivity extends AppCompatActivity {
    public static final String KEY_STATUS = "KEY_STATUS";
    private MaterialTextView LBL_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        findViews();
        initViews();

    }

    private void initViews() {
        Intent prevIntent = getIntent();
        String status = prevIntent.getStringExtra(KEY_STATUS);
        LBL_status.setText(status);
    }

    private void findViews() {
        LBL_status = findViewById(R.id.LBL_status);

    }
}