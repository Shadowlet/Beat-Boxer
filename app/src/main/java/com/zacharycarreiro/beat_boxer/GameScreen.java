package com.zacharycarreiro.beat_boxer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class GameScreen extends DisplayableActor {

    GameScreen(String spr) {
        super(spr);


        x = 0 +128;
        y = 0 +128;

        image_rotate = 45f;
        frame_speed = 0.1f;
    }


    @Override
    public void Update() {
        super.Update();


        if (Inputter.check(MotionEvent.ACTION_DOWN, new Rect(0, 0, 100, 100))) {
            image_yscale += 1;
        }
    }

    int gah = 0;
    int WIDE = 1;

    @Override
    public void Draw(Canvas c, Paint p) {
        super.Draw(c, p);


        /*
        float size = 10;


        p.setColor(Color.argb(255, 255, 255, 255));
        Artist.drawRect(x - (size / 2),y - (size / 2),
                x + (size / 2), y + (size / 2));

*/
        gah++;

        // Artist.drawBitmap("gggg", gah/10, 800, 400, 1f/3, 1f/3 *WIDE, 0);
        // Artist.drawBitmap("apple", gah, 800, 400, 2f + gah/100.0f, 2f, gah);
        // Artist.drawBitmap("apple", x - Q.getWidth()/2, y - Q.getHeight()/2, 2f, 2f, 10f);
        Helper.DebugMessage("X: "+image_rotate + "| Y: "+frame_speed);
    }

}
