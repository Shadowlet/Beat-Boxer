package com.zacharycarreiro.beat_boxer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class PunchingGuy extends AnimatedActor {



    PunchingGuy() {
        super("punchingGuy");


        RegisterAnim("idle", "lukas_idle");
        RegisterAnim("ready", "lukas_ready");
        RegisterAnim("punch", "lukas_punch");
        RegisterAnim("duck", "lukas_duck");
        RegisterAnim("fly", "lukas_fly");
        RegisterAnim("victory", "lukas_victory");

        frame_speed = 12/Helper.SECOND;
        image_xscale = 2.5f;
        image_yscale = 2.5f;
    }


    @Override
    public void Update() {
        super.Update();

    }



    @Override
    public void Draw(Canvas c, Paint p) {
        super.Draw(c, p);
    }
}
