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



        x = Artist.screenWidth *0.75f;
        y = Artist.screenHeight/6 +64;
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


        if (gtl.isDead) {
            myGuy.loopingMode = LOOPMODE_STOP;
            myGuy.SetAnim("fly");
            myGuy.frame_speed = 12/Helper.SECOND;

            myGuy.x -= 100/Helper.SECOND;
            myGuy.y -= 30/Helper.SECOND;
            myGuy.image_rotate += 120/Helper.SECOND;
        }
        else if (gtl.isFinished) {
            myGuy.loopingMode = LOOPMODE_STOP;
            myGuy.SetAnim("victory");
            myGuy.frame_speed = 12/Helper.SECOND;
        }
        else if (gtl.isPlaying) {
            myGuy.loopingMode = LOOPMODE_STOP;
            myGuy.frame_speed = 31/Helper.SECOND;

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
                            myGuy.loopingMode = LOOPMODE_REPEAT;
                            myGuy.SetAnim("ready");
                    }
                }
                else {
                    myGuy.loopingMode = LOOPMODE_REPEAT;
                    myGuy.SetAnim("ready");
                }
            }
        }
        else {
            myGuy.loopingMode = LOOPMODE_REPEAT;
            myGuy.SetAnim("idle");
            myGuy.frame_speed = 12/Helper.SECOND;
        }
    }
}
