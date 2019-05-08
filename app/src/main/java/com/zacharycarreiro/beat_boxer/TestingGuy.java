package com.zacharycarreiro.beat_boxer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class TestingGuy extends Actor {



    @Override
    public void Update() {

    }

    int gah = 0;

    @Override
    public void Draw(Canvas c, Paint p) {
        float size = 10;


        p.setColor(Color.argb(255, 255, 255, 255));
        Artist.drawRect(x - (size / 2),y - (size / 2),
                x + (size / 2), y + (size / 2));


        gah++;

        Bitmap Q = Resourcer.allBitmaps.get("apple").bitmap;
        Artist.drawBitmap("gggg", gah/10, 800, 400, 1f/3, 1f/3, 0);
        // Artist.drawBitmap("apple", gah, 800, 400, 2f + gah/100.0f, 2f, gah);
        // Artist.drawBitmap("apple", x - Q.getWidth()/2, y - Q.getHeight()/2, 2f, 2f, 10f);
    }



    @Override
    public void onCreate() {
        x = 10;
        y = 10;
    }
}
