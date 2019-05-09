package com.zacharycarreiro.beat_boxer;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Entity {
    protected Entity() {}


    // *** Any pre-calculations that need to be done before "update" is called.
    // *** This does some engine stuff so it's best NOT to override is UNLESS you plan to call "super.Process()"
    public void Process() {

    }
    public void Update() {
        // Called every frame
    }
    public void Draw(Canvas c, Paint p) {
        // Called whenever the screen in drawn-- Not necessarily on every frame (for example, game is minimized)
    }





    public void Discard() {
        // Remove from memory
    }

}
