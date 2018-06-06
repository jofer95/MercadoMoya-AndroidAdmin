package com.example.jorgebarraza.mercadomoya.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.example.jorgebarraza.mercadomoya.Activities.DetallesPedidoActv;
import com.example.jorgebarraza.mercadomoya.Modelos.Articulo;
import com.example.jorgebarraza.mercadomoya.Modelos.Pedido;
import com.example.jorgebarraza.mercadomoya.R;

import java.util.ArrayList;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.ViewHolderArticulos> {
    private ArrayList<Pedido> listaArticulos;
    private RequestQueue request;

    public PedidosAdapter(ArrayList<Pedido> listaDatos) {
        this.listaArticulos = listaDatos;
        setHasStableIds(true);
    }

    @Override
    public PedidosAdapter.ViewHolderArticulos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, null);
        return new PedidosAdapter.ViewHolderArticulos(view);
    }

    @Override
    public void onBindViewHolder(PedidosAdapter.ViewHolderArticulos holder, int position) {
        holder.asignarArticulos(listaArticulos.get(position));
    }

    @Override
    public int getItemCount() {
        return listaArticulos.size();
    }

    public class ViewHolderArticulos extends RecyclerView.ViewHolder {
        TextView nombre, pagado,total;
        ImageView foto;
        View estatus;
        Context contexto;

        public ViewHolderArticulos(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.tvUsuario);
            pagado = (TextView) itemView.findViewById(R.id.tvPagado);
            total = (TextView) itemView.findViewById(R.id.tvTotalPedido);
            foto = (ImageView) itemView.findViewById(R.id.idFoto);
            estatus = (View) itemView.findViewById(R.id.idEstatus);
            contexto = itemView.getContext();
            request = Volley.newRequestQueue(contexto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = getLayoutPosition();
                    Intent intent = new Intent(contexto, DetallesPedidoActv.class);
                    intent.putExtra("pedidoID", listaArticulos.get(itemPosition).getPedidoID());
                    contexto.startActivity(intent);
                }
            });
        }

        public void asignarArticulos(Pedido articulo) {
            if(articulo.getUsuario().equals("")){
                nombre.setText("Usuario no registrado");
            }else{
                nombre.setText(articulo.getUsuario());
            }
            if(articulo.getPagado()){
                pagado.setText("Pagado");
                estatus.setBackgroundColor(Color.GREEN);
            }else{
                pagado.setText("No pagado");
                estatus.setBackgroundColor(Color.RED);
            }
            total.setText("$"+articulo.getTotal().toString());
            asignarFoto("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRloKf8JSFe9QOuGoJp2A97RFoRxnvABK0lpNtE8LPPYk8MPJcm");
        }

        private void asignarFoto(String foto_url) {
            ImageRequest imageRequest = new ImageRequest(foto_url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    foto.setImageBitmap(response);
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