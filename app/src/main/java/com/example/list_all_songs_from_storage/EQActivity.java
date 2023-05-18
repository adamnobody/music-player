package com.example.list_all_songs_from_storage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EQActivity extends AppCompatActivity implements View.OnClickListener
{

    SeekBar seekBar1;
    SeekBar seekBar2;
    SeekBar seekBar3;
    SeekBar seekBar4;
    SeekBar seekBar5;

    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eq);

        seekBar1 = findViewById(R.id.seekBar);
        seekBar2 = findViewById(R.id.seekBar2);
        seekBar3 = findViewById(R.id.seekBar3);
        seekBar4 = findViewById(R.id.seekBar4);
        seekBar5 = findViewById(R.id.seekBar5);

        seekBar1.setProgress(50);
        seekBar2.setProgress(50);
        seekBar3.setProgress(50);
        seekBar4.setProgress(50);
        seekBar5.setProgress(50);

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_eq, menu);
        return true;
    }

    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent();
        Toast.makeText(this, "Настройки сохранены", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}
