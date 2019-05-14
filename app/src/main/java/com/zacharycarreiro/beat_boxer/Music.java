package com.zacharycarreiro.beat_boxer;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import java.io.IOException;

public class Music {
    int music;


    int sampleRate;
    int bitRate; // *** KPS
    long duration;
    long fileSize;
    //
    // long timeInSeconds;

    public Music(int musicIndex) {
        music = musicIndex;
        //
        MediaExtractor mex = new MediaExtractor();
        try {
            AssetFileDescriptor afd = Resourcer.resources.openRawResourceFd(music);
            fileSize = afd.getLength();
            //
            mex.setDataSource(afd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        //
        MediaFormat mf = mex.getTrackFormat(0);
        Log.d("OH!", ""+mf.toString());
        sampleRate = mf.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        duration = mf.getLong(MediaFormat.KEY_DURATION);
        //
        //
        Log.d("OH!", ""+fileSize);
        Log.d("OH!", ""+sampleRate);
        // timeInSeconds = fileSize / sampleRate;
    }
}
