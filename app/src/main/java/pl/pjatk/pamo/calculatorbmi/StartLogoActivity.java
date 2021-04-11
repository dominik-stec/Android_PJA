package pl.pjatk.pamo.calculatorbmi;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;



public class StartLogoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_logo);

        ImageView image = (ImageView) findViewById(R.id.logo);
        Animation hyperspaceJump = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_anim);
        image.startAnimation(hyperspaceJump);

        Handler handlerLogoStart = new Handler();
        handlerLogoStart.postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView image = (ImageView) findViewById(R.id.logo);
                Animation hyperspaceJump = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_anim);
                image.startAnimation(hyperspaceJump);
                finish();
            }
        }, 2500);

    }
}