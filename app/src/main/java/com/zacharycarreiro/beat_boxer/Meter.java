package com.zacharycarreiro.beat_boxer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

public class Meter extends DisplayableActor{

    Resourcer mResourcer = new Resourcer();


    Arrow myArrow;


    Rect punchTouchArea = new Rect(Artist.screenWidth/2, 0, Artist.screenWidth, Artist.screenHeight);
    Rect duckTouchArea = new Rect(0, 0, Artist.screenWidth/2, Artist.screenHeight);

    float meterValue;
    float initialMeter = 0.0f;
    float visualValue = 0.0f;

    float swingValue = 10f;
    float swingPercent = 100f;
    float leeway = 0.2f;
    float evenBetter = 0.1f;
    float sweetspot = 0;


    Meter() {
        super("meter");
    }


    @Override
    public void Update() {
        super.Update();

        /*
        if (Inputter.check(MotionEvent.ACTION_DOWN, punchTouchArea)){
            PunchBag();
            Log.d("STATE", "Punching");
        }
        else if(Inputter.check(MotionEvent.ACTION_DOWN, duckTouchArea)){
            Duck();
            Log.d("STATE", "Ducking");
        }
        */

        //Helper.DebugMessage("" + meterValue);
    }

    @Override
    public void Draw(Canvas c, Paint p) {
        super.Draw(c, p);


    }

    public void updateMeter(){
        x = Artist.screenWidth * 0.2f;
        y = 64 * 13.5f;


        GameTimeline gt = GameTimeline.CreateInstance();

        float swingPercent = (1 - gt.TimeRemainingUntilBeat() / (float)gt.TimeBetweenNextBeat());
        // Helper.DebugMessage(""+swingPercent);
        // Helper.DebugMessage(""+meterValue);
        //
        meterValue = (float)Math.sin((Math.PI) * swingPercent); // *1.10
        //
        visualValue = initialMeter + (1-initialMeter) * swingPercent;
        visualValue = (float)Math.sin((Math.PI) *visualValue);


                // meterValue = (float)Math.sin(gah /(swingValue *(1/(swingPercent/100))));


        myArrow.x = Artist.screenWidth * 0.2f   + visualValue* (sprite.GetWidth()/2);
        myArrow.y = y - 64 * 1;
    }



    public static final int HITRESULT_PERFECT = 0;
    public static final int HITRESULT_FAST = 1;
    public static final int HITRESULT_SLOW = 2;
    public static final int HITRESULT_MISS = 3;
    public static final int HITRESULT_DODGE = 4;

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
