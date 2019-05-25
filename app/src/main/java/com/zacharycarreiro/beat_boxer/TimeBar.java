package com.zacharycarreiro.beat_boxer;

public class TimeBar {

    public static final int BT_NONE = 0; // *** No beat; Not necessary to press anything
    public static final int BT_BASE = 1; // *** Standard beat; Miss two beats if you don't get it
    public static final int BT_HARD = 2; // *** Game is terminated if you miss this beat
    public static final int BT_EASY = 3; // *** No penalty for missing this beat
    public static final int BT__NUMBER = 4;



    // *** The "last" beat represents the start of the next bar/measure (meaning the first is not the first beat of the current bar)
    public int[] hasBeat = new int[GameSong.BEATCOUNT];

    public boolean HasBeat(int index) { return (hasBeat[index] != BT_NONE); }

    public TimeBar() {}
    public TimeBar(int b0, int b1, int b2, int b3) {
        hasBeat[0] = b0;
        hasBeat[1] = BT_NONE;
        hasBeat[2] = BT_NONE;
        hasBeat[3] = BT_NONE;
        hasBeat[4] = b1;
        hasBeat[5] = BT_NONE;
        hasBeat[6] = BT_NONE;
        hasBeat[7] = BT_NONE;
        hasBeat[8] = b2;
        hasBeat[9] = BT_NONE;
        hasBeat[10] = BT_NONE;
        hasBeat[11] = BT_NONE;
        hasBeat[12] = b3;
        hasBeat[13] = BT_NONE;
        hasBeat[14] = BT_NONE;
        hasBeat[15] = BT_NONE;
    }
    public TimeBar(int b0, int b1, int b2, int b3, int b4, int b5, int b6, int b7, int b8, int b9, int ba, int bb, int bc, int bd, int be, int bf) {
        hasBeat[0] = b0;
        hasBeat[1] = b1;
        hasBeat[2] = b2;
        hasBeat[3] = b3;
        hasBeat[4] = b4;
        hasBeat[5] = b5;
        hasBeat[6] = b6;
        hasBeat[7] = b7;
        hasBeat[8] = b8;
        hasBeat[9] = b9;
        hasBeat[10] = ba;
        hasBeat[11] = bb;
        hasBeat[12] = bc;
        hasBeat[13] = bd;
        hasBeat[14] = be;
        hasBeat[15] = bf;
    }
}
