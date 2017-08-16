package com.ramatsolucoes.rafah.sid.Activity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ramatsolucoes.rafah.sid.Adapter.ConsultaAdapter;
import com.ramatsolucoes.rafah.sid.DataBase.DataBaseHelper;
import com.ramatsolucoes.rafah.sid.Model.Doacoes;
import com.ramatsolucoes.rafah.sid.R;

import java.util.ArrayList;

public class ConsultaActivity extends AppCompatActivity {
    //atributos
    DataBaseHelper dbDoacoes;
    ListView listView;
    ArrayAdapter adapter;
    ArrayList<Doacoes> doacoes;
    String delID;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        //CONFIGURANDO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarC);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //DEFININDO A TRANSIÇÃO DE ENTRADA DA ACTIVITY
        Slide s = new Slide();
        s.setDuration(1000);
        getWindow().setEnterTransition(s);

        //INSTACIANDO OS COMPONENTES
        dbDoacoes = new DataBaseHelper(this);
        doacoes = new ArrayList<>();
        listView = (ListView) findViewById(R.id.lvDoacoes);

        //configurando o Adapter
        adapter = new ConsultaAdapter(ConsultaActivity.this, doacoes);
        listView.setAdapter(adapter);

        RecuperarDados();

        //ADICIONAR EVENTO DE CLICK PARA CHAMAR A ACTIVITY DE DETALHES
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Doacoes doacoesModel = doacoes.get(position);
                    Intent intent = new Intent(ConsultaActivity.this, DadosActivity.class);
                    intent.putExtra("id", doacoesModel.getId());
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                } catch (Exception e){
                    e.printStackTrace();
                    Log.i("Erro", e.getMessage());
                }

            }
        });

        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialog(position);
                return true;
            }
        });

    }

    private void RecuperarDados() {
        Cursor select = dbDoacoes.RetornarTodosDados();

        //verificando o reftorno
        if (select != null && select.getCount() > 0){

            //boas praticas
            doacoes.clear();

            while (select.moveToNext()) {

                String bdID = select.getString(0).toString().trim();
                String bdNome = select.getString(1).toString().trim();
                String bdValor = select.getString(3).toString().trim();

                Doacoes Cdoacoes = new Doacoes();
                Cdoacoes.setId(bdID);
                Cdoacoes.setNome(bdNome);
                Cdoacoes.setValor(bdValor);

                doacoes.add(Cdoacoes);
            }
        } else {
            Toast.makeText(this, "Problemas ao tentar recuperar os registros do banco!", Toast.LENGTH_SHORT).show();
        }
    }

    public void alertDialog(final int position){
        //CONFIGURAÇÃO DO ALERT DIALOG DENTRO DO EVENTO DE CLICK
        AlertDialog.Builder alert = new AlertDialog.Builder(ConsultaActivity.this);

        alert.setTitle("Exclusão de Doação");
        alert.setMessage("Deseja excluir esta doação?");
        alert.setCancelable(false);

        Doacoes doacoesModel = doacoes.get(position);
        delID = doacoesModel.getId();

        //AÇÃO DO ALERT DIALOG
        alert.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //EXCLUINDO A DOAÇÃO
                int resultDel = dbDoacoes.ExcluirDoacao(delID);

                if (resultDel == 1){
                    doacoes.remove(position); //removendo da lista também
                    adapter.notifyDataSetChanged();
                    Toast.makeText(ConsultaActivity.this, "Removido com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ConsultaActivity.this, "Não foi possivel excluir a Doação"+resultDel, Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        //materializando o alert
        alert.create();
        alert.show();
    }
}
