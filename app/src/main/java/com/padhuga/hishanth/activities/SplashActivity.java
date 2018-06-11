package com.padhuga.hishanth.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.VideoView;

import com.padhuga.hishanth.R;

import java.util.Random;

public class SplashActivity extends AppCompatActivity {
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_splash);
            videoView = findViewById(R.id.videoView);
            int[] splashVideoIds = new int[]{R.raw.hishanth_video1,R.raw.hishanth_video2, R.raw.hishanth_video3, R.raw.hishanth_video4, R.raw.hishanth_video5, R.raw.hishanth_video6};
            final Random randomGenerator = new Random();
            int randomValue = randomGenerator.nextInt(splashVideoIds.length);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
                    + splashVideoIds[randomValue]);
            videoView.setVideoURI(video);

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                public void onCompletion(MediaPlayer mp) {
                    jump();
                }

            });
            videoView.start();
        } catch (Exception ex) {
            jump();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            videoView.stopPlayback();
        } catch (Exception ex) {

        }
        jump();
        return true;
    }

    private void jump() {
        if (isFinishing())
            return;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
