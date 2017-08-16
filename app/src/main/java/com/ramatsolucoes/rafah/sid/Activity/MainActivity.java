package com.ramatsolucoes.rafah.sid.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ramatsolucoes.rafah.sid.R;

public class MainActivity extends AppCompatActivity {
    //ATRIBUTOS
    Button Doacoes, Consulta, contato, Sair;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //REGRA DE TRANSIÇÃO DA ACTIVIE QUANDO ELA FOR CRIADA
        TransitionInflater tf = TransitionInflater.from(this);
        Transition t = tf.inflateTransition(R.transition.fade_anim);
            getWindow().setExitTransition(t);


        //instanciando os componentes
        Doacoes = (Button) findViewById(R.id.btn_doacoes);
        Consulta = (Button) findViewById(R.id.btn_consultaDoacoes);
        contato = (Button) findViewById(R.id.btn_faleconosco);
        Sair = (Button) findViewById(R.id.btn_sair);


        //EVENTO DOS BOTÕES
        Doacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DoacoesActivity.class);
                startActivity(intent);
                //fazendo transição com animação padrão do android
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        Consulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TRANSIÇÃO DE TELA EVOLUIDO
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, null);
                startActivity(new Intent(MainActivity.this, ConsultaActivity.class), compat.toBundle());
            }
        });

        contato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Em Breve!", Toast.LENGTH_SHORT).show();
            }
        });
        Sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
