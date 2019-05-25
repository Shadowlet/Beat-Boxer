package com.zacharycarreiro.beat_boxer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

public class Meter extends DisplayableActor{

    Resourcer mResourcer = new Resourcer();


    Arrow myArrow;


    float meterValue;
    float initialMeter = 0.0f;
    float visualValue = 0.0f;


    float leeway = 0.35f;
    float evenBetter = 0.15f;
    float sweetspot = 0;


    Meter() {
        super("meter");

        onScreen = true;
    }

    @Override
    public void Discard() {
        super.Discard();


        myArrow.Discard();
        myArrow = null;
    }


    @Override
    public void Update() {
        super.Update();


        x = Artist.screenWidth * 0.2f;
        y = 64 * 13.5f;
        //
        myArrow.x = Artist.screenWidth * 0.2f   + visualValue* (sprite.GetWidth()/2);
        myArrow.y = y - 64 * 1;
    }

    @Override
    public void Draw(Canvas c, Paint p) {
        super.Draw(c, p);

        // ??? <-- Display "too fast" or "too slow", etc...
    }

    public void updateMeter(){
        GameTimeline gt = GameTimeline.CreateInstance();

        float swingPercent = (1 - gt.TimeRemainingUntilBeat() / (float)gt.TimeBetweenNextBeat());
        //
        meterValue = (float)Math.sin((Math.PI) * swingPercent); // *1.10
        //
        visualValue = initialMeter + (1-initialMeter) * swingPercent;
        visualValue = (float)Math.sin((Math.PI) *visualValue);
    }



    public static final int HITRESULT_PERFECT = 0;
    public static final int HITRESULT_FAST = 1;
    public static final int HITRESULT_SLOW = 2;
    public static final int HITRESULT_MISS = 3;
    public static final int HITRESULT_DODGE = 4;
    public static final int HITRESULT__NUMBER = 5;

    public int PunchBag(){
        float difference;
        difference = Math.abs(meterValue - sweetspot);

        if (difference <= leeway) {
            if (difference <= evenBetter) {
                Helper.DebugMessage("Hit");
                // swingPercent += 10;

                return HITRESULT_PERFECT;
            }
            else if (meterValue <= sweetspot)
            {
                Helper.DebugMessage("Too Slow");
                // swingValue += 1f;

                return HITRESULT_SLOW;
            }
            else if (meterValue >= sweetspot) {
                Helper.DebugMessage("Too fast");
                // swingValue -= 5f;

                return HITRESULT_FAST;
            }
        }
        else {
            Helper.DebugMessage("Miss");
        }

        //Helper.DebugMessage("Too Slow");
        return HITRESULT_MISS;
    }

    public void Duck(){

    }
}
