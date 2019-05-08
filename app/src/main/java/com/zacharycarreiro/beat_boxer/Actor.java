package com.zacharycarreiro.beat_boxer;

import java.util.ArrayList;

public class Actor extends Entity {

    public static ArrayList<Actor> actorList = new ArrayList<>();


    public float x = 0;
    public float y = 0;



    protected Actor() {
        super();


        _Register();
        //
        onCreate();
    }


    public void Despawn() {

        //
        //
        _Unregister();
        //
        Discard();
    }

    public void _Register() {
        actorList.add(this);
    }
    public void _Unregister() {
        actorList.remove(this);
    }




    public void onCreate() {}
}
