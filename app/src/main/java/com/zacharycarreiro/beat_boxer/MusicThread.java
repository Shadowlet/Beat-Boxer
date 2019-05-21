package com.zacharycarreiro.beat_boxer;

import android.app.Activity;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTimestamp;
import android.media.AudioTrack;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

class MusicThread extends Thread {


    public static MusicThread s_instance = null;
    public static MusicThread CreateInstance(Activity cont, Music music) {
        if (s_instance == null) {
            MusicThread mThread = new MusicThread(cont, music);
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

    public Music song;

    private MusicThread(Activity cont, Music music) {
        context = cont;
        song = music;


        minBufferSize = AudioTrack.getMinBufferSize(song.sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);
    }


    public Runnable callback;
    public void DoStart(Runnable r) {
        callback = r;
        //
        start();
    }

    // ??? <-- UNFORGIVEABLE!
    public boolean isDone = false;

    public void run() {

        Log.e("THREAD", "Now playing... " + song.name);

        int i = 0;
        byte[] music = null;
        InputStream is = context.getResources().openRawResource(song.music);


        int fileLength = (int)song.fileSize;
        Log.e("THREAD", ""+fileLength);


        music = new byte[fileLength];
        try {
            i = is.read(music, 0, fileLength);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        at = new AudioTrack(AudioManager.STREAM_MUSIC, song.sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                fileLength*2, AudioTrack.MODE_STATIC);

        Log.e("THREAD", "1111111111111");


        // at.setNotificationMarkerPosition(10);

        at.setNotificationMarkerPosition(1000);
        at.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
            @Override
            public void onPeriodicNotification(AudioTrack track) {
                // nothing to do
            }
            @Override
            public void onMarkerReached(AudioTrack track) {
                Log.e("THREAD", "MARKER?");

                if (isDone) {
                    Log.e("THREAD", "END");

                    at.stop();
                    Log.e("THREAD", "3333333333");
                    at.release();
                    Log.e("THREAD", "44444444444");
                    //
                    //
                    s_instance.interrupt();
                    //
                    s_instance = null;
                }
                else {
                    isDone = true;
                    at.setNotificationMarkerPosition((int)song.frameCount);

                    Log.e("THREAD", "BEGIN");

                    callback.run();
                }
            }
        });


        at.write(music, 0, i);
        //
        at.play();
        at.stop();
        //
        at.setPlaybackRate(song.sampleRate);
        at.setPlaybackHeadPosition(0);
        //
        //
        Log.e("TIMING", "Music played at: "+ Calendar.getInstance().getTimeInMillis());
        at.play();
        //
        Log.e("THREAD", "2222222");
        //
        //
        while (true) {
            if (isInterrupted()) break;
        }
        //
        Log.e("THREAD", "555555555555555");
        //
        return;

        /*
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
                //
                Log.e("Musicing", ""+i);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/


        /*
        try {
            music = new byte[1024];
            //
            do {
                i = is.read(music);
                //
                at.write(music, 0, i);

                Log.e("Musicing", ""+i);
            } while(i != -1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        at.play();
        //
        Log.e("THREAD", "2222222");

        // at.stop();
        Log.e("THREAD", "3333333333");
        // at.release();
        Log.e("THREAD", "44444444444");



        s_instance = null;
        return;
        */
    }
}
