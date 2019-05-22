package com.zacharycarreiro.beat_boxer;

import java.util.ArrayList;
import java.util.HashMap;

public class AnimatedActor extends DisplayableActor {



    public String animState = "";
    public HashMap<String, AnimData> allAnims = new HashMap<>();


    public AnimatedActor(String spr) {
        super(spr);


        RegisterAnim("default", spr);
        //
        SetAnim("default");
    }


    public void RegisterAnim(String index, String name) {
        allAnims.put(index, new AnimData(name));
    }
    public void ClearAnims() {
        allAnims.clear();
    }

    public void SetAnim(String index) {
        if (animState == index) return;

        AnimData ad = allAnims.get(index);
        if (ad != null) {
            sprite = Resourcer.allBitmaps.get(ad.spriteName);
            //
            animState = index;
        }
    }




    public class AnimData {
        String spriteName;


        public AnimData(String sprName) {
            spriteName = sprName;
        }
    }
}
