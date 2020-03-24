package com.example.pesoideal;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mPeso;
    private EditText mAltura;
    private EditText mIdade;

    private RadioGroup radioGroup;
    private RadioButton menina;
    private RadioButton menino;
    private RadioButton adulto;

    private Button mCalcular;

    private TextView txt_Resultado;

    private TextView txt_Situacao;

    private double resultado;

    private SharedPreferences sharedPreferences;

    DecimalFormat formato = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        componentes();
    }

    public void componentes() {

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        mPeso = findViewById(R.id.edit_Peso);
        mAltura = findViewById(R.id.edit_Altura);
        mIdade = findViewById(R.id.edit_Idade);

        txt_Resultado = findViewById(R.id.resultado);
        txt_Situacao = findViewById(R.id.situacao);

        menina = findViewById(R.id.radio_Mulher);
        menina.setOnClickListener(this);
        menino = findViewById(R.id.radio_Homem);
        menino.setOnClickListener(this);
        adulto = findViewById(R.id.radio_Adulto);
        adulto.setOnClickListener(this);

        mCalcular = findViewById(R.id.btnCalcular);
        mCalcular.setOnClickListener(this);

        radioGroup = findViewById(R.id.radioGroup);
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == R.id.radio_Mulher) {
//
//                } else if (checkedId == R.id.radio_Homem) {
//
//                }
//            }
//        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        Log.d("Base ACtivity", "selectedId:" + selectedId);
        switch (view.getId()) {
            case R.id.btnCalcular:
                if (selectedId == R.id.radio_Adulto) {
                    imcAdulto();
                } else if (selectedId == R.id.radio_Homem) {
                    imcMenino();
                } else if (selectedId == R.id.radio_Mulher) {
                    imcMenina();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notificacao() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "notificacao";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "notificacao", NotificationManager.IMPORTANCE_MAX);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_error_black_24dp)
                        .setContentTitle("IMC foi calculado")
                        .setContentText("clique para ver o resultado");

        Intent notificationIntent = new Intent(this, ResultadoActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    public void setSharedPreferences() {
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("resultado", txt_Resultado.getText().toString());
        myEdit.putString("situacao", txt_Situacao.getText().toString());
        myEdit.commit();
    }

    public void calculoImc() {
        double peso = Float.parseFloat(mPeso.getText().toString());
        double altura = Float.parseFloat(mAltura.getText().toString());
        resultado = peso / (altura * altura);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void imcAdulto() {
        double idade = Float.parseFloat(mIdade.getText().toString());
        calculoImc();
        if (idade > 15) {
            if (resultado < 17) {
                notificacao();
                txt_Resultado.setText(formato.format(resultado));
                txt_Situacao.setText("Muito abaixo do peso");
                setSharedPreferences();
            }

            if ((resultado >= 17) && (resultado <= 18.49)) {
                notificacao();
                txt_Resultado.setText(formato.format(resultado));
                txt_Situacao.setText("Abaixo do peso");
                setSharedPreferences();
            }
            if ((resultado >= 18.5) && (resultado <= 24.99)) {
                notificacao();
                txt_Resultado.setText(formato.format(resultado));
                txt_Situacao.setText("Peso Normal");
                setSharedPreferences();
            }
            if ((resultado >= 25) && (resultado <= 29.99)) {
                notificacao();
                txt_Resultado.setText(formato.format(resultado));
                txt_Situacao.setText("Acima do peso");
                setSharedPreferences();
            }
            if ((resultado >= 30) && (resultado <= 34.99)) {
                notificacao();
                txt_Resultado.setText(formato.format(resultado));
                txt_Situacao.setText("Obesidade 1");
                setSharedPreferences();
            }
            if ((resultado >= 35) && (resultado <= 39.99)) {
                notificacao();
                txt_Resultado.setText(formato.format(resultado));
                txt_Situacao.setText("Obesidade 2");
                setSharedPreferences();
            }
            if (resultado > 40) {
                notificacao();
                txt_Resultado.setText(formato.format(resultado));
                txt_Situacao.setText("Obesidade 3");
                setSharedPreferences();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void imcMenina() {
        calculoImc();
        String idade;
        if (mIdade.getText() != null) {
            idade = mIdade.getText().toString();
            switch (idade) {
                case "6":
                    if ((resultado == 14.3) && (resultado <= 16)) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Normal");
                        setSharedPreferences();
                    }

                    if ((resultado > 16.1) && (resultado <= 17.3)) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Sobrepeso");
                        setSharedPreferences();
                    }
                    if (resultado > 17.4) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Obesidade");
                        setSharedPreferences();
                    }
                    break;
                case "7":
                    if ((resultado == 14.9) && (resultado <= 17)) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Normal");
                        setSharedPreferences();
                    }

                    if ((resultado > 17.1) && (resultado <= 18.8)) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Sobrepeso");
                        setSharedPreferences();
                    }
                    if (resultado > 18.9) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Obesidade");
                        setSharedPreferences();
                    }
                    break;

                case "8":
                    if ((resultado == 15.6) && (resultado <= 18)) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Normal");
                        setSharedPreferences();
                    }

                    if ((resultado > 18.1) && (resultado <= 20.2)) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Sobrepeso");
                        setSharedPreferences();
                    }
                    if (resultado > 20.3) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Obesidade");
                        setSharedPreferences();
                    }
                    break;
                case "9":
                    if ((resultado == 16.3) && (resultado <= 19)) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Normal");
                        setSharedPreferences();
                    }
                    if ((resultado > 19.1) && (resultado <= 21.6)) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Sobrepeso");
                        setSharedPreferences();
                    }
                    if (resultado > 21.7) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Obesidade");
                        setSharedPreferences();
                    }
                    break;
                case "10":
                    if ((resultado == 17) && (resultado <= 20)) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Normal");
                        setSharedPreferences();
                    }
                    if ((resultado > 20.1) && (resultado <= 23.1)) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Sobrepeso");
                        setSharedPreferences();
                    }
                    if (resultado > 23.2) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Obesidade");
                        setSharedPreferences();
                    }
                    break;
                case "11":
                    if ((resultado == 17.6) && (resultado <= 21)) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Normal");
                        setSharedPreferences();
                    }
                    if ((resultado > 21.1) && (resultado <= 24.4)) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Sobrepeso");
                        setSharedPreferences();
                    }
                    if (resultado > 24.5) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Obesidade");
                        setSharedPreferences();
                    }
                    break;
                case "12":
                    if ((resultado == 18.3) && (resultado <= 22)) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Normal");
                        setSharedPreferences();
                    }
                    if ((resultado > 22.1) && (resultado <= 25.8)) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Sobrepeso");
                        setSharedPreferences();
                    }
                    if (resultado > 25.9) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Obesidade");
                        setSharedPreferences();
                    }
                    break;
                case "13":
                    if ((resultado == 18.9) && (resultado <= 22.9)) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Normal");
                        setSharedPreferences();
                    }
                    if ((resultado > 23) && (resultado <= 27.6)) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Sobrepeso");
                        setSharedPreferences();
                    }
                    if (resultado > 27.7) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Obesidade");
                        setSharedPreferences();
                    }
                    break;
                case "14":
                    if ((resultado == 19.3) && (resultado <= 23.7)) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Normal");
                        setSharedPreferences();
                    }
                    if ((resultado > 23.8) && (resultado <= 27.8)) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Sobrepeso");
                        setSharedPreferences();
                    }
                    if (resultado > 27.9) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Obesidade");
                        setSharedPreferences();
                    }
                    break;
                case "15":
                    if ((resultado == 19.6) && (resultado <= 23.7)) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Normal");
                        setSharedPreferences();
                    }
                    if ((resultado > 24.2) && (resultado <= 28.7)) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Sobrepeso");
                        setSharedPreferences();
                    }
                    if (resultado > 28.8) {
                        notificacao();
                        txt_Resultado.setText(formato.format(resultado));
                        txt_Situacao.setText("Obesidade");
                        setSharedPreferences();
                    }
                    break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void imcMenino() {
        int idade = Integer.parseInt(mIdade.getText().toString());
        calculoImc();
        switch (idade) {
            case 6:
                if ((resultado == 14.5) && (resultado <= 16.5)) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                }

                if ((resultado > 16.6) && (resultado <= 17.9)) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                }
                if (resultado > 18) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                }
                break;
            case 7:
                if ((resultado == 15) && (resultado <= 17.2)) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                }

                if ((resultado > 17.3) && (resultado <= 19)) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                }
                if (resultado > 19.1) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                }
                break;
            case 8:
                if ((resultado == 15.6) && (resultado <= 16.6)) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                }

                if ((resultado > 16.7) && (resultado <= 20.2)) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                }
                if (resultado > 20.3) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                }
                break;
            case 9:
                if ((resultado == 16.1) && (resultado <= 18.7)) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                }
                if ((resultado > 18.8) && (resultado <= 21.3)) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                }
                if (resultado > 21.4) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                }
                break;
            case 10:
                if ((resultado == 16.7) && (resultado <= 19.5)) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                }
                if ((resultado > 19.6) && (resultado <= 22.4)) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                }
                if (resultado > 22.5) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                }
                break;
            case 11:
                if ((resultado == 17.2) && (resultado <= 20.2)) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                }
                if ((resultado >= 20.3) && (resultado <= 23.6)) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                }
                if (resultado > 23.7) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                }
                break;
            case 12:
                if ((resultado == 17.8) && (resultado <= 21)) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                }
                if ((resultado > 21.1) && (resultado <= 25.8)) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                }
                if (resultado > 25.9) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                }
                break;
            case 13:
                if ((resultado == 18.5) && (resultado <= 21.8)) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                }
                if ((resultado > 21.9) && (resultado <= 25.8)) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                }
                if (resultado > 25.9) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                }
                break;
            case 14:
                if ((resultado == 19.2) && (resultado <= 22.6)) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                }
                if ((resultado > 22.7) && (resultado <= 26.8)) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                }
                if (resultado > 26.9) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                }
                break;
            case 15:
                if ((resultado == 19.9) && (resultado <= 23.5)) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                }
                if ((resultado > 23.7) && (resultado <= 27.6)) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                }
                if (resultado > 27.7) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                }
                break;
        }
    }
}
