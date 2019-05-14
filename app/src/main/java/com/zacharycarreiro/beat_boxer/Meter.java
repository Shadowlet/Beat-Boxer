package com.zacharycarreiro.beat_boxer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class Meter extends DisplayableActor{

    Resourcer mResourcer = new Resourcer();


    Arrow myArrow;

    int gah;

    float meterValue;
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

        if (Inputter.check(MotionEvent.ACTION_DOWN, null)){
            punchBag();
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



        meterValue = (float)Math.sin(gah /10f);


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
            }
            else if (meterValue <= sweetspot)
            {
                Helper.DebugMessage("Too Slow");
            }
            else if (meterValue >= sweetspot) {
                Helper.DebugMessage("Too fast");
            }
        }
        else {
            Helper.DebugMessage("Miss");
        }

        //Helper.DebugMessage("Too Slow");

    }
}
