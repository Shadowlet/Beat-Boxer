package com.zacharycarreiro.beat_boxer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Helper {
    public static String debugMessage = "";
    public static void DebugMessage(String str) {
        debugMessage = str;
    }

    public static final float SECOND = 60;

    public static Random random = new Random();


    public static float Longevity(float time, float delay, float period) {
        if (period <= 0) return 1;

        return Math.min(Math.max(0, time-delay) / period, 1);
    }

    public static float Lerp(float a, float b, float val) {
        return a + (b-a)*Math.min(Math.max(0, val), 1);
    }



    public static String TimeOfMilliseconds(long milliseconds) {
        long minutes = milliseconds / (1000*60);
        milliseconds = milliseconds % (1000*60);
        //
        long seconds = milliseconds / (1000);
        milliseconds = milliseconds % (1000);
        //
        return String.format("%02d:%02d:%03d", minutes, seconds, milliseconds);
    }
}
