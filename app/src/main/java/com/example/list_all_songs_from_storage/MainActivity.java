package com.example.list_all_songs_from_storage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private static final int MY_PERMISSION_REQUEST = 1;
    MediaPlayer mp = new MediaPlayer();
    ImageButton playButton, pauseButton, nextButton, previousButton;
    TextView songLabel;
    Boolean musicIsPlaying = false;
    ArrayList<String> stringArrayList;
    int currentSong;
    String locationsString = "";
    String songPlay = "";
    String[] musicParts;
    ListView musicListView;
    ArrayAdapter<String> stringArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);

        songLabel = findViewById(R.id.songLabel);

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED)
        {

            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }

        } else {
            doStuff();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void doStuff()
    {
        musicListView = (ListView) findViewById(R.id.musicListView);
        stringArrayList = new ArrayList<>();
        getMusic();
        stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringArrayList);
        musicListView.setAdapter(stringArrayAdapter);

        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (mp.getCurrentPosition() != position)
                {
                    mp.stop();
                    playMusic(position);
                }
                else if (mp.getCurrentPosition() == position)
                {
                    mp.pause();
                    playButton.setEnabled(true);
                    pauseButton.setEnabled(false);
                }
                else
                {
                    playMusic(position);
                    musicIsPlaying = true;
                }
                currentSong = position;
            }
        });
    }

    public void openEQ(MenuItem item)
    {
        if (musicIsPlaying)
        {
            Intent intent = new Intent(this, EQActivity.class);
            startActivityForResult(intent, 1);
        }
        else
        {
            Toast.makeText(this, "Сначала выберите трек из списка", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
        {
            return;
        }
    }

    public void play(View view)
    {
        if (!musicIsPlaying)
        {
            Toast.makeText(this, "Сначала выберите трек из списка", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mp.start();
            playButton.setEnabled(false);
            pauseButton.setEnabled(true);
        }
    }

    public void pause(View view)
    {
        mp.pause();
        playButton.setEnabled(true);
        pauseButton.setEnabled(false);
    }

    public void nextSong(View view)
    {
        if (!musicIsPlaying)
        {
            Toast.makeText(this, "Сначала выберите трек из списка", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mp.stop();
            playMusic(currentSong + 1);
            pauseButton.setEnabled(true);
            playButton.setEnabled(false);
        }
    }

    public void previousSong(View view)
    {
        if (!musicIsPlaying)
        {
            Toast.makeText(this, "Сначала выберите трек из списка", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mp.stop();
            playMusic(currentSong - 1);
            pauseButton.setEnabled(true);
            playButton.setEnabled(false);
        }
    }

    private void playMusic(Integer position)
    {
        currentSong = position;
        if (!mp.isPlaying())
        {
            Toast.makeText(this, "Играем...", Toast.LENGTH_SHORT).show();
            mp.start();
            musicIsPlaying = true;
        }
        else
        {
            mp.pause();
        }
        try
        {
            mp = new MediaPlayer();
            mp.setDataSource(musicParts[position]);
            mp.prepare();
            mp.start();
            musicIsPlaying = true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void getMusic()
    {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst())
        {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            int counter = 0;

            do
            {
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentLocation = songCursor.getString(songLocation);

                stringArrayList.add(currentTitle + "\n" + currentArtist);
                locationsString += currentLocation;
                locationsString += "|";
                counter++;
            } while (songCursor.moveToNext());
            musicParts = locationsString.split("\\|");
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSION_REQUEST:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    {
                        Toast.makeText(this, "Разрешение получено!", Toast.LENGTH_SHORT).show();
                        doStuff();
                    }
                    else
                    {
                        Toast.makeText(this, "Ошибка при получении разрешения!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    return;
                }
            }
        }
    }
}
