package com.zacharycarreiro.beat_boxer;

public class Helper {
    public static String debugMessage = "";
    public static void DebugMessage(String str) {
        debugMessage = str;
    }




    public static float Longevity(float time, float delay, float period) {
        if (period <= 0) return 1;

        return Math.min(Math.max(0, time-delay) / period, 1);
    }
}
