package arne5.com.moonstone2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

public class FlashLightActivity extends AppCompatActivity {

    private CameraManager mCameraManager;
    private String mCameraId;
    private ImageButton mTorchOnOffButton;
    private Boolean isTorchOn;
    private MediaPlayer mp;
    private MediaPlayer mp2;
    private MediaPlayer mp3;
    private LoopMediaPlayer lp1;
    private Button buttonSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d("FlashLightActivity","OnCreate()");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation((ActivityInfo.SCREEN_ORIENTATION_PORTRAIT));
        setContentView(R.layout.activity_flash_light);
        mTorchOnOffButton = (ImageButton) findViewById(R.id.button_on_off);
        buttonSound = (Button) findViewById((R.id.btnSound));
        isTorchOn = false;


        Boolean isFlashAvailable = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if(!isFlashAvailable) {
            AlertDialog alert = new AlertDialog.Builder(FlashLightActivity.this).create();
            alert.setTitle("Error !!");
            alert.setMessage("your device does not support flash light");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    System.exit(0);
                }
            });
            alert.show();
            return;
        }

            mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try {
                mCameraId = mCameraManager.getCameraIdList()[0];
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

            buttonSound.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(isTorchOn)
                    {
                        playOffSound();
                    }

                    if(!isTorchOn)

                    {
                        playOnSound();
                    }

                }

            });
            mTorchOnOffButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (isTorchOn) {
                            turnOffFlashLight();
                            isTorchOn = false;
                        }
                        else
                        {
                            turnOnFlashLight();
                            isTorchOn = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    public void turnOnFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, true);
                playOnSound();
                mTorchOnOffButton.setImageResource(R.drawable.green_lightsaber);
               // playRunSound();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void turnOffFlashLight() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, false);

                playOffSound();
                mTorchOnOffButton.setImageResource(R.drawable.green_lightsaberoff);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void playOnSound(){

        mp = MediaPlayer.create(FlashLightActivity.this, R.raw.fx4);
        mp2 = MediaPlayer.create(FlashLightActivity.this,R.raw.saberftn);
        lp1 = LoopMediaPlayer.create(FlashLightActivity.this,R.raw.saberftn);

       // mp2.setLooping(true);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub

                mp.release();
                lp1.start();



            }
        });
        mp2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp2) {
                // TODO Auto-generated method stub
                mp2.release();
            }
        });

        //lp1.start();
        mp.start();

        //mp.setNextMediaPlayer(mp2);




    }


    private void playOffSound(){
        if(lp1.isPlaying())
        {
            lp1.stop();
            lp1.release();
        }
       if( mp2.isPlaying())
        {
            mp2.stop();

            mp2.release();
        }
        mp3 = MediaPlayer.create(FlashLightActivity.this, R.raw.fx5);
        mp3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp3) {
                // TODO Auto-generated method stub
                mp3.release();
            }
        });
        mp3.start();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(isTorchOn){
            turnOffFlashLight();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isTorchOn){
            turnOffFlashLight();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isTorchOn){
            turnOnFlashLight();
        }
    }

}
