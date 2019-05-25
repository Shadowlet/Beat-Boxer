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

    private static GameTimeline _instance = null;
    public static GameTimeline CreateInstance() {
        if (_instance == null) {
            _instance = new GameTimeline();
        }

        return _instance;
    }

    private GameTimeline() {}

    ArrayList<GameSong> gameSongs = new ArrayList<>();




    ButtonArea returnButton;

    public void Initialize() {

        GameSong Q;
        //
        Databaser db = Databaser.CreateInstance();
        for (int i = 0; i < db.GamePlaylist.length; i++)
        {
            Q = new GameSong(Resourcer.allMusics.get(db.GamePlaylist[i]));
            gameSongs.add(Q);
        }



        returnButton = new ButtonArea("button_exit", new Rect(Artist.screenWidth-32 -300,Artist.screenHeight-200-32, Artist.screenWidth-32, (Artist.screenHeight-200-32)+160), new Runnable() {
            @Override
            public void run() {
                if (!isExiting) {
                    isExiting = true;
                    exitTime = SystemClock.uptimeMillis();
                }
            }
        });
        returnButton.visible = false;
    }




    public GameSong currentSong = null;


    // *** Creates/loads the beat algorithm that is used for playing the game
    public void Construct(int levelID) {
        currentSong = gameSongs.get(levelID);
        //
        //
        currentSong.Prepare();
        //
        Log.e("Current Song", ""+currentSong.music.title + " ("+currentSong.music.name+")");


        if (currentSong.music.walkmanned != 0) {
            Sounder.Music_Play(currentSong.music.walkmanned, 3000);
        }
        else {
            Sounder.Music_Play(R.raw.specialaffects_walkman, 3000);
        }
    }



    public float durationOfOneBeat;
    public float durationOfOneBar;

    public int timeOfCurrentBeat;




    public long timeStarted;
    public long timeFinished;
    public long exitTime;



    public boolean isPlaying = false;
    public boolean isFinished = false;
    public boolean isDead = false;
    public boolean isExiting = false;


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
        Sounder.Music_Stop();


        lastCursor = -1;
        beatMoment = 0;
        lastBeatMoment = 0;



        Music currentMusic = currentSong.music;

        float blah = currentMusic.duration / 1000f; // *** Convert Micro into Milli
        Log.e("PLAYING", ""+blah);
        blah /= currentMusic.barCount; // *** Get length of ONE bar
        Log.e("PLAYING", ""+blah);
        blah /= GameSong.BEATCOUNT; // *** Turn into length of one BEAT
        Log.e("PLAYING", ""+blah);

        durationOfOneBeat = blah;
        Log.e("PLAYING", ""+durationOfOneBeat);
        //
        durationOfOneBar = durationOfOneBeat *GameSong.BEATCOUNT;
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

        currentSong.thread.interrupt();

        isFinished = true;
        timeFinished = SystemClock.uptimeMillis();

        GameManager.CreateInstance().obj_meter.visible = false;
        GameManager.CreateInstance().obj_meter.myArrow.visible = false;

        if (!isDead) {
            if (currentSong.music.walkmanned != 0) {
                Sounder.Music_Play(currentSong.music.walkmanned, 3000);
            }
            else {
                Sounder.Music_Play(R.raw.specialaffects_walkman, 3000);
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
        float cursorBeat = (float) Math.floor((currentCursor - cursorBar) * GameSong.BEATCOUNT);
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


                float newCursor = n + (m / ((float) GameSong.BEATCOUNT));

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
        isDucking = false;
        if (isDucking && false) {
            isDucking = false;
        }
        else {
            Log.e("BEAT", "Hit Result: " + hitResult);
            if (hitResult == Meter.HITRESULT_MISS) {
                if (beatType == TimeBar.BT_EASY) {
                    // *** No penalty!

                    Sounder.Sound_Play("beat_dodge");
                }
                else if (beatType == TimeBar.BT_HARD) {
                    // *** Unacceptable; You die.
                    isDead = true;
                    Sounder.Sound_Play("lukas_dead");
                    //
                    Stop();


                    Sounder.Sound_Play("beat_hard_miss");
                }
                else {
                    meter.Duck();
                    isDucking = true;

                    Sounder.Sound_Play("beat_dodge");
                }
            } else if (hitResult == Meter.HITRESULT_DODGE) {
                meter.Duck();
                isDucking = true;

                Sounder.Sound_Play("beat_dodge");
            } else {
                if (beatType == TimeBar.BT_EASY) {
                    // "Easy" beats give the same result, as long as you hit
                    ScreenTremble(0.05f, 50);

                    Sounder.Sound_Play("beat_easy");
                }
                else {
                    // *** "Hard" beats require PERFECT timing. If you're off even slightly, you'll get penalized
                    switch (hitResult) {
                        case Meter.HITRESULT_FAST:
                            if (beatType == TimeBar.BT_HARD) {
                                meter.Duck();
                                isDucking = true;

                                Sounder.Sound_Play("beat_dodge");
                            }
                            else {
                                ScreenTremble(0.05f, 50);

                                Sounder.Sound_Play("beat_basic_normal");
                            }


                            break;
                        case Meter.HITRESULT_SLOW:
                            if (beatType == TimeBar.BT_HARD) {
                                meter.Duck();
                                isDucking = true;

                                Sounder.Sound_Play("beat_dodge");
                            }
                            else {
                                ScreenTremble(0.05f, 50);

                                Sounder.Sound_Play("beat_basic_normal");
                            }

                            break;
                        case Meter.HITRESULT_PERFECT:
                            if (beatType == TimeBar.BT_HARD) {
                                ScreenTremble(0.25f, 150);

                                Sounder.Sound_Play("beat_hard");
                            }
                            else {
                                ScreenTremble(0.10f, 100);

                                Sounder.Sound_Play("beat_basic_perfect");
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
        if (!isDucking || true) {
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

        // *** Prevent an extra miss from happening.
        if (isFinished) {
            return;
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


            p.setColor(Color.argb((int)(160*Helper.Longevity(finishTime, 0, 1000)), 0, 40, 160));
            Artist.drawRect(0, 0, Artist.screenWidth/1.5f, Artist.screenHeight);


            p.setColor(Color.argb((int)(255*Helper.Longevity(finishTime, 0, 1000)), 200, 200, 200));
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
            if (finishTime >= 5000) {
                str = "Max Combo: " + scoreTallier.highestCombo;
                c.drawText(str, 100, 300+60*7, p);
            }
            if (finishTime >= 6000) {
                str = "Rank: " + scoreTallier.CalculateRank();
                p.setColor(Color.argb(255, 255, 0, 0));
                c.drawText(str, 100, 300+60*9, p);
            }
            if (finishTime >= 8000) {
                p.setColor(Color.argb(255, 40, 200, 255));

                str = "Lukas Says:";
                c.drawText(str, 100, 300+60*11, p);

                str = "\"" + scoreTallier.RankComment() + "\"";
                c.drawText(str, 100, 300+60*12, p);
            }



            if (finishTime >= 8000) {
                if (!returnButton.visible) {
                    returnButton.visible = true;
                }
            }
        }


        if (isExiting) {
            Artist.SetColor(Color.argb(255, 0, 0, 0));
            Artist.drawRect(0, 0, (int)(Artist.screenWidth*Math.pow(Helper.Longevity(SystemClock.uptimeMillis(), exitTime, 1000), 2)), Artist.screenHeight);

            if (SystemClock.uptimeMillis()-exitTime >= 2000) {
                Sounder.Music_Stop();


                GameManager.CreateInstance().Begin_MainMenu();
            }
        }
    }

    @Override
    public void Discard() {
        super.Discard();


        GameManager gm = GameManager.CreateInstance();

        _instance = null;
        gm.gameplayTimeline = null;

        currentSong.thread.interrupt();


        returnButton.Discard();
    }
}


