package com.example.jorgebarraza.mercadomoya.Activities;

import android.app.ProgressDialog;
import android.app.UiAutomation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.jorgebarraza.mercadomoya.DB.Servicios;
import com.example.jorgebarraza.mercadomoya.Modelos.Articulo;
import com.example.jorgebarraza.mercadomoya.Modelos.Categoria;
import com.example.jorgebarraza.mercadomoya.R;
import com.example.jorgebarraza.mercadomoya.Utils.Utilerias;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CancellationException;

public class AltaDeCategoria extends AppCompatActivity {

    private EditText editNombre;
    private EditText editImagen;
    private ImageView imgImagen;
    private Button btnGuardar;
    private ProgressDialog progressDialog;
    private Context context;
    private String categoriaID;
    private RequestQueue request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_de_categoria);
        editNombre = findViewById(R.id.edtNombreCategoria);
        editImagen = findViewById(R.id.edtImagen);
        imgImagen = findViewById(R.id.imgCategoria);
        btnGuardar = findViewById(R.id.btnGuardarCategoria);
        context = AltaDeCategoria.this;
        Intent intent = getIntent();
        categoriaID = intent.getStringExtra("categoriaID");

        if(categoriaID != null){
            obtenerArticuloPorID();
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editNombre.getText().equals("")){
                    Categoria categoria = new Categoria();
                    categoria.setNombre(editNombre.getText().toString());
                    categoria.setImagenURL(editImagen.getText().toString());
                    guardarCategoria(categoria);
                }else{
                    Utilerias.mostrarToast(context,"Ingrese los campos necesarios para continuar");
                }

            }
        });

        editImagen.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus){
                    asignarFoto(editImagen.getText().toString());
                }
            }
        });
    }

    private void asignarFoto(String foto_url) {
        request = Volley.newRequestQueue(context);
        ImageRequest imageRequest = new ImageRequest(foto_url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imgImagen.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(contexto, "Error al obtener fotografia", Toast.LENGTH_SHORT).show();
            }
        });
        request.add(imageRequest);
    }

    private void guardarCategoria(final Categoria obj) {
        try {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Guardando datos...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = Servicios.crearActualizarCategoria();
            if (categoriaID != null) {
                obj.setCategoriaID(categoriaID);
            }
            String json = new Gson().toJson(obj);
            JSONObject jsonBody = new JSONObject(json);

            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (response.length() > 0) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
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

    private void obtenerArticuloPorID() {
        try {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Obteniendo datos...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = Servicios.obtenerCategoriaPorID();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("CategoriaID", categoriaID);

            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (response.length() > 0) {
                        ArrayList<Articulo> listArticulos = new ArrayList<Articulo>();
                        Gson gson = new GsonBuilder().create();
                        Categoria obj = gson.fromJson(String.valueOf(response), Categoria.class);
                        editNombre.setText(obj.getNombre());
                        editImagen.setText(obj.getImagenURL());
                        asignarFoto(obj.getImagenURL());
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
