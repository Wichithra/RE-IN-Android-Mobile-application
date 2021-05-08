package com.example.app.REIN;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class BUTTON1_CLICKED extends AppCompatActivity{

    private TextView value;
    private SeekBar seeker;
    private static ContentResolver cResolver;
    private static Window window;
    private static int brightness;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
     /*   Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + this.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        Toast.makeText(this, "Please note that Your auto brightness hass set to manual", Toast.LENGTH_SHORT).show();

        Settings.System.putInt(this.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
*/
        cResolver = getContentResolver();
        window = getWindow();

        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP | Gravity.LEFT;
        wlp.x = 25;
        wlp.y = 160;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        value = (TextView) findViewById(R.id.brightnessvalue);
        seeker = (SeekBar) findViewById(R.id.seekBar3);

        changeBrightness();

    }


    private void changeBrightness(){
        //Set the seekbar range between 0 and 255
        //seek bar settings//
        //sets the range between 0 and 255

        seeker.setMax(255);
        //set the seek bar progress to 1
        seeker.setKeyProgressIncrement(1);

        try
        {
            //Get the current system brightness
            brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
        }
        catch (Settings.SettingNotFoundException e)
        {

        }

        //Set the progress of the seek bar based on the system's brightness
        seeker.setProgress(brightness);
        //Calculate the brightness percentage
        float perc = (brightness /(float)255)*100;
        //Set the brightness percentage
        value.setText((int)perc +" %");

        //Register OnSeekBarChangeListener, so it can actually change values
        seeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                //Set the system brightness using the brightness variable value
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
                //Get the current window attributes
                WindowManager.LayoutParams layoutpars = window.getAttributes();
                //Set the brightness of this window
                layoutpars.screenBrightness = brightness / (float)255;
                //Apply attribute changes to this window
                window.setAttributes(layoutpars);
            }
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                //Nothing handled here
            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                //Set the minimal brightness level
                //if seek bar is 20 or any value below
                if(progress<=20)
                {
                    //Set the brightness to 20
                    brightness=20;
                }
                else //brightness is greater than 20
                {
                    //Set brightness variable based on the progress bar
                    brightness = progress;
                }
                //Calculate the brightness percentage
                float perc = (brightness /(float)255)*100;
                //Set the brightness percentage
                value.setText((int)perc +" %");
            }
        });
    }

}
