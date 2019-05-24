package com.zacharycarreiro.beat_boxer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

public class GameTimeline extends Entity {


    final static int BEATCOUNT = 16;

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

    MediaPlayer mp;


    ButtonArea returnButton;

    public void Initialize() {

        GameSong Q;
        //
        Q = new GameSong(Resourcer.allMusics.get("emphasis"));
        gameSongs.add(Q);


        Q = new GameSong(Resourcer.allMusics.get("c_hc"));
        gameSongs.add(Q);



        Construct(1);


        if (currentSong.music.walkmanned != 0) {
            mp = MediaPlayer.create(GameManager.CreateInstance().activity, currentSong.music.walkmanned);
            //
            // mp.seekTo(mp.getDuration()/32);
            mp.setVolume(0.3f, 0.3f);
            mp.setLooping(true);
            mp.seekTo(3000); // *** He's always 3 seconds into the song!
            //
            mp.start();
        }



        returnButton = new ButtonArea("button_exit", new Rect(0+632,Artist.screenHeight-200-32, (0+632)+300, (Artist.screenHeight-200-32)+200), new Runnable() {
            @Override
            public void run() {
                Log.e("AFSDS", "RESETTTTTTTT");
            }
        });
        returnButton.visible = false;
    }




    public GameSong currentSong = null;


    // *** Creates/loads the beat algorithm that is used for playing the game
    public void Construct(int levelID) {
        switch (levelID) {
            case 1:
                currentSong = gameSongs.get(0); // *** The test song
                break;
            case 2:

                currentSong = gameSongs.get(1); // *** Clap (the hard one)
                break;


            default:
                // *** Do nothing.
                currentSong = null;
                break;
        }


        currentSong.Prepare();
        //
        Log.e("Current Song", ""+currentSong.music.name);
    }



    public float durationOfOneBeat;
    public float durationOfOneBar;

    public int timeOfCurrentBeat;




    public long timeStarted;
    public long timeFinished;



    public boolean isPlaying = false;
    public boolean isFinished = false;
    public boolean isDead = false;

    public Runnable ThreadCallback = new Runnable() {
        public void run() {
            timeStarted = SystemClock.uptimeMillis() +currentSong.music.preDelay;
            Log.e("TIMING", "Callback received at: "+SystemClock.uptimeMillis());
            //
            timeOfCurrentBeat = 0;

            isPlaying = true;



            scoreTallier = new ScoreTallier();


            lastBeatMoment = 0;
            //
            PrepareNextBeat();
        }
    };

