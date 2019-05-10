package com.zacharycarreiro.beat_boxer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GameScreen extends Actor {



    @Override
    public void Update()
    {

    }

    @Override
    public void Draw(Canvas c, Paint p) {
        //float size = 10;


        p.setColor(Color.argb(255,255,255,255));
        Artist.drawRect(0, Artist.screenHeight/6, Artist.screenWidth, Artist.screenHeight);


    }

}
