package com.moviles.companys.Presentation;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.moviles.companys.R;
import com.moviles.companys.Util.Adaptador;
import com.moviles.companys.Util.Constanst;

import java.util.ArrayList;

public class ListCompanyActivity extends AppCompatActivity {

    private Button search;
    private Button add;
    private ListView lista;
    private ArrayList<ArrayList<String>> companies = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listcompany);
        search = findViewById(R.id.search);
        add = findViewById(R.id.add);
        Cursor resultSet = Constanst.db.rawQuery("SELECT a.id, a.name,a.clasification, b.name FROM Companies a, CompanyClasification b WHERE a.clasification = b.id",null);
        while (resultSet.moveToNext()) {
            ArrayList<String> information = new ArrayList<>();
            information.add(resultSet.getString(0));
            information.add(resultSet.getString(1));
            information.add(resultSet.getString(2));
            information.add(resultSet.getString(3));
            companies.add(information);
        }
        load();
    }
    void load(){
        lista = (ListView) findViewById(R.id.lvLista);
        lista.setAdapter(new Adaptador(this, companies));
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), CompanyActivity.class);
                startActivityForResult(intent,0);
            }
        });


    }
}
