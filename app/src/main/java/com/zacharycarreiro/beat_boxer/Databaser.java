package com.zacharycarreiro.beat_boxer;

import java.util.ArrayList;
import java.util.HashMap;

public class Databaser {
    private static Databaser _instance = null;
    public static Databaser CreateInstance() {
        if (_instance == null) {
            _instance = new Databaser();
        }

        return _instance;
    }

    private Databaser() { Initialize(); }




    public HashMap<Character, Integer> charToBeat = new HashMap<>();
    public HashMap<String, String> gameRaws_BeatMaps = new HashMap<>();
    private void Initialize() {
        // *** Beat Key
        charToBeat.put('_', GameTimeline.TimeBar.BT_NONE);
        charToBeat.put('B', GameTimeline.TimeBar.BT_BASE);
        // charToBeat.put('E', GameTimeline.TimeBar.BT_EASY);
        charToBeat.put('E', GameTimeline.TimeBar.BT_NONE);
        charToBeat.put('H', GameTimeline.TimeBar.BT_HARD);


        // *** This supports both 4 and 16 resolution bars

        // *** "Emphasis"
        gameRaws_BeatMaps.put("emphasis", "____\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B_B_\n" +
                "B___\n");

        // *** "C_HC"
        gameRaws_BeatMaps.put("c_hc", "____\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "E_______E_______\n" + // *** Silence...
                "E_______E_______\n" +
                "E_______E_______\n" +
                "E_______E_______\n" +
                "H_____B_________\n" + // *** Big beat
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "B_____B_________\n" + // *** Fade out
                "B_E___B___E_____\n" +
                "B_____B_________\n" +
                "B_E___B___E_____\n" +
                "E_____E_________\n" + // *** Easy hits
                "E_E___E___E_____\n" +
                "E_____E_________\n" +
                "E_E___E___E_____\n");


        // blah = blah.replaceAll("\\s","");
    }


}
