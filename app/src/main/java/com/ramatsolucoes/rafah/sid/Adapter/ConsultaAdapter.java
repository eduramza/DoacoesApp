package com.ramatsolucoes.rafah.sid.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ramatsolucoes.rafah.sid.Model.Doacoes;
import com.ramatsolucoes.rafah.sid.R;

import java.util.ArrayList;

/**
 * Created by rafah on 09/08/2017.
 */

public class ConsultaAdapter extends ArrayAdapter<Doacoes> {
    //atributos
    private ArrayList<Doacoes> lista;
    private Context context;

    public ConsultaAdapter(@NonNull Context c, ArrayList<Doacoes> objetos) {
        super(c, 0,objetos);
        this.context = c;
        this.lista = objetos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        //vrifica se a lista está preenchida
        if (lista != null){
            //inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //monta a view a partir do xml
            view = inflater.inflate(R.layout.lista_doacoes, parent, false);

            //recuperando elemento para exibição
            TextView id = (TextView) view.findViewById(R.id.etID);
            TextView nome = (TextView) view.findViewById(R.id.etNome);
            TextView valor = (TextView) view.findViewById(R.id.etValor);

            //usando a classe modelo do banco
            Doacoes doacoes = lista.get(position);
            id.setText(doacoes.getId());
            nome.setText(doacoes.getNome());
            valor.setText(doacoes.getValor());

        }
        return view;
    }
}
