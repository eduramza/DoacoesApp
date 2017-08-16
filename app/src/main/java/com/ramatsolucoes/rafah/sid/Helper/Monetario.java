package com.ramatsolucoes.rafah.sid.Helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by rafah on 06/08/2017.
 * referencia da utilização e criação da classe = https://pt.stackoverflow.com/questions/138841/mascara-de-moedas-no-campo
 */

public class Monetario implements TextWatcher {
    private final WeakReference<EditText> editTextWeakReference;
    private final Locale locale; //essa classe serve para ajudar na implementação da mascara com base na região país

    public Monetario (EditText edit, Locale local){
        this.editTextWeakReference = new WeakReference<EditText>(edit);
        this.locale = local != null ? local : local.getDefault();
    }

    public  Monetario (EditText edit){
        this.editTextWeakReference = new WeakReference<EditText>(edit);
        this.locale = Locale.getDefault();
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        EditText edit = editTextWeakReference.get();
        if (edit == null) return;

        edit.removeTextChangedListener(this);

        BigDecimal analisado = TransformaDecimal(s.toString(), locale);
        String formatado = NumberFormat.getCurrencyInstance(locale).format(analisado);

        //configurando o edit recebido
        edit.setText(formatado);
        edit.setSelection(formatado.length());
        edit.addTextChangedListener(this);
    }

    //metodo que transforma em moeda de acordo com a região
    private BigDecimal TransformaDecimal(String valor, Locale local){
        String substituivel = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance(local).getCurrency().getSymbol()); //para trazer o simbolo .getSymbol()
        //.getSymbol()
        String limpo = valor.replaceAll(substituivel, "");

        return new BigDecimal(limpo).setScale(2, BigDecimal.ROUND_FLOOR)
                .divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
    }
}
