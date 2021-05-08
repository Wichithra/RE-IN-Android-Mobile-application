package com.example.app.REIN;

        import android.app.Service;
        import android.content.Context;
        import android.content.Intent;
        import android.hardware.Sensor;
        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorManager;
        import android.media.AudioFormat;
        import android.media.AudioRecord;
        import android.media.MediaRecorder;
        import android.net.Uri;
        import android.os.Handler;
        import android.os.IBinder;
        import android.provider.Settings;
        import android.util.Log;
        import android.widget.Toast;

public class MyService extends Service implements SensorEventListener {






    private static String TAG = "MyService";
    private Handler handler;
    private Runnable runnable;
    private final int runTime = 2000;

    private SensorManager mSensorManager;
    private Sensor mAmbient;
    private float light;
    private float brightness;





    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + this.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        Toast.makeText(this, "Please note that Your auto brightness hass set to manual", Toast.LENGTH_SHORT).show();

        Settings.System.putInt(this.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);





        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAmbient = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this, mAmbient, SensorManager.SENSOR_DELAY_NORMAL);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                adaptBrightness();

                handler.postDelayed(runnable, runTime);
            }
        };
        handler.post(runnable);
    }

    private void adaptBrightness(){

        if (light==0){
            brightness = 1/255f;
            change(brightness);
        }
        else if(light>0 && light<= 12){
            brightness = 75/255f;
            change(brightness);
        }
        else if(light>12 && light<= 100){
            brightness = 125/255f;
            change(brightness);
        }
        else if(light>100 && light<= 500){
            brightness = 155/255f;
            change(brightness);
        }
        else if(light>500 && light<= 1000){
            brightness = 200/255f;
            change(brightness);
        }
        else if(light>1000 && light<= 2000){
            brightness = 255/255f;
            change(brightness);
        }
        else if(light>2000 && light<= 3000){
            brightness = 255/255f;
            change(brightness);
        }
        else if(light>3000 && light<= 4000){
            brightness = 255/255f;
            change(brightness);
        }
        else if(light>4000 && light<= 5000){
            brightness = 255/255f;
            change(brightness);
        }
        else {
            brightness = 1.0f;
            change(brightness);
        }

    }

    private void change( float brightness){
        // This is important. In the next line 'brightness'
        // should be a float number between 0.0 and 1.0
        int brightnessInt = (int)(brightness*255);

        //Check that the brightness is not 0, which would effectively
        //switch off the screen, and we don't want that:
        if(brightnessInt<1) {brightnessInt=1;}

        // Set systemwide brightness setting.
        Settings.System.putInt(getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightnessInt);

        // Apply brightness by creating a dummy activity
        Intent intent = new Intent(getBaseContext(), DummyBrightnessActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("brightness value", brightness);
        getApplication().startActivity(intent);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(this);
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(TAG, "onStart");





    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        light = sensorEvent.values[0];

    }


}
