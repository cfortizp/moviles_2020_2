package com.moviles.companys.Util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moviles.companys.R;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter {

    private static LayoutInflater inflater = null;

    Context contexto;
    ArrayList<ArrayList<String>> datos;

    public Adaptador(Context contexto, ArrayList<ArrayList<String>> datos)
    {
        this.contexto = contexto;
        this.datos = datos;
        inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        final View vista = inflater.inflate(R.layout.item_company, null);

        TextView game_name = (TextView) vista.findViewById(R.id.name_company);
        TextView classification = (TextView) vista.findViewById(R.id.clasification);
        game_name.setText(datos.get(i).get(1));
        classification.setText("Classification: "+datos.get(i).get(4));
        return vista;
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
