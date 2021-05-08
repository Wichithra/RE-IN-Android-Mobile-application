package com.example.app.REIN;

import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import static android.media.AudioManager.ADJUST_LOWER;
import static android.media.AudioManager.ADJUST_RAISE;
import static android.media.AudioManager.ADJUST_SAME;
import static android.media.AudioManager.STREAM_VOICE_CALL;

public class BUTTON5_CLICKED extends AppCompatActivity {
    private static final int sampleRate = 8000;
    private AudioRecord audio;
    private int bufferSize;
    private double lastLevel = 0;
    private Thread thread;
    private static final int SAMPLE_DELAY = 7;
    private AudioManager am;
    private  MediaRecorder recorder;
    private Handler handler;
    private Runnable runnable;
    private final int runTime = 2000;


      private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null;
    private TextView value;

    private static ContentResolver cResolver;
    private static Window window;


    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        setContentView(R.layout.layout5);

        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        //am.adjustStreamVolume(STREAM_VOICE_CALL,ADJUST_RAISE,5);

        cResolver = getContentResolver();
        window = getWindow();

        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP | Gravity.RELATIVE_LAYOUT_DIRECTION;
        wlp.x = 25;
        wlp.y = 160;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        value = (TextView) findViewById(R.id.callvolumevalue);
        initControls();


        try {
            bufferSize = AudioRecord
                    .getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO,
                            AudioFormat.ENCODING_PCM_16BIT);
        } catch (Exception e) {
            android.util.Log.e("TrackingFlow", "Exception", e);
        }
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
               adaptVolume();

                handler.postDelayed(runnable, runTime);
            }
        };
        handler.post(runnable);





    }
    protected void onResume() {
        super.onResume();
        MediaRecorder recorder = new MediaRecorder();
        audio = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);

        audio.startRecording();
        thread = new Thread(new Runnable() {
            public void run() {
                while(thread != null && !thread.isInterrupted()){
                    //Let's make the thread sleep for a the approximate sampling time
                    try{Thread.sleep(SAMPLE_DELAY);}catch(InterruptedException ie){ie.printStackTrace();}
                    readAudioBuffer();//After this call we can get the last value assigned to the lastLevel variable
                                   }
            }
        });
        thread.start();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                adaptVolume();

                handler.postDelayed(runnable, runTime);
            }
        };
        handler.post(runnable);

    }

    private void readAudioBuffer() {

        try {
            short[] buffer = new short[bufferSize];

            int bufferReadResult = 1;

            if (audio != null) {

                // Sense the voice...
                bufferReadResult = audio.read(buffer, 0, bufferSize);
                double sumLevel = 0;
                for (int i = 0; i < bufferReadResult; i++) {
                    sumLevel += buffer[i];
                }
                lastLevel = Math.abs((sumLevel / bufferReadResult));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        thread.interrupt();
        thread = null;
        try {
            if (audio != null) {
                audio.stop();
                audio.release();
                audio = null;
            }
        } catch (Exception e) {e.printStackTrace();}
    }
    public double soundDb(double ampl){
        return  20 * Math.log10(getAmplitudeEMA() / ampl);
    }
    public double getAmplitude() {
        if ( recorder!= null)
            return  (recorder.getMaxAmplitude());
        else
            return 0;

    }
    public double getAmplitudeEMA() {
        double amp =  getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }



    private void adaptVolume() {

        if (lastLevel > 10 ) {

           //am.setStreamVolume(STREAM_VOICE_CALL, ADJUST_RAISE, 1);
            am.adjustVolume(ADJUST_RAISE,1);
        }

        else if (lastLevel < 2 ){
      //  am.adjustStreamVolume(STREAM_VOICE_CALL,ADJUST_RAISE,3);
           // am.setStreamVolume(STREAM_VOICE_CALL, ADJUST_RAISE,1);
            am.adjustVolume(ADJUST_LOWER,1);

        }

        else if (lastLevel>500){
       //   am.adjustStreamVolume(STREAM_VOICE_CALL,ADJUST_RAISE,5);
           //am.setStreamVolume(STREAM_VOICE_CALL, ADJUST_RAISE, 1);
            am.adjustVolume(ADJUST_RAISE,1);

        }
    }

    private void initControls() {
        try {
            volumeSeekbar = (SeekBar) findViewById(R.id.seekBar5);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL));
            value.setText(String.valueOf(audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL)));
            volumeSeekbar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_VOICE_CALL));


            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                            progress, 0);
                    value.setText(String.valueOf(progress));
                }
            });
        } catch (Exception e) {

        }
    }
}
