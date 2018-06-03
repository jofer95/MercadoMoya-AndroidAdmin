package com.example.jorgebarraza.mercadomoya.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Utilerias {

    /**
     * Metodo para mostrar un toast corto en las actividades
     * @param context contexto donde mostrarlos
     * @param mensaje mensaje
     */
    public static void mostrarToast(Context context,String mensaje){
        Toast.makeText(context,mensaje,Toast.LENGTH_LONG).show();
    }


    /**
     * Metodo para mostrar un toas largo en las actividades
     * @param context contexto donde mostrarlo
     * @param mensaje mensaje
     */
    public static void mostrarToastLargo(Context context,String mensaje){
        Toast.makeText(context,mensaje,Toast.LENGTH_LONG).show();
    }

    public static String getPreference(Context context,String key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "");
    }

    public static boolean savePreference(Context context, String key, String value){
        try {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putString(key,value);
            editor.apply();
            editor = null;
            return true;
        } catch(Exception ex) {
            return false;
        }
    }
}
