package com.example.jorgebarraza.mercadomoya.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jorgebarraza.mercadomoya.Adapters.ArticulosAdapter;
import com.example.jorgebarraza.mercadomoya.DB.Servicios;
import com.example.jorgebarraza.mercadomoya.Modelos.Articulo;
import com.example.jorgebarraza.mercadomoya.Modelos.Categoria;
import com.example.jorgebarraza.mercadomoya.R;
import com.example.jorgebarraza.mercadomoya.Utils.Utilerias;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AltaDeArticuloActivity extends AppCompatActivity {

    private String articuloID = "";
    private Context context;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialog2;
    private EditText editNombre;
    private EditText editDescripcion;
    private EditText editPrecio;
    private EditText editImagenURL;
    private Spinner spnCategorias;
    private Button btnGuardar;
    private ImageView image;
    private RequestQueue request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_de_articulo);
        editDescripcion = findViewById(R.id.edtDescripcion);
        editPrecio = findViewById(R.id.edtPrecio);
        editImagenURL = findViewById(R.id.edtImagen);
        spnCategorias = findViewById(R.id.spnCategoria);
        btnGuardar = findViewById(R.id.btnGuardarArticulo);
        editNombre = findViewById(R.id.edtNombre);
        image = findViewById(R.id.imgImagenProducto);
        setTitle("Alta de articulo");
        Intent intent = getIntent();
        articuloID = intent.getStringExtra("articuloID");
        context = AltaDeArticuloActivity.this;
        obtenerCategorias();

        //Si tenemos el ID del articulo, consultar para modificar
        if (articuloID != null) {
            obtenerArticuloPorID();
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Articulo articulo = new Articulo();
                if (editNombre.getText().equals("") || editDescripcion.getText().equals("") || editImagenURL.getText().equals("") || editPrecio.getText().equals("")) {
                    Utilerias.mostrarToast(context, "Faltan campos por completar");
                } else {
                    articulo.setNombre(editNombre.getText().toString());
                    articulo.setDescripcion(editDescripcion.getText().toString());
                    articulo.setCategoria(String.valueOf(spnCategorias.getSelectedItem()));
                    articulo.setPrecio(Double.valueOf(editPrecio.getText().toString()));
                    articulo.setImagenURL(editImagenURL.getText().toString());
                    guardarArticulo(articulo);
                }
            }
        });

        editImagenURL.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus){
                    asignarFoto(editImagenURL.getText().toString());
                }
            }
        });
    }

    private void obtenerArticuloPorID() {
        try {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Obteniendo datos...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = Servicios.obtenerArticuloPorID();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("ArticuloID", articuloID);

            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (response.length() > 0) {
                        ArrayList<Articulo> listArticulos = new ArrayList<Articulo>();
                        Gson gson = new GsonBuilder().create();
                        Articulo arti = gson.fromJson(String.valueOf(response), Articulo.class);
                        editNombre.setText(arti.getNombre());
                        editDescripcion.setText(arti.getDescripcion());
                        editPrecio.setText(arti.getPrecio().toString());
                        editImagenURL.setText(arti.getImagenURL());
                        asignarFoto(arti.getImagenURL());
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

    private void guardarArticulo(final Articulo articulo) {
        try {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Guardando datos...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = Servicios.crearActualizarArticulo();
            if (articuloID != null) {
                articulo.setArticuloID(articuloID);
            }
            String jsonArticulo = new Gson().toJson(articulo);
            JSONObject jsonBody = new JSONObject(jsonArticulo);

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

    private void obtenerCategorias() {
        progressDialog2 = new ProgressDialog(context);
        progressDialog2.setMessage("Obteniendo categorias...");
        progressDialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog2.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Servicios.obtenerTodasLasCategorias();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                ArrayList<String> lista = new ArrayList<String>();
                                Gson gson = new GsonBuilder().create();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Categoria obj = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), Categoria.class);
                                    lista.add(obj.getNombre());
                                    //baseDeDatos.insertOrReplaceAlumnos(alumno);
                                }
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                                        android.R.layout.simple_spinner_item, lista);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spnCategorias.setAdapter(dataAdapter);
                                if(progressDialog2 != null){
                                    progressDialog2.dismiss();
                                    progressDialog2 = null;
                                }
                            } else {
                                Toast.makeText(context, "Categorias no disponibles", Toast.LENGTH_LONG).show();
                                if(progressDialog2 != null){
                                    progressDialog2.dismiss();
                                    progressDialog2 = null;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Ocurrio un error al procesar los datos", Toast.LENGTH_LONG).show();
                            if(progressDialog2 != null){
                                progressDialog2.dismiss();
                                progressDialog2 = null;
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(progressDialog2 != null){
                            progressDialog2.dismiss();
                            progressDialog2 = null;
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("name", "Alif");
                //params.put("domain", "http://itsalif.info");

                return params;
            }
        };
        queue.add(postRequest);
    }

    private void asignarFoto(String foto_url) {
        request = Volley.newRequestQueue(context);
        ImageRequest imageRequest = new ImageRequest(foto_url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                image.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(contexto, "Error al obtener fotografia", Toast.LENGTH_SHORT).show();
            }
        });
        request.add(imageRequest);
    }
}
