package com.zacharycarreiro.beat_boxer;

import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;

public class Inputter {

    public static ArrayList<SingleInput> allInputs = new ArrayList<>();


    public static boolean Read(MotionEvent event) {
        int X = (int) event.getX();
        int Y = (int) event.getY();
        int eventaction = event.getAction();
        //
        allInputs.add(new SingleInput(eventaction, X, Y));
        return true;
    }


    public static void Clear() {
        allInputs.clear();
    }



    public static boolean check(int motionEvent, Rect area) {
       for (SingleInput Q : allInputs) {
           if (Q.action == motionEvent) {
               if (area == null || area.contains((int)Q.x, (int)Q.y)) {
                   return true;
               }
           }
       }


        return false;
    }



}


