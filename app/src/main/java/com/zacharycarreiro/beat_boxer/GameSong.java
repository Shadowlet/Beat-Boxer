package com.zacharycarreiro.beat_boxer;

import java.util.ArrayList;

public class GameSong {

    public final static int BEATCOUNT = 16;


    public ArrayList<TimeBar> timeBars = new ArrayList<>();

    public Music music;
    MusicThread thread;

    public GameSong(Music mu) {
        music = mu;

        Databaser db = Databaser.CreateInstance();
        if (db.gameRaws_BeatMaps.containsKey(music.name)) {
            CreateFromString(db.gameRaws_BeatMaps.get(music.name));
        }
    }


    public void Prepare() {
        thread = MusicThread.CreateInstance(GameManager.CreateInstance().activity, music);
    }


    public void Add(int b1, int b2, int b3, int b4) {
        Add(new TimeBar(b1, TimeBar.BT_NONE, TimeBar.BT_NONE, TimeBar.BT_NONE,
                b2, TimeBar.BT_NONE, TimeBar.BT_NONE, TimeBar.BT_NONE,
                b3, TimeBar.BT_NONE, TimeBar.BT_NONE, TimeBar.BT_NONE,
                b4, TimeBar.BT_NONE, TimeBar.BT_NONE, TimeBar.BT_NONE));
    }
    public void Add(TimeBar tb) {
        // if (tb.hasBeat == null) return;

        timeBars.add(tb);
    }

    public void Clear() {
        timeBars.clear();
    }


    public void CreateFromString(String str) {
        Databaser db = Databaser.CreateInstance();

        int index;
        String sub;
        while ((index = str.indexOf("\n")) >= 0) {
            sub = str.substring(0, index);
            //
            TimeBar tb = new TimeBar();
            if (sub.length() < 16) {
                for (int n = 0; n < BEATCOUNT/4; n++) {
                    int i = n*4;

                    char chr = sub.charAt(n);
                    int type = db.charToBeat.get(chr);
                    tb.hasBeat[i+0] = type;
                    tb.hasBeat[i+1] = db.charToBeat.get('_');
                    tb.hasBeat[i+2] = db.charToBeat.get('_');
                    tb.hasBeat[i+3] = db.charToBeat.get('_');
                }
            }
            else {
                for (int n = 0; n < BEATCOUNT; n++) {
                    tb.hasBeat[n] = db.charToBeat.get(sub.charAt(n));
                }
            }
            //
            Add(tb);
            //
            str = str.substring(index+1);
        }
    }



    public TimeBar GetBar(float index) {
        int actualIndex = (int)Math.floor(index);

        return timeBars.get(actualIndex);
    }

    public int GetBeat(float timeCursor) {
        int actualIndex;
        actualIndex = (int)Math.floor(timeCursor); // *** We only care about the WHOLE number
        //
        if (actualIndex < 0) return TimeBar.BT_NONE;
        else if (actualIndex >= timeBars.size()) return TimeBar.BT_NONE;
        //
        TimeBar tb = timeBars.get(actualIndex);
        //

        timeCursor -= Math.floor(timeCursor);
        timeCursor *= BEATCOUNT; // *** Increase it by the number of possible beats,
        timeCursor = (float)Math.floor(timeCursor); // *** Round it *UP* (because we only care about the NEXT beat, not the current one)


        return tb.hasBeat[(int)timeCursor];
    }
}
