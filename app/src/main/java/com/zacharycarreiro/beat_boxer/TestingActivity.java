package com.zacharycarreiro.beat_boxer;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
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



        txtMessage = findViewById(R.id.txtMessage);
        button = findViewById(R.id.button);
        //
        button.setOnClickListener(this);




    }




    @Override
    public void onClick(View v) {


        MusicThread mThread = MusicThread.CreateInstance(this, 100);
        //
        if (!mThread.isAlive()) {
            mThread.start();
        }
        else {
            mThread.at.setPlaybackRate(88200);
            Toast.makeText(this, "111111111", Toast.LENGTH_LONG).show();
        }



        /*
        if (at != null) {
            if (at.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                at.setPlaybackRate(88200);

                return;
            }
        }
         */






    }
}


class MusicThread extends Thread {


    public static MusicThread s_instance = null;
    public static MusicThread CreateInstance(Activity cont, int val) {
        if (s_instance == null) {
            MusicThread mThread = new MusicThread(cont, val);
            //
            s_instance = mThread;
            //
            return mThread;
        }
        else {
            return s_instance;
        }
    }



    public int minBufferSize;
    public AudioTrack at;
    public Activity context;


    private MusicThread(Activity cont, long blah) {
        context = cont;

        minBufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_CONFIGURATION_STEREO, AudioFormat.ENCODING_PCM_16BIT);
    }

    public void run() {
        int i = 0;
        byte[] music = null;
        InputStream is = context.getResources().openRawResource(R.raw.test);

        at = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_CONFIGURATION_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                minBufferSize, AudioTrack.MODE_STREAM);

        // Toast.makeText(context, "111111111", Toast.LENGTH_LONG).show();

        try {
            music = new byte[512];
            //
            at.play();
            //
            while((i = is.read(music)) != -1) {
                if (isInterrupted()) {
                    break;
                }
                //
                at.write(music, 0, i);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        // Toast.makeText(context, "2222222222222", Toast.LENGTH_LONG).show();

        at.stop();
        // Toast.makeText(context, "33333333333333", Toast.LENGTH_LONG).show();
        at.release();
        // Toast.makeText(context, "4444444444444444444444", Toast.LENGTH_LONG).show();



        s_instance = null;
        return;
    }
}
