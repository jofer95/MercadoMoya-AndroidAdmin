package com.example.jorgebarraza.mercadomoya.DB;

public class Servicios {

    //SERVICIOS DE LA APP
    static String baseURL = "https://mercadomoya.azurewebsites.net/api/";

    //Articulos:
    public static String obtenerTodosLosArticulos(){
        return baseURL + "Articulos/ObtenerTodosLosArticulos";
    }

    public static String obtenerArticuloPorID(){
        return baseURL + "Articulos/ObtenerArticuloPorID";
    }

    public static String crearActualizarArticulo(){
        return baseURL + "Articulos/CrearActualizarArticulo";
    }

    //Categorias:
    public static String obtenerTodasLasCategorias(){
        return baseURL + "Categorias/ObtenerTodasLasCategorias";
    }

    public static String obtenerCategoriaPorID(){
        return baseURL + "Categorias/ObtenerCategoriaPorID";
    }

    public static String crearActualizarCategoria(){
        return baseURL + "Categorias/CrearActualizarCategoria";
    }
}