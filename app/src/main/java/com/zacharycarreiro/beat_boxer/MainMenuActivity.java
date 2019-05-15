package com.zacharycarreiro.beat_boxer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button buttonPlay = (Button)findViewById(R.id.buttonPlay);
        Button buttonControls = (Button)findViewById(R.id.buttonControls);
        Button buttonQuit = (Button)findViewById(R.id.buttonQuit);

        buttonPlay.setOnClickListener(this);
        buttonControls.setOnClickListener(this);
        buttonQuit.setOnClickListener(this);
    }

}
