package com.example.jorgebarraza.mercadomoya.Modelos;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private String pedidoID;
    private String usuario;
    private String tempID;
    private List<ArticuloPedido> articulos;
    private String fecha;
    private Boolean pagado;
    private String estatusPago;
    private Double total;
    private String referenciaPAgo;

    public Pedido() {
        this.articulos = new ArrayList<>();
    }

    public String getPedidoID() {
        return pedidoID;
    }

    public void setPedidoID(String pedidoID) {
        this.pedidoID = pedidoID;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTempID() {
        return tempID;
    }

    public void setTempID(String tempID) {
        this.tempID = tempID;
    }

    public List<ArticuloPedido> getArticulos() {
        return articulos;
    }

    public void setArticulos(List<ArticuloPedido> articulos) {
        this.articulos = articulos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Boolean getPagado() {
        return pagado;
    }

    public void setPagado(Boolean pagado) {
        this.pagado = pagado;
    }

    public String getEstatusPago() {
        return estatusPago;
    }

    public void setEstatusPago(String estatusPago) {
        this.estatusPago = estatusPago;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getReferenciaPAgo() {
        return referenciaPAgo;
    }

    public void setReferenciaPAgo(String referenciaPAgo) {
        this.referenciaPAgo = referenciaPAgo;
    }
}
