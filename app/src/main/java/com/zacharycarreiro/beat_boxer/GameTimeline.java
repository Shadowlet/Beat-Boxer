package com.zacharycarreiro.beat_boxer;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Calendar;

public class GameTimeline extends Entity {


    final static int BEATCOUNT = 4;

    private static GameTimeline _instance = null;
    public static GameTimeline CreateInstance() {
        if (_instance == null) {
            _instance = new GameTimeline();
        }

        return _instance;
    }

    private GameTimeline() {
        Initialize();
    }

    ArrayList<GameSong> gameSongs = new ArrayList<>();



    public void Initialize() {

        GameSong Q;
        Q = new GameSong(Resourcer.allMusics.get("emphasis"));
        //
        // *** The test song
        Q.Add(false, false, false, false); // *** Bar 1
        Q.Add(true, false, true, false); // *** Bar 2
        Q.Add(true, false, true, false); // *** Bar 3
        Q.Add(true, false, true, false); // *** ...
        Q.Add(true, false, true, false);
        Q.Add(true, false, true, false);
        Q.Add(true, false, true, false);
        Q.Add(true, false, true, false);
        Q.Add(true, false, true, false);
        Q.Add(true, false, true, false);
        Q.Add(true, false, true, false);
        Q.Add(true, false, true, false);
        Q.Add(true, false, true, false);
        Q.Add(true, false, true, false);
        Q.Add(true, false, true, false);
        Q.Add(true, false, true, false);
        Q.Add(true, false, true, false);
        Q.Add(true, true, true, true);
        Q.Add(true, true, true, true);
        Q.Add(true, true, true, true);
        Q.Add(true, true, true, true);
        Q.Add(true, true, true, true);
        Q.Add(true, true, true, true);
        Q.Add(true, true, true, true);
        Q.Add(true, true, true, true);
        //
        gameSongs.add(Q);



        Construct(1);
    }




    public GameSong currentSong = null;


    // *** Creates/loads the beat algorithm that is used for playing the game
    public void Construct(int levelID) {
        switch (levelID) {
            case 1:
                currentSong = gameSongs.get(0); // *** The test song
                break;


            default:
                // *** Do nothing.
                currentSong = null;
                break;
        }
    }



    public int gah;
    public int durationOfOneBeat;
    public int durationOfOneBar;

    public int timeOfCurrentBeat;




    public long timeStarted;



    public boolean isPlaying = false;

    public Runnable ThreadCallback = new Runnable() {
        public void run() {
            timeStarted = Calendar.getInstance().getTimeInMillis();
            Log.e("TIMING", "Callback received at: "+Calendar.getInstance().getTimeInMillis());
            //
            timeOfCurrentBeat = 0;

            isPlaying = true;
        }
    };

    // *** Begin the game/timeline
    public void Play() {

        lastCursor = -1;
        beatMoment = 0;
        lastBeatMoment = 0;



        Music currentMusic = currentSong.music;

        float blah = currentMusic.duration / 1000; // *** Convert Micro into Milli
        Log.e("PLAYING", ""+blah);
        blah /= currentMusic.barCount; // *** Get length of ONE bar
        Log.e("PLAYING", ""+blah);
        blah /= BEATCOUNT; // *** Turn into length of one BEAT
        Log.e("PLAYING", ""+blah);

        durationOfOneBeat = (int)blah;
        Log.e("PLAYING", ""+durationOfOneBeat);
        //
        durationOfOneBar = durationOfOneBeat *BEATCOUNT;
        //
        //
        if (currentSong.thread.isAlive()) {
            currentSong.thread.at.stop();
            currentSong.thread.at.setPlaybackHeadPosition(0);
            currentSong.thread.at.play();
            //
            ThreadCallback.run();
        }
        else {
            Log.e("TIMING", "Thread started at: " + Calendar.getInstance().getTimeInMillis());
            currentSong.thread.DoStart(ThreadCallback);
        }
    }
    public void Stop() {
        isPlaying = false;
    }
    public float SongCursor() {
        return ConvertTimeToCursor(TimePassedSinceStarted());
    }
    public long TimePassedSinceStarted() {
        return Calendar.getInstance().getTimeInMillis() - timeStarted;
    }

