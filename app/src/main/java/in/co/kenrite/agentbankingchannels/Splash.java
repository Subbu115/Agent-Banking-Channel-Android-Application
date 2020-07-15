package in.co.kenrite.agentbankingchannels;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        Timer RunSplash=new Timer();
        TimerTask showSplash=new TimerTask() {

            @Override
            public void run() {
                finish();
                Intent intent =new Intent(Splash.this, Login.class);
                startActivity(intent);
            }
        };
        long delay=5000;
        RunSplash.schedule(showSplash, delay);

    }
}
