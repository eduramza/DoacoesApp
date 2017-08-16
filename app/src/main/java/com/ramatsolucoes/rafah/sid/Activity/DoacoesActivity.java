package com.ramatsolucoes.rafah.sid.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ramatsolucoes.rafah.sid.DataBase.DataBaseHelper;
import com.ramatsolucoes.rafah.sid.Helper.Monetario;
import com.ramatsolucoes.rafah.sid.Model.Doacoes;
import com.ramatsolucoes.rafah.sid.R;
import java.text.SimpleDateFormat;
import java.util.Locale;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class DoacoesActivity extends AppCompatActivity {
    //ATRIBUTOS
    private Toolbar toolbar;
    DataBaseHelper dbDoacoes;
    EditText nome, cpf, valor, email, telefone, data, obs;
    TextInputLayout inputNome, inputCPF, inputValor, inputEmail, inputTelefone, inputData, inputObs;
    Button salvar;
    static final int REQUEST_SELECT_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doacoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbDoacoes);
        setSupportActionBar(toolbar);

        //ACESSANDO OS CONTATOS DO CELULAR
        SelecionarContato();

        //DEFININDO A LOCALIZAÇÃO PARA AS MASCARAS DE VALOR E DATA
        Locale meuLocal = new Locale("pt", "BR");

        //INSTANCIANDO OS COMPONENTES E OS INPUT TEXT
        toolbar = (Toolbar) findViewById(R.id.tbDoacoes);
        dbDoacoes = new DataBaseHelper(this);
        nome = (EditText) findViewById(R.id.etDoador);
        inputNome = (TextInputLayout) findViewById(R.id.tiNome);
        cpf = (EditText) findViewById(R.id.etCPF);
        inputCPF = (TextInputLayout) findViewById(R.id.tiCPF);
        valor = (EditText) findViewById(R.id.etValor);
        inputValor = (TextInputLayout) findViewById(R.id.tiValor);
        email = (EditText) findViewById(R.id.etEmail);
        inputEmail = (TextInputLayout) findViewById(R.id.tiEmail);
        telefone = (EditText) findViewById(R.id.etTelefone);
        inputTelefone = (TextInputLayout) findViewById(R.id.tiTelefone);
        data = (EditText) findViewById(R.id.etData);
        inputData = (TextInputLayout) findViewById(R.id.tiData);
        obs = (EditText) findViewById(R.id.etObservacoes);
        inputObs = (TextInputLayout) findViewById(R.id.tiObservacoes);
        salvar = (Button) findViewById(R.id.btSalvarDoacoes);

        //SETANDO OS FORMATADORES DE TEXTO PARA OS EDITS
        valor.addTextChangedListener(new Monetario(valor, meuLocal));

        //adicionando mascaras fixas com a biblioteca baixada no github
        MaskEditTextChangedListener mascaraCPF = new MaskEditTextChangedListener("###.###.###-##", cpf);

        //data.addTextChangedListener(mascaraData);
        cpf.addTextChangedListener(mascaraCPF);

        //CONFIGURANDO A DATA ATUAL NA TELA
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);
        data.setText(dateString);
        data.setEnabled(false);

        //toolbar
        toolbar.setTitle(R.string.toolbar_doacoes);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //EVENTO BOTOES
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalvarDoacoes();
            }
        });
    }

    public void SalvarDoacoes(){

        //verificando se os campos obrigatórios foram preenchidos
        int vDoador   = nome.getText().toString().trim().length();
        int vCPF      = cpf.getText().toString().trim().length();
        int vValor    = valor.getText().toString().trim().length();
        int vTelefone = telefone.getText().toString().trim().length();
        int vData     = data.getText().toString().trim().length();

        if (vDoador == 0 || vCPF == 0 || vValor == 0 || vTelefone == 0 || vData == 0){
            if (vDoador == 0){
                inputNome.setError("Este campo precisa ser preenchido");
            } if (vCPF == 0){
                inputCPF.setError("Este campo precisa ser preenchido");
            } if (vValor == 0){
                inputValor.setError("Este campo precisa ser preenchido");
            } if (vTelefone == 0){
                inputTelefone.setError("Este campo precisa ser preenchido");
            } if (vData == 0){
                inputData.setError("Este campo precisa ser preenchido");
            }
            Toast.makeText(this, "Esses campos são obrigatórios!", Toast.LENGTH_SHORT).show();
        } else {

            //retornando em variaveis
            String sNome = nome.getText().toString();
            String sCPF = cpf.getText().toString();
            String sValor = valor.getText().toString();
            String sEmail = email.getText().toString();
            String sTel = telefone.getText().toString();
            String sData = data.getText().toString();
            String sObs = obs.getText().toString();

            //PASSANDO OS DADOS PARA O MODEL DOAÇÕES
            Doacoes doacoes = new Doacoes();
            doacoes.setNome(sNome);
            doacoes.setCpf(sCPF);
            doacoes.setValor(sValor);
            doacoes.setEmail(sEmail);
            doacoes.setTelefone(sTel);
            doacoes.setData(sData);
            doacoes.setObservacoes(sObs);

            //salvando os dados no banco
            Boolean retorno = salvarDados(sNome, sCPF, sValor, sEmail, sTel, sData, sObs);

            if (retorno == true) {
                Alertar("Dados gravados com sucesso!");

            } else {
                Alertar("Desculpe! Não possivel cadastrar a doação, verifique os erros");
            }
        }
    }

    public void SelecionarContato(){
        Intent contatos = new Intent(Intent.ACTION_PICK); //CHAMANDO UMA ACTIVITY COM A CONSTANTE DE ESCOLHER UM DADO A SER RETORNADO
        contatos.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); //SELECIONANDO O CONTEUDO UTILIZANDO A CONTACTS PROVIDER

        //VALIDANDO
        if (contatos.resolveActivity(getPackageManager()) != null){
            //CHAMANDO OS CONTATOS
            startActivityForResult(contatos, REQUEST_SELECT_CONTACT);

        }
    }

    //TRATANDO O RESULTADO DO RETORNO DOS CONTATOS
    protected void onActivityResult(int RequestCode, int ResultCode, Intent Data){
        if (RequestCode == REQUEST_SELECT_CONTACT && ResultCode == RESULT_OK){ //O ULTIMO PARAMETRO É PARA CASO O USUARIO CANCELE
            //Pegar a URI e a Query do contactProvider e o numero do telefone
            Uri contatoUri = Data.getData();
            String[] projecao = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

            final int INDEX_NUMBER = 0;
            final int INDEX_NAME = 1;

            Cursor cursor = getContentResolver().query(contatoUri, projecao, null, null, null);

            //SE O CURSOR RETORNAR UM VALOR VALIDO ENTÃO PEGA O NUMERO
            if (cursor != null && cursor.moveToFirst()){

                String number   = cursor.getString(INDEX_NUMBER);
                String name     = cursor.getString(INDEX_NAME);

                //ação do que recebe o numero do contato e envia para a activity
                telefone.setText(number);
                telefone.setEnabled(false);
                nome.setText(name);
                nome.setEnabled(false);
                cursor.close();

            }
        } else {
            //ADICIONANDO MASCARA DE TELEFONE QUANDO NÃO TIVER SIDO SELECIONADO
            MaskEditTextChangedListener mascaraTel = new MaskEditTextChangedListener("(##)#####-####", telefone);
            telefone.addTextChangedListener(mascaraTel);
        }
    }

    private boolean salvarDados(String nome, String cpf, String valor, String email, String tel, String data, String obs){
        try{
            //chamada da função de inserir dados do banco
            dbDoacoes.InserirDados(nome,
                    cpf,
                    valor,
                    email,
                    tel,
                    data,
                    obs);
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    private void Alertar(String resultado){
        AlertDialog.Builder alert = new AlertDialog.Builder(DoacoesActivity.this);

        //configurando o alerta
        alert.setTitle("Cadastro de Doações");
        alert.setMessage(resultado);
        alert.setCancelable(false);

        alert.setNeutralButton("Obrigado", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        //materializando o alert
        alert.create();
        alert.show();
    }
}
