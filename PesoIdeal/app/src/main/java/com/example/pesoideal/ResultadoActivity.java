package com.example.pesoideal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class ResultadoActivity extends AppCompatActivity {

    private TextView txt_situacao;
    private TextView txt_valor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        txt_valor = findViewById(R.id.text_ValorIMC);
        txt_situacao = findViewById(R.id.text_Situacao);

        @SuppressLint("WrongConstant") SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_APPEND);
        String valor = sh.getString("resultado", String.valueOf(txt_valor));
        String situacao = sh.getString("situacao", String.valueOf(txt_situacao));
        txt_valor.setText(valor);
        txt_situacao.setText(situacao);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(0);
    }
}