    public long TimeBetweenNextBeat() {
        if (beatMoment < 0) {
            return 0;
        }


        return ConvertCursorToTime(beatMoment) - ConvertCursorToTime(lastBeatMoment);
    }
    public long TimeRemainingUntilBeat() {
        if (beatMoment < 0) {
            return -1;
        }

        return ConvertCursorToTime(beatMoment) - TimePassedSinceStarted();
    }

    public float ConvertTimeToCursor(long time) {
        return time / (1f * durationOfOneBar);
    }
    public long ConvertCursorToTime(float cursor) {
        return (long)(durationOfOneBar * cursor);
    }


    float lastCursor = -1;
    float beatMoment = 0;
    float lastBeatMoment = 0;

    @Override
    public void Update() {

        if (Inputter.check(MotionEvent.ACTION_DOWN, null)) {
            Play();
        }


        if (isPlaying) {
            gah++;

            float currentCursor = ConvertTimeToCursor(TimePassedSinceStarted());
            //
            if (lastCursor < beatMoment && beatMoment <= currentCursor) {
                lastBeatMoment = beatMoment;
                //
                float tempMoment = -1;

                float cursorBar = (float) Math.floor(currentCursor);
                //
                float cursorBeat = (float) Math.floor((currentCursor - cursorBar) * BEATCOUNT);
                for (int n = 0; n < currentSong.timeBars.size() - 1; n++) {
                    if (n < Math.floor(currentCursor)) continue;


                    TimeBar tb = currentSong.timeBars.get(n);
                    for (int m = 0; m < tb.hasBeat.length; m++) {
                        if (n > cursorBar) {
                        } else {
                            if (m <= cursorBeat) {
                                continue;
                            }
                        }

                        float newCursor = n + (m / ((float) BEATCOUNT));

                        if (currentSong.GetBeat(newCursor)) {
                            tempMoment = newCursor;
                            break;
                        }
                    }
                    //
                    if (tempMoment >= 0) {
                        break;
                    }
                }
                //
                beatMoment = tempMoment;
                //
                if (beatMoment < 0) {
                    Log.e("BEAT", "... Song has ended");
                }
            }
            //
            lastCursor = currentCursor;
        }
    }



    class GameSong {
        private ArrayList<TimeBar> timeBars = new ArrayList<>();

        public Music music;
        MusicThread thread;

        public GameSong(Music mu) {
            music = mu;

            thread = MusicThread.CreateInstance(GameManager.CreateInstance().activity, music);
        }


        public void Add(boolean b1, boolean b2, boolean b3, boolean b4) {
            Add(new TimeBar(b1, b2, b3, b4));
        }
        public void Add(TimeBar tb) {
            if (tb.hasBeat == null) return;
            if (timeBars.size() > 39) return;

            timeBars.add(tb);
        }

        public void Clear() {
            timeBars.clear();
        }



        public TimeBar GetBar(float index) {
            int actualIndex = (int)Math.floor(index);

            return timeBars.get(actualIndex);
        }

        public boolean GetBeat(float timeCursor) {
            int actualIndex;
            actualIndex = (int)Math.floor(timeCursor); // *** We only care about the WHOLE number
            //
            TimeBar tb = timeBars.get(actualIndex);
            //

            timeCursor -= Math.floor(timeCursor);
            timeCursor *= BEATCOUNT; // *** Increase it by the number of possible beats,
            timeCursor = (float)Math.floor(timeCursor); // *** Round it *UP* (because we only care about the NEXT beat, not the current one)


            return tb.hasBeat[(int)timeCursor];
        }
    }
    class TimeBar {
        // *** The "last" beat represents the start of the next bar/measure (meaning the first is not the first beat of the current bar)
        boolean[] hasBeat = new boolean[BEATCOUNT];

        public TimeBar(boolean b1, boolean b2, boolean b3, boolean b4) {
            hasBeat[0] = b1;
            hasBeat[1] = b2;
            hasBeat[2] = b3;
            hasBeat[3] = b4;
        }
    }
}