    // *** Begin the game/timeline
    public void Play() {

        if (mp != null) {
            mp.stop();
        }


        lastCursor = -1;
        beatMoment = 0;
        lastBeatMoment = 0;



        Music currentMusic = currentSong.music;

        float blah = currentMusic.duration / 1000f; // *** Convert Micro into Milli
        Log.e("PLAYING", ""+blah);
        blah /= currentMusic.barCount; // *** Get length of ONE bar
        Log.e("PLAYING", ""+blah);
        blah /= BEATCOUNT; // *** Turn into length of one BEAT
        Log.e("PLAYING", ""+blah);

        durationOfOneBeat = blah;
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
        // isPlaying = false;

        isFinished = true;
        timeFinished = SystemClock.uptimeMillis();

        GameManager.CreateInstance().obj_meter.visible = false;
        GameManager.CreateInstance().obj_meter.myArrow.visible = false;

        if (!isDead) {
            if (currentSong.music.walkmanned != 0) {
                mp = MediaPlayer.create(GameManager.CreateInstance().activity, currentSong.music.walkmanned);
                //
                // mp.seekTo(mp.getDuration()/32);
                mp.setVolume(0.3f, 0.3f);
                mp.setLooping(true);
                mp.seekTo(3000); // *** He's always 3 seconds into the song!
                //
                mp.start();
            }
        }

        Log.e("BEAT", "... Song has ended");

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

                if (currentSong.GetBeat(newCursor) != TimeBar.BT_NONE) {
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


    float lastCursor = -1;
    float beatMoment = 0;
    float lastBeatMoment = 0;
    public int beatType = TimeBar.BT_NONE;

    public void PrepareNextBeat() {
        GameManager gm = GameManager.CreateInstance();
        //
        Meter meter = gm.obj_meter;

        beatMoment = GetMomentOfNextBeat(0);
        hitResult = Meter.HITRESULT_MISS;
        meter.initialMeter = meter.meterValue;
        //
        beatType = currentSong.GetBeat(beatMoment);


        if (beatType == TimeBar.BT_HARD) {
            isDucking = false;
        }
        else if (beatType == TimeBar.BT_EASY) {
            meter.initialMeter = 0;
        }
    }


    long strikeTime;
    int lastResult = Meter.HITRESULT_PERFECT;

    public ScoreTallier scoreTallier;

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
                if (beatType == TimeBar.BT_EASY) {
                    // *** No penalty!
                }
                else if (beatType == TimeBar.BT_HARD) {
                    // *** Unacceptable; You die.
                    isDead = true;
                    //
                    Stop();
                }
                else {
                    meter.Duck();
                    isDucking = true;
                }
            } else if (hitResult == Meter.HITRESULT_DODGE) {
                meter.Duck();
                isDucking = true;
            } else {
                if (beatType == TimeBar.BT_EASY) {
                    // "Easy" beats give the same result, as long as you hit
                    ScreenTremble(0.05f, 50);
                }
                else {
                    // *** "Hard" beats require PERFECT timing. If you're off even slightly, you'll get penalized
                    switch (hitResult) {
                        case Meter.HITRESULT_FAST:
                            if (beatType == TimeBar.BT_HARD) {
                                meter.Duck();
                                isDucking = true;
                            }
                            else {
                                ScreenTremble(0.05f, 50);
                            }
                            break;
                        case Meter.HITRESULT_SLOW:
                            if (beatType == TimeBar.BT_HARD) {
                                meter.Duck();
                                isDucking = true;
                            }
                            else {
                                ScreenTremble(0.05f, 50);
                            }
                            break;
                        case Meter.HITRESULT_PERFECT:
                            if (beatType == TimeBar.BT_HARD) {
                                ScreenTremble(0.25f, 150);
                            }
                            else {
                                ScreenTremble(0.10f, 100);
                            }

                            break;
                    }
                }
            }
        }
        //
        lastResult = hitResult;
        strikeTime = TimePassedSinceStarted();
        //
        scoreTallier.Tally(hitResult);
        //
        PrepareNextBeat();

        // Log.e("BEAT", ""+lastBeatMoment+" and "+beatMoment);
        Helper.DebugMessage("Remain: "+lastBeatMoment + " | Next: "+beatMoment);

