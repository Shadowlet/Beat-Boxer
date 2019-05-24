package com.zacharycarreiro.beat_boxer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class ButtonArea extends DisplayableActor {



    public Runnable doesWhat;
    public Rect bounds;

    public ButtonArea(String spr, Rect area, Runnable does) {
        super(spr);

        bounds = area;
        doesWhat = does;
    }



    @Override
    public void Process() {
        super.Process();


        x = bounds.left +sprite.offsetX;
        y = bounds.top +sprite.offsetY;
        image_xscale = bounds.width()  / (float)sprite.GetWidth();
        image_yscale = bounds.height() / (float)sprite.GetHeight();
    }

    @Override
    public void Update() {
        super.Update();


        if (visible) {
            if (Inputter.check(MotionEvent.ACTION_DOWN, bounds)) {
                doesWhat.run();
            }
        }
    }

    @Override
    public void Draw(Canvas c, Paint p) {
        super.Draw(c, p);


    }
}
