package com.zacharycarreiro.beat_boxer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    Intent iPlay;
    Intent iControls;


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


        iPlay = new Intent(this, ScreenActivity.class);
        iControls = new Intent(this, ControlsActivity.class);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.buttonPlay:
                startActivity(iPlay);
                break;
            case R.id.buttonControls:
                startActivity(iControls);
                break;
            case R.id.buttonQuit:
                finish();
                System.exit(0);
                break;

        }
    }
}
