package com.zacharycarreiro.beat_boxer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;

public class Artist {


    public static Canvas c;
    public static Paint p;

    public static int screenWidth = 0;
    public static int screenHeight = 0;
    public static float ratioWidth = 1.0f;
    public static float ratioHeight = 1.0f;
    public static int offsetX = 0;
    public static int offsetY = 0;


    public static boolean retainRatio = false;



    public static boolean isInit = false;

    public static Point displaySize = new Point();
    public static Display d = null;


    public static void Initialize(Display display) {
        d = display;
        //
        d.getSize(displaySize);
        //
        SetScreenSize(displaySize.x, displaySize.y);


        isInit = true;
    }

    public static void Setup(Canvas canvas, Paint paint) {
        c = canvas;
        p = paint;
    }



    public static void SetScreenSize(float val) {
        d.getSize(displaySize);
        //
        SetScreenSize((int)(displaySize.x/val), (int)(displaySize.y/val));
    }
    public static void SetScreenSize(int width, int height) {
        d.getSize(displaySize);


        screenWidth = width;
        screenHeight = height;
        //
        ratioWidth = screenWidth / (float)displaySize.x;
        ratioHeight = screenHeight / (float)displaySize.y;
        //
        offsetX = 0;
        offsetY = 0;
        //
        if (retainRatio) {
            ratioWidth = 1;
            ratioHeight = 1;
            //
            offsetX = (displaySize.x - screenWidth)/2;
            offsetY = (displaySize.y - screenHeight)/2;


            /*
            float lesser = Math.min(ratioWidth, ratioHeight);
            ratioWidth = lesser;
            ratioHeight = lesser;
            //
            int xx, yy;
            xx = (int)(displaySize.x * ratioWidth);
            yy = (int)(displaySize.y * ratioHeight);

            offsetX = (screenWidth - xx)/2;
            offsetY = (screenHeight - yy)/2;
            //
            screenWidth = xx;
            screenHeight = yy;
            */
        }
    }



    public static void drawRect(float x1, float y1, float x2, float y2) {
        drawRect((int)x1, (int)y1, (int)x2, (int)y2);
    }
    public static void drawRect(int x1, int y1, int x2, int y2) {
        if (!isInit) return;


        c.drawRect(offsetX+ x1 /ratioWidth, offsetY+ y1 /ratioHeight, offsetX+ x2 /ratioWidth, offsetY+ y2 /ratioHeight, p);
    }


    public static void drawBitmap(Bitmap bitmap, int ix1, int iy1, int ix2, int iy2, Rect screenRect) {
        drawBitmap(bitmap, new Rect(ix1, iy1, ix2, iy2), screenRect);
    }
    public static void drawBitmap(Bitmap bitmap, Rect imageRect, int sx1, int sy1, int sx2, int sy2) {
        drawBitmap(bitmap, imageRect, new Rect(sx1, sy1, sx2, sy2));
    }
    public static void drawBitmap(Bitmap bitmap, int ix1, int iy1, int ix2, int iy2, int sx1, int sy1, int sx2, int sy2) {
        drawBitmap(bitmap, new Rect(ix1, iy1, ix2, iy2), new Rect(sx1, sy1, sx2, sy2));
    }
    public static void drawBitmap(Bitmap bitmap, Rect imageRect, Rect screenRect) {
        c.drawBitmap(bitmap, imageRect, screenRect, p);
    }
    public static void drawBitmap(String bitmap, float currentFrame, float x, float y, float sclx, float scly, float rot) {
        drawBitmap(Resourcer.allBitmaps.get(bitmap), currentFrame, x, y, sclx, scly, rot);
    }
    // *** This is the most "preferred" method to use.
    public static void drawBitmap(Sprite image, float currentFrame, float x, float y, float sclx, float scly, float rot) {

        currentFrame = Math.round(currentFrame % image.frameCount);

        /*
        Matrix m = new Matrix();
        m.reset();
        m.postTranslate(-image.offsetX, -image.offsetY); // Orientation...?
        //
        m.postScale(sclx, scly);
        m.postRotate(rot);
        //
        m.postTranslate(x, y);
        */



        c.save();
        //
        //
        /*
        c.translate(x, y);

        c.translate(-image.offsetX *sclx, -image.offsetY *scly); // Orientation...?
        //
        c.rotate(rot, image.offsetX *sclx, image.offsetY *scly);
        //
        c.scale(sclx, scly);
        */

        //
        //
        float mn = 2.625f; // ??? <-- What the hell does this number mean...? Where did it come from? Are the aliens trying to communicate with us?
        int tx, ty;
        tx = (int) (image.frameWidth*mn * (currentFrame % image.frameAcross));
        ty = (int) (image.frameHeight*mn * (Math.floor(currentFrame / image.frameAcross)));
        Log.d("Artist", "Frame data...");
        Log.d("Artist", ""+image.bitmap);
        Log.d("Artist", ""+ratioWidth);
        Log.d("Artist", ""+ratioHeight);
        c.drawBitmap(image.bitmap,
                new Rect(0 + tx,0 + ty, 0 + (int)(image.frameWidth*mn) + tx, 0 + (int)(image.frameHeight*mn) + ty),
                new Rect(0, 0, image.frameWidth*5, image.frameHeight*5),
                p);
        // c.drawBitmap(image.bitmap, 0, 0, p);
        // c.drawBitmap(image.bitmap, m, p);
        //
        //
        c.restore();
    }



}
