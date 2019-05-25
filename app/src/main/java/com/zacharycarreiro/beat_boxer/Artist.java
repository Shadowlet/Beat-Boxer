package com.zacharycarreiro.beat_boxer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

public class Artist {


    public static Canvas c;
    public static Paint p;

    public static int screenWidth = 0;
    public static int screenHeight = 0;
    public static float ratioWidth = 1.0f;
    public static float ratioHeight = 1.0f;
    public static boolean retainRatio = false;

    public static Viewport viewport;



    public static boolean isInit = false;
    public static boolean isSetup = false;

    public static Point displaySize = new Point();
    public static Display d = null;
    public static DisplayMetrics dm = null;

    public static void Initialize(Display display, DisplayMetrics displaymetrics) {
        d = display;
        dm = displaymetrics;
        //
        d.getSize(displaySize);
        //
        SetScreenSize(displaySize.x, displaySize.y);


        viewport = new Viewport(0, 0, screenWidth, screenHeight);


        isInit = true;
    }

    public static void Setup(Canvas canvas, Paint paint) {
        c = canvas;
        p = paint;

        isSetup = true;
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
        if (retainRatio) {
            ratioWidth = 1;
            ratioHeight = 1;
        }
    }




    private static Rect srcRect = new Rect();
    private static Rect destRect = new Rect();


    public static void SetColor(int col) {
        if (!isSetup) return;

        p.setColor(col);
    }

    public static void drawRect(float x1, float y1, float x2, float y2) {
        drawRect((int)x1, (int)y1, (int)x2, (int)y2);
    }
    public static void drawRect(int x1, int y1, int x2, int y2) {
        drawRect(x1, y1, x2, y2, false);
    }
    public static void drawRect(int x1, int y1, int x2, int y2, boolean screenRelative) {
        if (!isInit) return;
        if (!isSetup) return;



        int xoff = -viewport.left;
        int yoff = -viewport.top;
        float wzoom = viewport.GetWZoom();
        float hzoom = viewport.GetHZoom();
        if (screenRelative) {
            xoff = 0;
            yoff = 0;
            wzoom = 1;
            hzoom = 1;
        }

        c.drawRect(xoff+ x1/wzoom, yoff+ y1/hzoom, xoff+ x2/wzoom, yoff+ y2/hzoom, p);
    }

    /*
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
    */
    public static void drawBitmap(String bitmap, float currentFrame, float x, float y, float sclx, float scly, float rot) {
        drawBitmap(Resourcer.allBitmaps.get(bitmap), currentFrame, x, y, sclx, scly, rot, false);
    }
    public static void drawBitmap(String bitmap, float currentFrame, float x, float y, float sclx, float scly, float rot, boolean onScreen) {
        drawBitmap(Resourcer.allBitmaps.get(bitmap), currentFrame, x, y, sclx, scly, rot, onScreen);
    }
    public static void drawBitmap(Sprite image, float currentFrame, float x, float y, float sclx, float scly, float rot) {
        drawBitmap(image, currentFrame, x, y, sclx, scly, rot, false);
    }
    // *** This is the most "preferred" method to use.
    public static void drawBitmap(Sprite image, float currentFrame, float x, float y, float sclx, float scly, float rot, boolean screenRelative) {
        if (!isSetup) return;

        currentFrame = Math.round(currentFrame % image.frameCount) % image.frameCount;

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


        float mn = dm.density; // *** The display density... Don't ask me.
        int xoff = -viewport.left;
        int yoff = -viewport.top;
        float wzoom = viewport.GetWZoom();
        float hzoom = viewport.GetHZoom();
        if (screenRelative) {
            xoff = 0;
            yoff = 0;
            wzoom = 1;
            hzoom = 1;
        }

        c.save();
        //
        //
        c.translate(xoff + x /wzoom, yoff + y /hzoom); // *** Translate
        //
        c.translate(-image.offsetX *sclx /wzoom, -image.offsetY *scly /hzoom); // Orientation...?
        //
        c.rotate(rot, image.offsetX *sclx /wzoom, image.offsetY *scly /hzoom);
        //
        //
        c.scale(sclx /wzoom, scly /hzoom);
        //
        //
        int tx, ty;
        tx = (int) (image.frameWidth*mn * (currentFrame % image.frameAcross));
        ty = (int) (image.frameHeight*mn * (Math.floor(currentFrame / image.frameAcross)));
        //
        srcRect.left = 0 + tx;
        srcRect.top = 0 + ty;
        srcRect.right = 0 + tx + (int)(image.frameWidth*mn);
        srcRect.bottom = 0 + ty + (int)(image.frameHeight*mn);

        destRect.left = 0;
        destRect.top = 0;
        destRect.right = (int)(image.frameWidth);
        destRect.bottom = (int)(image.frameHeight);
        //
        c.drawBitmap(image.bitmap,
                srcRect,
                destRect,
                p);
        //
        //
        c.restore();
    }


    public static class Viewport {
        public int left;
        public int top;
        public int width;
        public int height;

        public Viewport(int l, int t, int w, int h) {
            left = l;
            top = t;
            width = w;
            height = h;
        }


        public float GetWZoom() { return width/((float)screenWidth); }
        public float GetHZoom() { return height/((float)screenHeight); }
        public float GetZoom() { return width/((float)screenWidth); }
    }
}
