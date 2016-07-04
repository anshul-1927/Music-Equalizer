package dev.datvt.musicequalizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;


public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CountDownTimer countDownTimer = new CountDownTimer(5000, 100) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();

//        mLoadingView = (LoadingView) findViewById(R.id.loading_view_repeat);
//        boolean isLollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
//        int marvel_1 = isLollipop ? R.drawable.marvel_1_lollipop : R.drawable.marvel_1;
//        int marvel_2 = isLollipop ? R.drawable.marvel_2_lollipop : R.drawable.marvel_2;
//        int marvel_3 = isLollipop ? R.drawable.marvel_3_lollipop : R.drawable.marvel_3;
//        int marvel_4 = isLollipop ? R.drawable.marvel_4_lollipop : R.drawable.marvel_4;
//        mLoadingView.addAnimation(Color.parseColor("#FFD200"), marvel_1,
//                LoadingView.FROM_LEFT);
//        mLoadingView.addAnimation(Color.parseColor("#2F5DA9"), marvel_2,
//                LoadingView.FROM_TOP);
//        mLoadingView.addAnimation(Color.parseColor("#FF4218"), marvel_3,
//                LoadingView.FROM_RIGHT);
//        mLoadingView.addAnimation(Color.parseColor("#C7E7FB"), marvel_4,
//                LoadingView.FROM_BOTTOM);
//
//        mLoadingView.addListener(new LoadingView.LoadingListener() {
//            @Override
//            public void onAnimationStart(int currentItemPosition) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(int nextItemPosition) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(int nextItemPosition) {
//
//            }
//        });
//        mLoadingView.startAnimation();
//
//    }
    }
}
