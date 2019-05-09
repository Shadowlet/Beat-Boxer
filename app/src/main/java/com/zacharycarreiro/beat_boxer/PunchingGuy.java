package com.zacharycarreiro.beat_boxer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class PunchingGuy extends Actor {


    @Override
    public void Update() {

    }


    @Override
    public void Draw(Canvas c, Paint p) {
        float size = 10;


        //p.setColor(Color.argb(255, 255, 255, 255));
        //Artist.drawRect(x - (size / 2),y - (size / 2), x + (size / 2), y + (size / 2));




        Bitmap Q = Resourcer.allBitmaps.get("punchingGuy").bitmap;
        Artist.drawBitmap("punchingGuy", 0, x, y, 1, 1, 0);

    }



    @Override
    public void onCreate() {
        x = Artist.screenWidth * 0.55f;
        y = Artist.screenHeight * 0.53f;
    }
}
