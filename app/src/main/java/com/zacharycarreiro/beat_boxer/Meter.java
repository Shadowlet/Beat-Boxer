package com.zacharycarreiro.beat_boxer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

public class Meter extends DisplayableActor{

    Resourcer mResourcer = new Resourcer();


    Arrow myArrow;

    int gah;


    Rect punchTouchArea = new Rect(Artist.screenWidth/2, 0, Artist.screenWidth, Artist.screenHeight);
    Rect duckTouchArea = new Rect(0, 0, Artist.screenWidth/2, Artist.screenHeight);

    float meterValue;
    float swingValue = 10f;
    float swingPercent = 100f;
    float leeway = 0.7f; // 0.2f;
    float evenBetter = 0.1f;
    float sweetspot = 0;


    Meter() {
        super("meter");
    }


    @Override
    public void Update() {
        super.Update();

        updateMeter();

        if (Inputter.check(MotionEvent.ACTION_DOWN, punchTouchArea)){
            punchBag();
            Log.d("STATE", "Punching");
        }
        else if(Inputter.check(MotionEvent.ACTION_DOWN, duckTouchArea)){
            Duck();
            Log.d("STATE", "Ducking");
        }
        //Helper.DebugMessage("" + meterValue);
        gah++;
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
        Helper.DebugMessage("Remain: "+gt.lastBeatMoment + " | Next: "+gt.beatMoment);
        //
        meterValue = (float)Math.sin((Math.PI) * swingPercent); // *1.10


        // meterValue = (float)Math.sin(gah /(swingValue *(1/(swingPercent/100))));


        myArrow.x = Artist.screenWidth * 0.2f   + meterValue* (sprite.GetWidth()/2);
        myArrow.y = y - 64 * 1;
    }

    private void punchBag(){
        gah = 0;
        float difference;
        difference = Math.abs(meterValue - sweetspot);

        if (difference <= leeway) {
            if (difference <= evenBetter) {
                Helper.DebugMessage("Hit");
                swingPercent += 10;
            }
            else if (meterValue <= sweetspot)
            {
                Helper.DebugMessage("Too Slow");
                swingValue += 1f;
            }
            else if (meterValue >= sweetspot) {
                Helper.DebugMessage("Too fast");
                swingValue -= 5f;
            }
        }
        else {
            Helper.DebugMessage("Miss");
            // You die a most embarrassing death
        }

        //Helper.DebugMessage("Too Slow");

    }

    private void Duck(){

    }
}
