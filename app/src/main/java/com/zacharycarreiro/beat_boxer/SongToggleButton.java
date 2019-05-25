package com.zacharycarreiro.beat_boxer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class SongToggleButton extends ButtonArea {


    public String text;


    public SongToggleButton(int tx, int ty, Runnable blah) {
        super("songbox", new Rect(), blah);


        int xx, yy;
        xx = (int)(tx - sprite.offsetX*2);
        yy = (int)(ty - sprite.offsetY*2);
        bounds = new Rect(xx, yy, xx+sprite.GetWidth()*2, yy+sprite.GetHeight()*2);
    }


    @Override
    public void Draw(Canvas c, Paint p) {
        super.Draw(c, p);


        if (visible) {
            p.setColor(Color.argb(255, 0, 0, 0));
            p.setTextSize(40);
            p.setTextAlign(Paint.Align.CENTER);
            c.drawText(text, x, y, p);
            //
            p.setTextAlign(Paint.Align.LEFT);
        }
    }
}
