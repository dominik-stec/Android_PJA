package pl.pjatk.pamo.kotlincalculatorbmi

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class StartLogoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_logo)
        val image = findViewById<View>(R.id.logo) as ImageView
        val hyperspaceJump = AnimationUtils.loadAnimation(
            applicationContext, R.anim.logo_anim
        )
        image.startAnimation(hyperspaceJump)
        val handlerLogoStart = Handler()
        handlerLogoStart.postDelayed({
            val image = findViewById<View>(R.id.logo) as ImageView
            val hyperspaceJump = AnimationUtils.loadAnimation(
                applicationContext, R.anim.logo_anim
            )
            image.startAnimation(hyperspaceJump)
            finish()
        }, 2500)
    }
}

//
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.ImageView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//
//public class StartLogoActivity extends AppCompatActivity {
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_start_logo);
//
//        ImageView image = (ImageView) findViewById(R.id.logo);
//        Animation hyperspaceJump = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_anim);
//        image.startAnimation(hyperspaceJump);
//
//        Handler handlerLogoStart = new Handler();
//        handlerLogoStart.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ImageView image = (ImageView) findViewById(R.id.logo);
//                Animation hyperspaceJump = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_anim);
//                image.startAnimation(hyperspaceJump);
//                finish();
//            }
//        }, 2500);
//
//    }
//}