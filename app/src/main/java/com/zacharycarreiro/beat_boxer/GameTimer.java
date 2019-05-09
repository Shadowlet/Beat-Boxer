package com.zacharycarreiro.beat_boxer;

import android.graphics.Canvas;
import android.graphics.Paint;

public class GameTimer extends Actor {

    @Override
    public void Update() {

    }


    @Override
    public void Draw(Canvas c, Paint p) {
        float size = 10;


        //p.setColor(Color.argb(255, 255, 255, 255));
        //Artist.drawRect(x - (size / 2),y - (size / 2), x + (size / 2), y + (size / 2));




        //Bitmap Q = Resourcer.allBitmaps.get("apple").bitmap;
        //Artist.drawBitmap("gggg", gah/10, 800, 400, 1f/3, 1f/3, 0);

    }



    @Override
    public void onCreate() {
        x = 10;
        y = 10;
    }
}
