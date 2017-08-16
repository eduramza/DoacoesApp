package com.ramatsolucoes.rafah.sid.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ramatsolucoes.rafah.sid.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //CLASSE RESPONSAVEL PELO DELAY DO SPLASH
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mostrarMain();
            }
        }, 3000); //tempo de delay

        //REMOVENDO ACTION BAR
        getSupportActionBar().hide();
    }

    //chamar a tela main
    private void mostrarMain(){
        Intent main = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(main);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
