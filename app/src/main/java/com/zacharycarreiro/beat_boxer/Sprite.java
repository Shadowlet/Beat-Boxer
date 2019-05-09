package com.zacharycarreiro.beat_boxer;


import android.graphics.Bitmap;

// *** A class that contains various bits of "game" information about all the "Bitmaps" we use.
public class Sprite {

    public Bitmap bitmap;
    //
    public int offsetX = 0;
    public int offsetY = 0;
    public int preferredWidth;
    public int preferredHeight;

    public int frameCount = 1;
    public int frameAcross = 1;
    public int frameWidth;
    public int frameHeight;



    public Sprite(Bitmap bm) {
        bitmap = bm;
        //
        frameWidth = bm.getWidth();
        frameHeight = bm.getHeight();

        preferredWidth = bm.getWidth();
        preferredHeight = bm.getHeight();


        Cornerize();
    }


    public int GetWidth() { return (int)(bitmap.getWidth() / Artist.dm.density); }
    public int GetHeight() { return (int)(bitmap.getHeight() /Artist.dm.density); }



    // *** Sets origin to top-left
    public void Cornerize() {
        offsetX = 0;
        offsetY = 0;
    }
    // *** Sets origin to middle
    public void Centralize() {
        offsetX = frameWidth/2;
        offsetY = frameHeight/2;
    }
}
