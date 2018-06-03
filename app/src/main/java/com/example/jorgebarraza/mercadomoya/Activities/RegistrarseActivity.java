package com.example.jorgebarraza.mercadomoya.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.jorgebarraza.mercadomoya.DB.Servicios;
import com.example.jorgebarraza.mercadomoya.Modelos.Articulo;
import com.example.jorgebarraza.mercadomoya.Modelos.Usuario;
import com.example.jorgebarraza.mercadomoya.R;
import com.example.jorgebarraza.mercadomoya.Utils.Utilerias;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistrarseActivity extends AppCompatActivity {
    private EditText editNombre, editCorreo, editDireccion, editContra;
    private Switch swAdmin;
    private Button btnGuardar;
    private Context context;
    private ProgressDialog progressDialog;
    private String usuarioID = "";
    private String correoID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        editContra = findViewById(R.id.edtContrasena);
        editNombre = findViewById(R.id.edtNombre);
        editDireccion = findViewById(R.id.edtDireccion);
        editCorreo = findViewById(R.id.edtCorreo);
        swAdmin = findViewById(R.id.swAdministrador);
        btnGuardar = findViewById(R.id.btnRegistrarse);
        context = RegistrarseActivity.this;

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editCorreo.getText().equals("") || editNombre.getText().equals("")){
                    Utilerias.mostrarToast(context,"Ingrese nombre y correo para continuar");
                }else{
                    Usuario usuario = new Usuario();
                    usuario.setNombre(editNombre.getText().toString());
                    usuario.setDireccion(editDireccion.getText().toString());
                    usuario.setContrasena(editContra.getText().toString());
                    usuario.setCorreo(editCorreo.getText().toString());
                    if(swAdmin.isChecked()){
                        usuario.setRol(1);
                    }else{
                        usuario.setRol(0);
                    }

                    guardarUsuario(usuario);
                }
            }
        });

        setTitle("Pantalla de usuario");
        Intent intent = getIntent();
        usuarioID = intent.getStringExtra("usuarioID");
        correoID = intent.getStringExtra("correo");
        if(correoID != null){
            obtenerUsuarioPorCorreo();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    private void guardarUsuario(final Usuario usuario) {
        try {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Guardando datos...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = Servicios.crearActualizarUsuario();
            if (usuarioID != null) {
                usuario.setUsuarioID(usuarioID);
            }
            String jsonArticulo = new Gson().toJson(usuario);
            JSONObject jsonBody = new JSONObject(jsonArticulo);

            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (response.length() > 0) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        Utilerias.mostrarToast(context,"Datos guardados correctamente!");
                        finish();
                    } else {
                        Toast.makeText(context, "Datos no disponibles", Toast.LENGTH_LONG).show();
                        if(progressDialog != null){
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utilerias.mostrarToast(context, "Error al procesar la peticion");
                    if(progressDialog != null){
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    // headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            queue.add(jsonOblect);
            //VolleyApplication.getInstance().addToRequestQueue(jsonOblect);
        } catch (Exception ex) {
            Utilerias.mostrarToast(context, "Error al procesar la peticion");
            if(progressDialog != null){
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }

    private void obtenerUsuarioPorCorreo() {
        try {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Obteniendo datos...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = Servicios.obtenerUsuarioPorCorreo();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Correo", correoID);

            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (response.length() > 0) {
                        ArrayList<Articulo> listArticulos = new ArrayList<Articulo>();
                        Gson gson = new GsonBuilder().create();
                        Usuario resp = gson.fromJson(String.valueOf(response), Usuario.class);
                        editNombre.setText(resp.getNombre());
                        editContra.setText(resp.getContrasena());
                        editDireccion.setText(resp.getDireccion());
                        editCorreo.setText(resp.getCorreo());
                        if(resp.getRol() == 1){
                            swAdmin.setChecked(true);
                        }else{
                            swAdmin.setChecked(false);
                        }
                        if(progressDialog != null){
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                    } else {
                        Toast.makeText(context, "Datos no disponibles", Toast.LENGTH_LONG).show();
                        if(progressDialog != null){
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utilerias.mostrarToast(context, "Error al procesar la peticion");
                    if(progressDialog != null){
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    // headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            queue.add(jsonOblect);
            //VolleyApplication.getInstance().addToRequestQueue(jsonOblect);
        } catch (Exception ex) {
            Utilerias.mostrarToast(context, "Error al procesar la peticion");
            if(progressDialog != null){
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }
}
