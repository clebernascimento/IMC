package com.example.pesoideal;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

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
                txt_Resultado.setText(formato.format(resultado));
                txt_Situacao.setText("Muito abaixo do peso");
                setSharedPreferences();
                notificacao();
            }

            if ((resultado >= 17) && (resultado <= 18.49)) {
                txt_Resultado.setText(formato.format(resultado));
                txt_Situacao.setText("Abaixo do peso");
                setSharedPreferences();
                notificacao();
            }
            if ((resultado >= 18.5) && (resultado <= 24.99)) {
                txt_Resultado.setText(formato.format(resultado));
                txt_Situacao.setText("Peso Normal");
                setSharedPreferences();
                notificacao();
            }
            if ((resultado >= 25) && (resultado <= 29.99)) {
                txt_Resultado.setText(formato.format(resultado));
                txt_Situacao.setText("Acima do peso");
                setSharedPreferences();
                notificacao();
            }
            if ((resultado >= 30) && (resultado <= 34.99)) {
                txt_Resultado.setText(formato.format(resultado));
                txt_Situacao.setText("Obesidade 1");
                setSharedPreferences();
                notificacao();
            }
            if ((resultado >= 35) && (resultado <= 39.99)) {
                txt_Resultado.setText(formato.format(resultado));
                txt_Situacao.setText("Obesidade 2");
                setSharedPreferences();
                notificacao();
            }
            if (resultado > 40) {
                txt_Resultado.setText(formato.format(resultado));
                txt_Situacao.setText("Obesidade 3");
                setSharedPreferences();
                notificacao();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void imcMenina() {
        int idade = Integer.parseInt(mIdade.getText().toString());
        switch (idade) {
            case 6:
                calculoImc();
                if (resultado < 14.3) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Abaixo do peso");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado == 14.3) && (resultado <= 16)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado > 16.1) && (resultado <= 17.3)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                    notificacao();
                } else if (resultado > 17.4) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                    notificacao();
                }
                break;
            case 7:
                calculoImc();
                if (resultado < 14.9) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Abaixo do peso");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado == 14.9) && (resultado <= 17)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado > 17.1) && (resultado <= 18.8)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                    notificacao();
                } else if (resultado > 18.9) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                    notificacao();
                }
                break;

            case 8:
                calculoImc();
                if (resultado < 15.6) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Abaixo do peso");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado == 15.6) && (resultado <= 18)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado > 18.1) && (resultado <= 20.2)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                    notificacao();
                } else if (resultado > 20.3) {
                    notificacao();
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                    notificacao();
                }
                break;
            case 9:
                calculoImc();
                if (resultado < 16.3) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Abaixo do peso");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado == 16.3) && (resultado <= 19.1)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado > 19.1) && (resultado <= 21.7)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                    notificacao();
                } else if (resultado > 21.7) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                    notificacao();
                }
                break;
            case 10:
                calculoImc();
                if (resultado < 17) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Abaixo do peso");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado == 17) && (resultado <= 20)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado > 20.1) && (resultado <= 23.1)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                    notificacao();
                } else if (resultado > 23.2) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                    notificacao();
                }
                break;
            case 11:
                calculoImc();
                if (resultado < 17.6) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Abaixo do peso");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado == 17.6) && (resultado <= 21)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado > 21.1) && (resultado <= 24.4)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                    notificacao();
                } else if (resultado > 24.5) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                    notificacao();
                }
                break;
            case 12:
                calculoImc();
                if (resultado < 18.3) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Abaixo do peso");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado == 18.3) && (resultado <= 22)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado > 22.1) && (resultado <= 25.8)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                    notificacao();
                } else if (resultado > 25.9) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                    notificacao();
                }
                break;
            case 13:
                calculoImc();
                if (resultado < 18.9) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Abaixo do peso");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado == 18.9) && (resultado <= 22.9)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado > 23) && (resultado <= 27.6)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                    notificacao();
                } else if (resultado > 27.7) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                    notificacao();
                }
                break;
            case 14:
                calculoImc();
                if (resultado < 19.3) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Abaixo do peso");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado == 19.3) && (resultado <= 23.7)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado > 23.8) && (resultado <= 27.8)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                    notificacao();
                } else if (resultado > 27.9) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                    notificacao();
                }
                break;
            case 15:
                calculoImc();
                if (resultado < 19.6) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Abaixo do peso");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado == 19.6) && (resultado <= 23.7)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado > 24.2) && (resultado <= 28.7)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                    notificacao();
                } else if (resultado > 28.8) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                    notificacao();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void imcMenino() {
        int idade = Integer.parseInt(mIdade.getText().toString());
        switch (idade) {
            case 6:
                calculoImc();
                if (resultado < 14.5) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Abaixo do peso");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado <= 14.5) && (resultado <= 16.5)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado > 16.6) && (resultado <= 17.9)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                    notificacao();
                } else if (resultado > 18) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                    notificacao();
                }
                break;
            case 7:
                calculoImc();
                if (resultado < 15) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Abaixo do peso");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado == 15) && (resultado <= 17.2)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado > 17.3) && (resultado <= 19)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                    notificacao();
                } else if (resultado > 19.1) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                    notificacao();
                }
                break;
            case 8:
                calculoImc();
                if (resultado < 15.6) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Abaixo do peso");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado == 15.6) && (resultado <= 16.6)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado > 16.7) && (resultado <= 20.2)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                    notificacao();
                } else if (resultado > 20.3) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                    notificacao();
                }
                break;
            case 9:
                calculoImc();
                if (resultado < 16.1) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Abaixo do peso");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado == 16.1) && (resultado <= 18.8)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado > 18.8) && (resultado <= 21.4)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                    notificacao();
                } else if (resultado > 21.4) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                    notificacao();
                }
                break;
            case 10:
                calculoImc();
                if (resultado < 16.7) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Abaixo do peso");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado == 16.7) && (resultado <= 19.5)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado > 19.6) && (resultado <= 22.4)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                    notificacao();
                } else if (resultado > 22.5) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                    notificacao();
                }
                break;
            case 11:
                calculoImc();
                if (resultado < 17.2) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Abaixo do peso");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado == 17.2) && (resultado <= 20.2)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado >= 20.3) && (resultado <= 23.6)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                    notificacao();
                } else if (resultado > 23.7) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                    notificacao();
                }
                break;
            case 12:
                calculoImc();
                if (resultado < 17.8) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Abaixo do peso");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado == 17.8) && (resultado <= 21)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado > 21.1) && (resultado <= 25.8)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                    notificacao();
                } else if (resultado > 25.9) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                    notificacao();
                }
                break;
            case 13:
                calculoImc();
                if (resultado < 18.5) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Abaixo do peso");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado == 18.5) && (resultado <= 21.8)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado > 21.9) && (resultado <= 25.8)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                    notificacao();
                } else if (resultado > 25.9) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                    notificacao();
                }
                break;
            case 14:
                calculoImc();
                if (resultado < 19.2) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Abaixo do peso");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado == 19.2) && (resultado <= 22.6)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado > 22.7) && (resultado <= 26.8)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                    notificacao();
                } else if (resultado > 26.9) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                    notificacao();
                }
                break;
            case 15:
                calculoImc();
                if (resultado < 19.95) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Abaixo do peso");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado == 19.9) && (resultado <= 23.5)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Normal");
                    setSharedPreferences();
                    notificacao();
                } else if ((resultado > 23.7) && (resultado <= 27.6)) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Sobrepeso");
                    setSharedPreferences();
                    notificacao();
                } else if (resultado > 27.7) {
                    txt_Resultado.setText(formato.format(resultado));
                    txt_Situacao.setText("Obesidade");
                    setSharedPreferences();
                    notificacao();
                }
                break;
        }
    }
}
