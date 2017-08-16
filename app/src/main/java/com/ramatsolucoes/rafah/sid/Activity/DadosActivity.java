package com.ramatsolucoes.rafah.sid.Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ramatsolucoes.rafah.sid.DataBase.DataBaseHelper;
import com.ramatsolucoes.rafah.sid.Model.Doacoes;
import com.ramatsolucoes.rafah.sid.R;

import java.util.ArrayList;

public class DadosActivity extends AppCompatActivity {
    //ATRIBUTOS
    Toolbar toolbar;
    DataBaseHelper dbDoacoes;
    int idDoador;
    TextView tnome, tcpf, tvalor, temail, ttel, tdata, tobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbdados);
        setSupportActionBar(toolbar);

        //INSTANCIANDO COMPONENTES
        toolbar = (Toolbar) findViewById(R.id.tbdados);
        dbDoacoes = new DataBaseHelper(this);
        tnome = (TextView) findViewById(R.id.tvNomeDados);
        tcpf = (TextView) findViewById(R.id.tvCPFDados);
        tvalor = (TextView) findViewById(R.id.tvValorDados);
        temail = (TextView) findViewById(R.id.tvEmailDados);
        ttel = (TextView) findViewById(R.id.tvTelefoneDados);
        tdata = (TextView) findViewById(R.id.tvDataDados);
        tobs = (TextView) findViewById(R.id.tvObservacoesDados);

        //TRATANDO OS DADOS RECEBIDOS DA ACTIVITY ANTERIOR
        Bundle extra = getIntent().getExtras();
        if (extra != null){
            idDoador = Integer.parseInt(extra.getString("id"));
        }

        //chamando a função para retornar dados do banco
        DadosDoacao(idDoador);

        //CONFIGURANDO A TOOLBAR
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void DadosDoacao(int id){
        Cursor select = dbDoacoes.RetornarDoacao(id);

        //tratanto o retorno do select
        if (select != null && select.getCount() > 0){
            while (select.moveToNext()){

                String ID         = select.getString(0).toString().trim();
                String NOME       = select.getString(1).toString().trim();
                String CPF        = select.getString(2).toString().trim();
                String VALOR      = select.getString(3).toString().trim();
                String EMAIL      = select.getString(4).toString().trim();
                String TELEFONE   = select.getString(5).toString().trim();
                String DATA       = select.getString(6).toString().trim();
                String OBSERVACAO = select.getString(7).toString().trim();

                Doacoes Cdoacoes = new Doacoes();
                Cdoacoes.setId(ID);
                Cdoacoes.setNome(NOME);
                Cdoacoes.setCpf(CPF);
                Cdoacoes.setValor(VALOR);
                Cdoacoes.setEmail(EMAIL);
                Cdoacoes.setTelefone(TELEFONE);
                Cdoacoes.setData(DATA);
                Cdoacoes.setObservacoes(OBSERVACAO);

                tnome.setText(Cdoacoes.getNome());
                tcpf.setText(Cdoacoes.getCpf());
                tvalor.setText(Cdoacoes.getValor());
                temail.setText(Cdoacoes.getEmail());
                ttel.setText(Cdoacoes.getTelefone());
                tdata.setText(Cdoacoes.getData());
                tobs.setText(Cdoacoes.getObservacoes());

            }
        } else {
            Toast.makeText(this, "Não foi possivel retornar os dados selecionados!", Toast.LENGTH_SHORT).show();
        }
    }

}
