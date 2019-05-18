package com.zacharycarreiro.beat_boxer;

import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

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
        Q.Add(true, false, true, false);
        Q.Add(true, false, true, false);
        Q.Add(true, false, true, false);
        Q.Add(true, false, true, false);
        Q.Add(true, false, true, false);
        Q.Add(true, false, true, false);
        Q.Add(true, false, true, false);
        Q.Add(true, false, true, false);
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



    public int durationOfOneBeat;
    public int durationOfOneBar;

    public int timeOfCurrentBeat;




    public long timeStarted;



    public boolean isPlaying = false;


    public final int AUDIOOFFSET = 250;

    public Runnable ThreadCallback = new Runnable() {
        public void run() {
            timeStarted = SystemClock.uptimeMillis() +AUDIOOFFSET;
            Log.e("TIMING", "Callback received at: "+SystemClock.uptimeMillis());
            //
            timeOfCurrentBeat = 0;

            isPlaying = true;


            lastBeatMoment = 0;
            beatMoment = GetMomentOfNextBeat(0);
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
            Log.e("TIMING", "Thread started at: " + SystemClock.uptimeMillis());
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
        return SystemClock.uptimeMillis() - timeStarted;
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


    public float GetMomentOfNextBeat(int skip) {
        float currentCursor = lastBeatMoment; // ConvertTimeToCursor(TimePassedSinceStarted());
        float tempMoment = -1;

        float cursorBar = (float) Math.floor(currentCursor);
        //
        float cursorBeat = (float) Math.floor((currentCursor - cursorBar) * BEATCOUNT);
        for (int n = 0; n < currentSong.timeBars.size(); n++) {
            if (n < Math.floor(currentCursor)) continue;


            TimeBar tb = currentSong.timeBars.get(n);
            for (int m = 0; m < tb.hasBeat.length; m++) {
                if (n > cursorBar) {
                } else {
                    if (m <= cursorBeat) {
                        continue;
                    }
                }

                if (skip > 0) {
                    skip -= 1;
                    continue;
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


        return tempMoment;
    }

    public float ConvertTimeToCursor(long time) {
        return time / (1f * durationOfOneBar);
    }
    public long ConvertCursorToTime(float cursor) {
        return (long)(durationOfOneBar * cursor);
    }


    public void RegisterResult() {
        Log.e("BEAT", "NEXT BEAT");


        GameManager gm = GameManager.CreateInstance();
        //
        Meter meter = gm.obj_meter;


        lastBeatMoment = beatMoment;
        //
        if (isDucking) {
            isDucking = false;
        }
        else {
            Log.e("BEAT", "Hit Result: " + hitResult);
            if (hitResult == Meter.HITRESULT_MISS) {
                // *** On "EASY", you automatically duck.
                // *** On "HARD", you ded.
                meter.Duck();
                isDucking = true;
            } else if (hitResult == Meter.HITRESULT_DODGE) {
                meter.Duck();
                isDucking = true;
            } else {
                switch (hitResult) {
                    case Meter.HITRESULT_FAST:
                        break;
                    case Meter.HITRESULT_SLOW:
                        break;
                    case Meter.HITRESULT_PERFECT:
                        break;
                }
            }
        }
        //
        beatMoment = GetMomentOfNextBeat(0);
        hitResult = Meter.HITRESULT_MISS;
        meter.initialMeter = meter.meterValue;
        // Log.e("BEAT", ""+lastBeatMoment+" and "+beatMoment);
        Helper.DebugMessage("Remain: "+lastBeatMoment + " | Next: "+beatMoment);

        //
        if (beatMoment < 0) {
            Log.e("BEAT", "... Song has ended");
        }
    }


    float lastCursor = -1;
    float beatMoment = 0;
    float lastBeatMoment = 0;


    boolean isDucking = false;
    int hitResult = Meter.HITRESULT_MISS;

    @Override
    public void Update() {

        if (!isPlaying) {
            if (Inputter.check(MotionEvent.ACTION_DOWN, null)) {
                Play();
            }
            return;
        }


        GameManager gm = GameManager.CreateInstance();
        //
        Meter meter = gm.obj_meter;
        meter.updateMeter();


        float currentCursor = ConvertTimeToCursor(TimePassedSinceStarted());
        //
        if (Inputter.check(MotionEvent.ACTION_DOWN, null)) {
            if (hitResult == Meter.HITRESULT_MISS) {
                int tempResult = meter.PunchBag();
                hitResult = tempResult;
                //
                if (hitResult != Meter.HITRESULT_MISS) {
                    RegisterResult();
                }
            }
        }


        float leewayMoment = beatMoment+(beatMoment-lastBeatMoment)*meter.leeway;
        if (currentCursor >= leewayMoment) {
            RegisterResult();
        }
        //
        lastCursor = currentCursor;
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
            // if (tb.hasBeat == null) return;

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