        //
        if (beatMoment < 0) {
            Stop();
        }
    }



    public long trembleTime = -1000;
    public float trembleAmount;
    public float trembleLength;
    public void ScreenTremble(float howMuch, int howLong) {
        trembleAmount = howMuch;
        trembleLength = howLong;
        //
        trembleTime = TimePassedSinceStarted();
    }


    boolean isDucking = false;
    int hitResult = Meter.HITRESULT_MISS;


    int runningTime = 0;

    @Override
    public void Update() {
        GameManager gm = GameManager.CreateInstance();
        //
        Meter meter = gm.obj_meter;


        runningTime += 1;

        // ----------------------------------------------------------------------------------------
        // *** Screen Effects!

        if (beatType == TimeBar.BT_HARD) {
            float randx = Helper.random.nextFloat();
            float randy = Helper.random.nextFloat();

            float jist1 = 1-TimeRemainingUntilBeat()/((float)TimeBetweenNextBeat());

            Artist.viewport.width = (int)(Artist.screenWidth *(1-0.4f*jist1));
            Artist.viewport.height = (int)(Artist.screenHeight *(1-0.4f*jist1));
            //
            Artist.viewport.left = (Artist.screenWidth-Artist.viewport.width) +(int)(10*randx);
            Artist.viewport.top = (Artist.screenHeight-Artist.viewport.height)/2 +(int)(10*randy);
        }
        else {
            float jist1 = Helper.Longevity(TimePassedSinceStarted(), trembleTime, trembleLength/2);
            float jist2 = Helper.Longevity(TimePassedSinceStarted(), trembleTime +trembleLength/2, trembleLength/2);
            Artist.viewport.width = (int)(Artist.screenWidth *(1-trembleAmount*(jist1-jist2)));
            Artist.viewport.height = (int)(Artist.screenHeight *(1-trembleAmount*(jist1-jist2)));
            //
            Artist.viewport.left = (Artist.screenWidth-Artist.viewport.width)/2;
            Artist.viewport.top = (Artist.screenHeight-Artist.viewport.height)/2;
        }

        // ========================================================================================



        if (!isPlaying) {
            if (runningTime > Helper.SECOND*4) {
                if (Inputter.check(MotionEvent.ACTION_DOWN, null)) {
                    Play();
                }
            }
            return;
        }


        meter.updateMeter();


        if (isFinished) {
            return;
        }


        float currentCursor = ConvertTimeToCursor(TimePassedSinceStarted());
        //
        if (!isDucking) {
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
        }


        float leewayMoment = beatMoment+(beatMoment-lastBeatMoment)*meter.leeway;
        if (currentCursor >= leewayMoment) {
            RegisterResult();
        }
        //
        lastCursor = currentCursor;
    }


    @Override
    public void Draw(Canvas c, Paint p) {
        float wakeupWide = 0.1f*Helper.Longevity(runningTime, Helper.SECOND, Helper.SECOND/3)
                + 0.9f*Helper.Longevity(runningTime, Helper.SECOND*3, Helper.SECOND*2);

        int targetY = (int)(Artist.screenHeight*0.45f);
        Artist.SetColor(Color.argb(255, 0, 0, 0));
        Artist.drawRect(0, 0, Artist.screenWidth, Helper.Lerp(targetY, 0, wakeupWide));
        Artist.drawRect(0, Helper.Lerp(targetY, Artist.screenHeight, wakeupWide), Artist.screenWidth, Artist.screenHeight);



        if (!isPlaying && runningTime > Helper.SECOND*8) {
            float fadeJist = Helper.Longevity(runningTime, Helper.SECOND*8, Helper.SECOND*2);

            p.setColor(Color.argb((int)(255*fadeJist), 0, 0, 0));
            p.setTextSize(60);
            c.drawText("Tap the screen to begin", 100, 400, p);
        }


        if (isFinished) {
            float finishTime = SystemClock.uptimeMillis()-timeFinished;


            p.setColor(Color.argb((int)(80*Helper.Longevity(finishTime, 0, 1000)), 0, 40, 160));
            Artist.drawRect(0, 0, Artist.screenWidth/1.5f, Artist.screenHeight);


            p.setColor(Color.argb((int)(255*Helper.Longevity(finishTime, 0, 1000)), 0, 0, 0));
            p.setTextSize(60);

            String str = "Results: \n\n";
            c.drawText(str, 100, 300+60*0, p);
            if (finishTime >= 1000) {
                str = "Song: \"" + currentSong.music.title + "\" \n";
                c.drawText(str, 100, 300+60*2, p);
            }
            if (finishTime >= 2000) {
                str = "Duration: " + Helper.TimeOfMilliseconds(timeFinished-timeStarted) + " / "+Helper.TimeOfMilliseconds(currentSong.music.duration/1000)+" \n\n";
                c.drawText(str, 100, 300+60*3, p);
            }
            if (finishTime >= 3000) {
                str = "Perfect: " + scoreTallier.hitCounter[Meter.HITRESULT_PERFECT] + "       Miss: "+(scoreTallier.hitCounter[Meter.HITRESULT_MISS]+scoreTallier.hitCounter[Meter.HITRESULT_DODGE])+" \n";
                c.drawText(str, 100, 300+60*5, p);
            }
            if (finishTime >= 4000) {
                str = "Too Fast: " + scoreTallier.hitCounter[Meter.HITRESULT_FAST] + "      Too Slow: " + scoreTallier.hitCounter[Meter.HITRESULT_SLOW]+" \n\n";
                c.drawText(str, 100, 300+60*6, p);
            }
            if (finishTime >= 6000) {
                str = "Rank: " + scoreTallier.CalculateRank();
                p.setColor(Color.argb(255, 255, 0, 0));
                c.drawText(str, 100, 300+60*8, p);
            }



            if (finishTime >= 8000) {
                if (!returnButton.visible) {
                    returnButton.visible = true;
                }
            }
        }
    }


    class GameSong {
        private ArrayList<TimeBar> timeBars = new ArrayList<>();

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
    class TimeBar {

        public static final int BT_NONE = 0; // *** No beat; Not necessary to press anything
        public static final int BT_BASE = 1; // *** Standard beat; Miss two beats if you don't get it
        public static final int BT_HARD = 2; // *** Game is terminated if you miss this beat
        public static final int BT_EASY = 3; // *** No penalty for missing this beat
        public static final int BT__NUMBER = 4;



        // *** The "last" beat represents the start of the next bar/measure (meaning the first is not the first beat of the current bar)
        public int[] hasBeat = new int[BEATCOUNT];

        public boolean HasBeat(int index) { return (hasBeat[index] != BT_NONE); }

        public TimeBar() {}
        public TimeBar(int b0, int b1, int b2, int b3) {
            hasBeat[0] = b0;
            hasBeat[1] = BT_NONE;
            hasBeat[2] = BT_NONE;
            hasBeat[3] = BT_NONE;
            hasBeat[4] = b1;
            hasBeat[5] = BT_NONE;
            hasBeat[6] = BT_NONE;
            hasBeat[7] = BT_NONE;
            hasBeat[8] = b2;
            hasBeat[9] = BT_NONE;
            hasBeat[10] = BT_NONE;
            hasBeat[11] = BT_NONE;
            hasBeat[12] = b3;
            hasBeat[13] = BT_NONE;
            hasBeat[14] = BT_NONE;
            hasBeat[15] = BT_NONE;
        }
        public TimeBar(int b0, int b1, int b2, int b3, int b4, int b5, int b6, int b7, int b8, int b9, int ba, int bb, int bc, int bd, int be, int bf) {
            hasBeat[0] = b0;
            hasBeat[1] = b1;
            hasBeat[2] = b2;
            hasBeat[3] = b3;
            hasBeat[4] = b4;
            hasBeat[5] = b5;
            hasBeat[6] = b6;
            hasBeat[7] = b7;
            hasBeat[8] = b8;
            hasBeat[9] = b9;
            hasBeat[10] = ba;
            hasBeat[11] = bb;
            hasBeat[12] = bc;
            hasBeat[13] = bd;
            hasBeat[14] = be;
            hasBeat[15] = bf;
        }
    }


    public class ScoreTallier {
        public int hitTotal = 0;
        public int[] hitCounter = new int[Meter.HITRESULT__NUMBER];

        public ScoreTallier() {
        }



        public void Tally(int hitResult) {
            hitCounter[hitResult]++;
            hitTotal++;
        }


        public String CalculateRank() {
            float maxScore = 100 * hitTotal;


            float myScore = 0;
            myScore += 70 *hitCounter[Meter.HITRESULT_FAST];
            myScore += 40 *hitCounter[Meter.HITRESULT_SLOW];
            myScore += 100 *hitCounter[Meter.HITRESULT_PERFECT];
            myScore += 10 *hitCounter[Meter.HITRESULT_DODGE];
            //
            if (myScore >= maxScore * 0.95f) {
                return "SSS";
            }
            else if (myScore >= maxScore * 0.90f) {
                return "SS";
            }
            else if (myScore >= maxScore * 0.85f) {
                return "S";
            }
            else if (myScore >= maxScore * 0.80f) {
                return "A";
            }
            else if (myScore >= maxScore * 0.70f) {
                return "B";
            }
            else if (myScore >= maxScore * 0.60f) {
                return "C";
            }
            else if (myScore >= maxScore * 0.50f) {
                return "D";
            }
            else {
                return "F";
            }
        }
    }
}
