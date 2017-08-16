package com.ramatsolucoes.rafah.sid.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;

import java.util.Date;

/**
 * Created by rafah on 05/08/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    //ATRIBUTOS DO BANCO
    public static final String DATABASE_NAME = "DoacoesApp.db";
    public static final String TABLE_NAME = "doacoes";

    public static final String ID = "id";
    public static final String NOME = "nome";
    public static final String CPF = "cpf";
    public static final String VALOR = "valor";
    public static final String EMAIL = "email";
    public static final String TELEFONE = "telefone";
    public static final String DATA = "data";
    public static final String OBSERVACOES = "observacoes";
    public static final String FOTO = "foto";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NOME TEXT, CPF TEXT, VALOR TEXT, " +
                "EMAIL TEXT, TELEFONE TEXT, DATA DATE, OBSERVACOES TEXT, FOTO CLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }

    public boolean InserirDados(String nome, String cpf, String valor, String email, String telefone, String data, String obs){
        SQLiteDatabase db = this.getWritableDatabase(); //abrindo o banco para escrita e edição

        //VARIAVEL CONTENT QUE RECEBERÁ OS DADOS PARA ARMAZENAR
        ContentValues values = new ContentValues();
        values.put(NOME, nome);
        values.put(CPF, cpf);
        values.put(VALOR, valor);
        values.put(EMAIL, email);
        values.put(TELEFONE, telefone);
        values.put(DATA, data);
        values.put(OBSERVACOES, obs);

        //mostrando resultado
        long resultado = db.insert(TABLE_NAME, null, values);
        db.close();

        //checando o sucesso da inserção
        if (resultado == -1){
            return false;
        } else {
            return true;
        }
    }

    public Cursor RetornarTodosDados(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor select = db.rawQuery("SELECT * FROM "+TABLE_NAME+" ORDER BY ID DESC", null);
        return select;
    }

    public Cursor RetornarDoacao(int iID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor select = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE ID = "+iID, null);
        return select;
    }

    public boolean InserindoImagem(MediaStore.Images.Media foto){

        return true;
    }

    public Integer ExcluirDoacao(String iID){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_NAME, " ID =?", new String[]{iID});

        return i;
    }
}
