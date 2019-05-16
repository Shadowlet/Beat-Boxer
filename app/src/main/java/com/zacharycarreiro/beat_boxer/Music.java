package com.zacharycarreiro.beat_boxer;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioFormat;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class Music {
    int music;


    // *** Resourcer information
    int bitRate;
    int barCount;
    int _tempo;
    int beatDensity;
    //
    long fileSize;
    //
    int sampleRate; // Samples per Second
    long duration; // Length of song (in Microseconds-- Not to be mistaken for "Milliseconds")
    //
    long frameCount; // Total number of samples

    //
    // long timeInSeconds;

    public Music(int musicIndex) {
        music = musicIndex;
        //
        MediaExtractor mex = new MediaExtractor();
        // MediaMetadataRetriever mmdr = new MediaMetadataRetriever();
        // 1,860,454
        try {
            AssetFileDescriptor afd = Resourcer.resources.openRawResourceFd(music);
            fileSize = afd.getDeclaredLength(); // afd.getDeclaredLength();
            //
            mex.setDataSource(afd);


            // mmdr.setDataSource(afd.getFileDescriptor());
            // mmdr.extractMetadata(MediaMetadataRetriever.)
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        //

        MediaFormat mf = mex.getTrackFormat(0);
        Log.e("OH!", ""+mf.toString());
        sampleRate = mf.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        duration = mf.getLong(MediaFormat.KEY_DURATION);
        //
        // frameCount = sampleRate *(duration / 1000000); // Convert it into Seconds to determine the amount of samples!
        // *** I'm assuming everything has two channels (Stereo) and works by 2 bits per sample
        frameCount = (fileSize - 44) / (2 * 2);
        //
        //
        Log.d("OH!", ""+fileSize);
        Log.d("OH!", ""+sampleRate);



        SetTempo(_tempo);
    }



    private float tempo;
    public void SetTempo(float val) {
        tempo = val;
    }
    public float GetTempo() {
        return tempo;
    }


    public int CalculateSampleRate() {
        return (int)(sampleRate * (tempo / _tempo));
    }
}
