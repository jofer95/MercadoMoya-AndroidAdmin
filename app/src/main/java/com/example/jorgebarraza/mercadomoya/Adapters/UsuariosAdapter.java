package com.example.jorgebarraza.mercadomoya.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.jorgebarraza.mercadomoya.Activities.AltaDeArticuloActivity;
import com.example.jorgebarraza.mercadomoya.Activities.RegistrarseActivity;
import com.example.jorgebarraza.mercadomoya.Modelos.Articulo;
import com.example.jorgebarraza.mercadomoya.Modelos.Usuario;
import com.example.jorgebarraza.mercadomoya.R;

import java.util.ArrayList;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.ViewHolderUsuarios> {
    private ArrayList<Usuario> listaUsuarios;
    private RequestQueue request;

    public UsuariosAdapter(ArrayList<Usuario> listaDatos) {
        this.listaUsuarios = listaDatos;
        setHasStableIds(true);
    }

    @Override
    public ViewHolderUsuarios onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario, null);
        return new ViewHolderUsuarios(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderUsuarios holder, int position) {
        holder.asignarUsuarios(listaUsuarios.get(position));
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public class ViewHolderUsuarios extends RecyclerView.ViewHolder {
        TextView nombre, correo;
        ImageView imagen;
        View estatus;
        Context contexto;

        public ViewHolderUsuarios(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.tvNombreUsuario);
            correo = (TextView) itemView.findViewById(R.id.tvCorreoUsuario);
            imagen = itemView.findViewById(R.id.idFoto);
            estatus = (View) itemView.findViewById(R.id.idEstatus);
            contexto = itemView.getContext();
            request = Volley.newRequestQueue(contexto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = getLayoutPosition();
                    Intent intent = new Intent(contexto, RegistrarseActivity.class);
                    intent.putExtra("correo", listaUsuarios.get(itemPosition).getCorreo());
                    intent.putExtra("usuarioID", listaUsuarios.get(itemPosition).getUsuarioID());
                    contexto.startActivity(intent);
                    /*if(MainActivity.asistenciaPorAlumno){
                        Intent intent = new Intent(contexto, AlumnoDetalle.class);
                        intent.putExtra("nombre", listaArticulos.get(itemPosition).nombreCompleto());
                        intent.putExtra("numeroControl", listaArticulos.get(itemPosition).getNcontrol());
                        contexto.startActivity(intent);
                    }else{
                        if(listaArticulos.get(itemPosition).getEstatus() == 1){
                            estatus.setBackgroundColor(Color.RED);
                            listaArticulos.get(itemPosition).setEstatus(2);
                        }else{
                            estatus.setBackgroundColor(Color.GREEN);
                            listaArticulos.get(itemPosition).setEstatus(1);
                        }
                    }*/
                }
            });
        }

        public void asignarUsuarios(Usuario usuario) {
            nombre.setText(usuario.getNombre());
            correo.setText(usuario.getCorreo());
            //asignarFoto(Servicios.obtenerFotografia(articulo.getNcontrol()));
        }

        private void asignarFoto(String foto_url) {
            ImageRequest imageRequest = new ImageRequest(foto_url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    imagen.setImageBitmap(response);
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

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
