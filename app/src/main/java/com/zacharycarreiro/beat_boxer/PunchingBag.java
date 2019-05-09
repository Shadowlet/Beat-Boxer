package com.zacharycarreiro.beat_boxer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class PunchingBag extends Actor{

    @Override
    public void Update() {

    }


    @Override
    public void Draw(Canvas c, Paint p) {
        super.Draw(c, p);
        float size = 10;


        //p.setColor(Color.argb(255, 255, 255, 255));
        //Artist.drawRect(x - (size / 2),y - (size / 2), x + (size / 2), y + (size / 2));




        Bitmap Q = Resourcer.allBitmaps.get("punchingBag").bitmap;
        Artist.drawBitmap("punchingBag", 0, x, y, 1, 1, 0);

    }



    @Override
    public void onCreate() {
        super.onCreate();

        x = Artist.screenWidth *0.85f;
        y = 200;
    }
}
