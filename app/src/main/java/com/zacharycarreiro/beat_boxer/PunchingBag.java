package com.zacharycarreiro.beat_boxer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class PunchingBag extends DisplayableActor {

    PunchingGuy myGuy;
    Meter myMeter;

    PunchingBag() {
        super("punchingBag");


        x = 0;
        y = 0;

        image_rotate = 0;
    }


    @Override
    public void Update() {
        super.Update();


        if (Inputter.check(MotionEvent.ACTION_DOWN, new Rect(0, 0, 100, 100))) {
            image_yscale += 1;
        }

        updateMeter();
    }


    @Override
    public void Draw(Canvas c, Paint p) {
        super.Draw(c, p);
    }

    public void updateMeter(){
        x = Artist.screenWidth *0.75f;
        y = Artist.screenHeight/6 +64;


        myGuy.x = x - 64 * 5;
        myGuy.y = y + 64 * 9;


        GameTimeline gtl = GameTimeline.CreateInstance();

        if (gtl.beatType == GameTimeline.TimeBar.BT_EASY) {
            image_rotate = -myMeter.visualValue * 10;
        }
        else if (gtl.beatType == GameTimeline.TimeBar.BT_HARD) {
            image_rotate = -myMeter.visualValue * 90;
        }
        else {
            image_rotate = -myMeter.visualValue * 50;
        }


        if (gtl.isPlaying) {
            if (gtl.isDucking) {
                myGuy.SetAnim("duck");
            }
            else {
                if (gtl.TimePassedSinceStarted()-gtl.strikeTime < 300) {
                    switch (gtl.lastResult) {
                        case Meter.HITRESULT_MISS:
                        case Meter.HITRESULT_DODGE:
                            myGuy.SetAnim("duck");
                            break;

                        case Meter.HITRESULT_FAST:
                        case Meter.HITRESULT_SLOW:
                        case Meter.HITRESULT_PERFECT:
                            myGuy.SetAnim("punch");
                            break;

                        default:
                            myGuy.SetAnim("ready");
                    }
                }
                else {
                    myGuy.SetAnim("ready");
                }
            }
        }
        else {
            myGuy.SetAnim("idle");
        }
    }
}
