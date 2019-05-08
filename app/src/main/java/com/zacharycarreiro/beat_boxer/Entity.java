package com.zacharycarreiro.beat_boxer;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Entity {
    protected Entity() {}



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
