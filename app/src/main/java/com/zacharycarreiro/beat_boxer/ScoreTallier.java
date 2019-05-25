package com.zacharycarreiro.beat_boxer;

import android.os.SystemClock;

public class ScoreTallier {
    public int hitTotal = 0;
    public int[] hitCounter = new int[Meter.HITRESULT__NUMBER];

    public int comboCount = 0;
    public int lastCombo = 0;
    public int highestCombo = 0;

    public ScoreTallier() {
    }


    public long tallyTime;
    public int lastResult;

    public void Tally(int hitResult) {
        tallyTime = SystemClock.uptimeMillis();
        lastResult = hitResult;


        hitCounter[hitResult]++;
        hitTotal++;

        if (hitResult != Meter.HITRESULT_MISS && hitResult != Meter.HITRESULT_DODGE) {
            comboCount++;
            //
            highestCombo = Math.max(highestCombo, comboCount);
        }
        else {
            lastCombo = comboCount;
            //
            comboCount = 0;
        }
    }

    public String ComboMessage() {
        if (comboCount >= 100) {
            return "WHOA...";
        }
        else if (comboCount >= 40) {
            return "EARTHSHAKING!";
        }
        else if (comboCount >= 20) {
            return "BEATASTIC!";
        }
        else if (comboCount >= 10) {
            return "ROCKIN'!";
        }
        else if (comboCount >= 5) {
            return "GOOD!";
        }
        else if (comboCount >= 3) {
            return "YES!";
        }
        else {
            return "";
        }
    }
    public String FinishMessage() {
        if (lastCombo >= 100) {
            return "Holy Crap!";
        }
        else if (lastCombo >= 40) {
            return "Amazing!";
        }
        else if (lastCombo >= 20) {
            return "Excellent!";
        }
        else if (lastCombo >= 10) {
            return "Awesome!";
        }
        else if (lastCombo >= 5) {
            return "Alright!";
        }
        else if (lastCombo >= 3) {
            return "Not bad!";
        }
        else {
            return "";
        }
    }

    public String CalculateRank() {
        float maxScore = 100 * hitTotal;


        float myScore = 0;
        myScore += 70 *hitCounter[Meter.HITRESULT_FAST];
        myScore += 40 *hitCounter[Meter.HITRESULT_SLOW];
        myScore += 100 *hitCounter[Meter.HITRESULT_PERFECT];
        myScore += 10 *hitCounter[Meter.HITRESULT_DODGE];
        //
        if (myScore >= maxScore * 0.95f) {
            return "SSS";
        }
        else if (myScore >= maxScore * 0.90f) {
            return "SS";
        }
        else if (myScore >= maxScore * 0.85f) {
            return "S";
        }
        else if (myScore >= maxScore * 0.80f) {
            return "A";
        }
        else if (myScore >= maxScore * 0.70f) {
            return "B";
        }
        else if (myScore >= maxScore * 0.60f) {
            return "C";
        }
        else if (myScore >= maxScore * 0.50f) {
            return "D";
        }
        else {
            return "F";
        }
    }

    public String RankComment() {
        switch (CalculateRank()) {
            case "SSS":
                return "You've mastered this song!!! Good work!";
            case "SS":
                return "On this peak, you can nearly see the heavens!";
            case "S":
                return "Excellent! But let's not stop there!";
            case "A":
                return "Close! You just have to push your limits.";
            case "B":
                return "A nice warm-up-- Now, for real this time!";
            case "C":
                return "Sweating already? You need more practice.";
            case "D":
                return "We won't talk about that one."; // You shouldn't have killed those bombs, Zachary.
            case "F":
                return "Are you even trying...?!";

            default:
                return "... ... ...";
        }
    }
}
