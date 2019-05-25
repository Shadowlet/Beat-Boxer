package com.zacharycarreiro.beat_boxer;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.io.IOException;
import java.util.HashMap;

public class Sounder {

    /*
    private static Sounder _instance = null;
    public static Sounder CreateInstance() {
        if (_instance == null) {
            _instance = new Sounder();
        }

        return _instance;
    }

    private Sounder() {}
    */



    public static MediaPlayer mp = null;

    public static MediaPlayer Music_Play(int songID, int seekTo) {
        if (mp != null) {
            mp.stop();
            //
            mp.release();
            //
            mp = null;
        }

        if (songID >= 0) {
            mp = MediaPlayer.create(GameManager.CreateInstance().activity, songID);
            //
            // mp.seekTo(mp.getDuration()/32);
            mp.setVolume(0.3f, 0.3f);
            mp.setLooping(true);
            mp.seekTo(seekTo); // *** He's always 3 seconds into the song!
            //
            mp.start();
        }


        return mp;
    }

    public static void Music_Stop() {
        mp.stop();
    }

    public static boolean Music_IsPlaying() {
        return (mp != null) && (mp.isPlaying());
    }


    public static HashMap<String, Integer> allSounds = new HashMap<>();
    public static SoundPool sp;


    public static void Sound_Play(String id) {
        if (allSounds.containsKey(id)) {
            sp.play(allSounds.get(id), 1, 1, 0, 0, 1);
        }
    }

    public static void Initialize() {
        //Sound code
        sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        try {
            //Create objects of the 2 required classes
            AssetManager assetManager = GameManager.CreateInstance().activity.getAssets();


            allSounds.put("menu_select", sp.load(assetManager.openFd("menu_select.wav"), 0));
            allSounds.put("beat_basic_normal", sp.load(assetManager.openFd("beat_basic_normal.wav"), 0));
            allSounds.put("beat_basic_perfect", sp.load(assetManager.openFd("beat_basic_perfect.wav"), 0));
            allSounds.put("beat_hard", sp.load(assetManager.openFd("beat_hard.wav"), 0));
            allSounds.put("beat_dodge", sp.load(assetManager.openFd("beat_dodge.wav"), 0));
            allSounds.put("beat_hard_miss", sp.load(assetManager.openFd("beat_hard_miss.wav"), 0));
            allSounds.put("beat_easy", sp.load(assetManager.openFd("beat_easy.wav"), 0));
            allSounds.put("lukas_dead", sp.load(assetManager.openFd("lukas_dead.wav"), 0));
        } catch (IOException e) {
            //catch exceptions here
        }
    }

}
