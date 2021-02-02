package com.appbirds.movies.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.appbirds.movies.R;
import com.appbirds.movies.utils.AppLog;

public class SplashActivity extends BaseActivity {
    public static String TAG = SplashActivity.class.getSimpleName();

    private Handler mHandler;
    private Runnable mRunnable;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = SplashActivity.this;
        setActivityContext(activity);
        //full screen code
        setFullScreen(true);
        setContentView(R.layout.activity_splash);

        nextScreens();
    }

    //load next screen
    private void nextScreens() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                nextActivityFinish(MainActivity.class);
                finish();
            }
        };
        mHandler.postDelayed(mRunnable, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppLog.e(TAG, "onDestroy: ");
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }
}
