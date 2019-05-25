package com.zacharycarreiro.beat_boxer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;

public class ComboCounter extends Actor {

    @Override
    public void Draw(Canvas c, Paint p) {
        super.Draw(c, p);



        GameManager gm = GameManager.CreateInstance();


        ScoreTallier st = gm.gameplayTimeline.scoreTallier;
        float xx, yy;


        if (st != null && gm.gameplayTimeline.isPlaying && !gm.gameplayTimeline.isFinished) {
            xx = Artist.screenWidth * (1 / 10f);
            yy = Artist.screenHeight * (3 / 10f);

            if (st.comboCount >= 3) {
                p.setColor(Color.argb(255, 255, 0, 255));
                p.setTextSize(60);
                c.drawText(String.format("%02d Beat", st.comboCount), xx, yy, p);

                p.setTextSize(45);
                c.drawText(st.ComboMessage(), xx, yy + 60, p);
            }
            else if (st.lastCombo >= 3) {
                p.setColor(Color.argb(255, 0, 255, 255));
                p.setTextSize(60);
                c.drawText(String.format("%02d Beats!", st.lastCombo), xx, yy, p);

                p.setTextSize(45);
                c.drawText(st.FinishMessage(), xx, yy + 60, p);
            }


            xx = gm.obj_meter.x;
            yy = gm.obj_meter.y;

            String str;

            long tallyTiming = (SystemClock.uptimeMillis() - st.tallyTime);
            int alpha = (int) (255 * Math.min(tallyTiming / 300f, 1));

            if (tallyTiming < 1000) {
                String img;
                if (st.lastResult == Meter.HITRESULT_FAST) {
                    p.setColor(Color.argb(alpha, 160, 40, 40));
                    str = "Too Fast!";
                    img = "hit_fast";
                } else if (st.lastResult == Meter.HITRESULT_SLOW) {
                    p.setColor(Color.argb(alpha, 40, 40, 160));
                    str = "Too Slow!";
                    img = "hit_slow";
                } else if (st.lastResult == Meter.HITRESULT_PERFECT) {
                    p.setColor(Color.argb(alpha, 200, 200, 40));
                    str = "Perfect!";
                    img = "hit_perfect";
                } else {
                    p.setColor(Color.argb(alpha, 80, 80, 80));
                    str = "Miss!";
                    img = "hit_miss";
                }


                p.setTextAlign(Paint.Align.CENTER);
                p.setTextSize(90);
                Artist.drawBitmap(img, 0, xx, yy - 100 * (float) (Math.pow(Math.min(tallyTiming / 300f, 1), 2)), 1/2f, 1/2f, 0);
                // c.drawText(str, xx, yy - 100 * (float) (Math.pow(Math.min(tallyTiming / 300f, 1), 2)), p);
                //
                p.setTextAlign(Paint.Align.LEFT);
            }


            if (gm.gameplayTimeline.isPlaying && !gm.gameplayTimeline.isFinished) {
                xx = Artist.screenWidth / 2;
                yy = Artist.screenHeight * (1.5f / 10f);

                p.setColor(Color.argb(255, 80 +(int)Helper.Lerp(120, 0, 1 - gm.gameplayTimeline.TimeRemainingUntilBeat()/((float)gm.gameplayTimeline.TimeBetweenNextBeat()/3f)), 200, 200));
                p.setTextAlign(Paint.Align.CENTER);
                p.setTextSize(160);
                // Helper.TimeOfMilliseconds((long)Math.max(0, (gm.gameplayTimeline.currentSong.music.duration/1000f)-gm.gameplayTimeline.TimePassedSinceStarted()))
                c.drawText("" + (int) (Math.floor((gm.gameplayTimeline.currentSong.music.duration / 1000) - gm.gameplayTimeline.TimePassedSinceStarted()) / 1000f), xx, yy, p);
                //
                p.setTextAlign(Paint.Align.LEFT);
            }
        }
    }
}
