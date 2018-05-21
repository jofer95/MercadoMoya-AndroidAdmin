package com.example.jorgebarraza.mercadomoya.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.jorgebarraza.mercadomoya.R;
import com.example.jorgebarraza.mercadomoya.Utils.Utilerias;

public class MainActivity extends AppCompatActivity {

    private EditText edtUsuario;
    private EditText edtContra;
    private Button btnIniciarSesion;
    private Button btnRegistrarse;
    private CheckBox checkRecordar;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        getSupportActionBar().hide();

        //Finds
        edtUsuario = findViewById(R.id.edtUsuario);
        edtContra = findViewById(R.id.edtContra);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        checkRecordar = findViewById(R.id.chRecordar);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtUsuario.getText().equals("") && !edtContra.equals("")){
                    Intent intent = new Intent(context,MenuPrincipalActivity.class);
                    startActivity(intent);

                }else{
                    Utilerias.mostrarToast(context,"Ingrese un usuario y contraseña");
                }
            }
        });
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,RegistrarseActivity.class);
                startActivity(intent);
            }
        });



    }
}
