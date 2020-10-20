package com.moviles.tic_tac_toe.Presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.moviles.tic_tac_toe.R;
import com.moviles.tic_tac_toe.Util.Constanst;

public class DificultyActivity extends AppCompatActivity {
    private Button easy;
    private Button hard;
    private Button expert;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dificulty);
        easy = findViewById(R.id.easy);
        hard = findViewById(R.id.hard);
        expert = findViewById(R.id.expert);
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constanst.dificulty = 0;
                Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                startActivityForResult(intent,0);

            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constanst.dificulty = 1;
                Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                startActivityForResult(intent,0);

            }
        });
        expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constanst.dificulty = 2;
                Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                startActivityForResult(intent,0);

            }
        });
    }
}
