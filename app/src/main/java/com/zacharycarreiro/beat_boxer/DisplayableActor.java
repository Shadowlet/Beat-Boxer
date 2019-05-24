package com.zacharycarreiro.beat_boxer;

import android.graphics.Canvas;
import android.graphics.Paint;

public class DisplayableActor extends Actor {
    Sprite sprite;
    float image_frame = 0.0f;
    float image_xscale = 1.0f;
    float image_yscale = 1.0f;
    float image_rotate = 0.0f;
    //
    float frame_speed = 1.0f;

    boolean onScreen = false;
    boolean visible = true;





    public final int LOOPMODE_REPEAT = 0;
    public final int LOOPMODE_STOP = 1;
    public int loopingMode = LOOPMODE_REPEAT;



    protected DisplayableActor() { super(); }
    public DisplayableActor(String spr) {
        super();


        sprite = Resourcer.allBitmaps.get(spr);
    }

    @Override
    public void Process() {
        super.Process();


        switch (loopingMode) {
            case LOOPMODE_REPEAT:
                image_frame = ((image_frame +frame_speed) +sprite.frameCount) % sprite.frameCount;
                break;
            case LOOPMODE_STOP:
                image_frame = Math.max(0, Math.min((image_frame +frame_speed), sprite.frameCount-1));
                break;
        }
        image_rotate = (image_rotate +360) % 360;
    }



    @Override
    public void Draw(Canvas c, Paint p) {
        super.Draw(c, p);


        if (visible) {
            Artist.drawBitmap(sprite, image_frame, x, y, image_xscale, image_yscale, image_rotate, onScreen);
        }
    }
}
