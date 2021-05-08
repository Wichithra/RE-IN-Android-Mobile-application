package com.example.app.REIN;

import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

public class BUTTON3_CLICKED extends AppCompatActivity {

    private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null;
    private TextView value;

    private static ContentResolver cResolver;
    private static Window window;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_RING);
        setContentView(R.layout.layout3);

        cResolver = getContentResolver();
        window = getWindow();

        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP | Gravity.LEFT;
        wlp.x = 25;
        wlp.y = 160;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        value = (TextView) findViewById(R.id.ringtonevolumevalue);
        initControls();
    }

    private void initControls() {
        try {
            volumeSeekbar = (SeekBar) findViewById(R.id.seekBar3);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_RING));
            value.setText(String.valueOf(audioManager.getStreamVolume(AudioManager.STREAM_RING)));
            volumeSeekbar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_RING));


            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_RING,
                            progress, 0);
                    value.setText(String.valueOf(progress));
                }
            });
        } catch (Exception e) {

        }
    }
}
