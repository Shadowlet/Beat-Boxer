package com.zacharycarreiro.beat_boxer;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class TestingActivity extends Activity implements View.OnClickListener {



    TextView txtMessage;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);


        //
        Artist.Initialize(getWindowManager().getDefaultDisplay(), getResources().getDisplayMetrics());
        //
        Resourcer.Setup(getResources());


        txtMessage = findViewById(R.id.txtMessage);
        button = findViewById(R.id.button);
        //
        button.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.button:
                Music m = Resourcer.allMusics.get("test");
                Log.e("OH!", ""+m.music);

                MusicThread mThread = MusicThread.CreateInstance(this, m);
                //
                if (!mThread.isAlive()) {
                    mThread.start();
                }
                else {

                    if (mThread.at.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                        mThread.at.pause();
                        // mThread.at.setPlaybackHeadPosition(1800000);
                    }
                    else {
                        mThread.at.setPlaybackRate(mThread.song.CalculateSampleRate() *10);
                        //
                        mThread.at.play();
                    }

                    // mThread.at.setPlaybackRate(88200);
                    Toast.makeText(this, "111111111", Toast.LENGTH_LONG).show();
                }
                break;


            case R.id.button2:

                break;
        }

    }
}


