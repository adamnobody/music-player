package com.example.list_all_songs_from_storage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class EQActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eq);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_eq, menu);
        return true;
    }

    public void closeEQ(MenuItem item)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
